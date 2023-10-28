package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.Dealer;
import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.repository.DealerRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.DealerService;
import io.azmain.jb.service.dto.DealerDTO;
import io.azmain.jb.service.mapper.DealerMapper;
import io.azmain.jb.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dealer}.
 */
@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private final Logger log = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;
    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    public DealerServiceImpl(
        DealerRepository dealerRepository,
        DealerMapper dealerMapper,
        SpringSecurityAuditorAware springSecurityAuditorAware
    ) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
    }

    @Override
    public DealerDTO save(DealerDTO dealerDTO) {
        log.debug("Request to save Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);

        dealer.setCreatedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        dealer.createdDate(Instant.now());

        dealer = dealerRepository.save(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public DealerDTO update(DealerDTO dealerDTO) {
        log.debug("Request to update Dealer : {}", dealerDTO);

        Dealer persistFertilizer = dealerRepository
            .findById(dealerDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "dealer", "idnotfound"));

        Dealer dealer = dealerMapper.toEntity(dealerDTO);

        dealer.setCreatedDate(persistFertilizer.getCreatedDate());
        dealer.setCreatedBy(persistFertilizer.getCreatedBy());
        dealer.setLastModifiedDate(Instant.now());
        dealer.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

        dealer = dealerRepository.save(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public Optional<DealerDTO> partialUpdate(DealerDTO dealerDTO) {
        log.debug("Request to partially update Dealer : {}", dealerDTO);

        return dealerRepository
            .findById(dealerDTO.getId())
            .map(existingDealer -> {
                dealerMapper.partialUpdate(existingDealer, dealerDTO);

                return existingDealer;
            })
            .map(dealerRepository::save)
            .map(dealerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dealers");
        return dealerRepository.findAll(pageable).map(dealerMapper::toDto);
    }

    public Page<DealerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dealerRepository.findAllWithEagerRelationships(pageable).map(dealerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealerDTO> findOne(Long id) {
        log.debug("Request to get Dealer : {}", id);
        return dealerRepository.findOneWithEagerRelationships(id).map(dealerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dealer : {}", id);
        dealerRepository.deleteById(id);
    }
}
