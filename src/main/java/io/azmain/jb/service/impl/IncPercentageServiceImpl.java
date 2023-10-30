package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.District;
import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.repository.IncPercentageRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.IncPercentageService;
import io.azmain.jb.service.dto.IncPercentageDTO;
import io.azmain.jb.service.mapper.IncPercentageMapper;
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
 * Service Implementation for managing {@link IncPercentage}.
 */
@Service
@Transactional
public class IncPercentageServiceImpl implements IncPercentageService {

    private final Logger log = LoggerFactory.getLogger(IncPercentageServiceImpl.class);

    private final IncPercentageRepository incPercentageRepository;
    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    private final IncPercentageMapper incPercentageMapper;

    public IncPercentageServiceImpl(
        IncPercentageRepository incPercentageRepository,
        SpringSecurityAuditorAware springSecurityAuditorAware,
        IncPercentageMapper incPercentageMapper
    ) {
        this.incPercentageRepository = incPercentageRepository;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
        this.incPercentageMapper = incPercentageMapper;
    }

    @Override
    public IncPercentageDTO save(IncPercentageDTO incPercentageDTO) {
        log.debug("Request to save IncPercentage : {}", incPercentageDTO);
        IncPercentage incPercentage = incPercentageMapper.toEntity(incPercentageDTO);

        incPercentage.setCreatedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        incPercentage.createdDate(Instant.now());

        incPercentage = incPercentageRepository.save(incPercentage);
        return incPercentageMapper.toDto(incPercentage);
    }

    @Override
    public IncPercentageDTO update(IncPercentageDTO incPercentageDTO) {
        log.debug("Request to update IncPercentage : {}", incPercentageDTO);
        IncPercentage persistIncPercentage = incPercentageRepository
            .findById(incPercentageDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "incPercentage", "idnotfound"));

        IncPercentage incPercentage = incPercentageMapper.toEntity(incPercentageDTO);

        incPercentage.setCreatedDate(persistIncPercentage.getCreatedDate());
        incPercentage.setCreatedBy(persistIncPercentage.getCreatedBy());
        incPercentage.setLastModifiedDate(Instant.now());
        incPercentage.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

        incPercentage = incPercentageRepository.save(incPercentage);
        return incPercentageMapper.toDto(incPercentage);
    }

    @Override
    public Optional<IncPercentageDTO> partialUpdate(IncPercentageDTO incPercentageDTO) {
        log.debug("Request to partially update IncPercentage : {}", incPercentageDTO);

        return incPercentageRepository
            .findById(incPercentageDTO.getId())
            .map(existingIncPercentage -> {
                incPercentageMapper.partialUpdate(existingIncPercentage, incPercentageDTO);

                return existingIncPercentage;
            })
            .map(incPercentageRepository::save)
            .map(incPercentageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncPercentageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IncPercentages");
        return incPercentageRepository.findAll(pageable).map(incPercentageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IncPercentageDTO> findOne(Long id) {
        log.debug("Request to get IncPercentage : {}", id);
        return incPercentageRepository.findById(id).map(incPercentageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncPercentage : {}", id);
        incPercentageRepository.deleteById(id);
    }
}
