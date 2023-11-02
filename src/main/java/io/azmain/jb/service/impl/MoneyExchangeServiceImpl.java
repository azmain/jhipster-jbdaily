package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.repository.MoneyExchangeRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.MoneyExchangeService;
import io.azmain.jb.service.dto.MoneyExchangeDTO;
import io.azmain.jb.service.mapper.MoneyExchangeMapper;
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
 * Service Implementation for managing {@link MoneyExchange}.
 */
@Service
@Transactional
public class MoneyExchangeServiceImpl implements MoneyExchangeService {

    private final Logger log = LoggerFactory.getLogger(MoneyExchangeServiceImpl.class);

    private final MoneyExchangeRepository moneyExchangeRepository;
    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    private final MoneyExchangeMapper moneyExchangeMapper;

    public MoneyExchangeServiceImpl(
        MoneyExchangeRepository moneyExchangeRepository,
        SpringSecurityAuditorAware springSecurityAuditorAware,
        MoneyExchangeMapper moneyExchangeMapper
    ) {
        this.moneyExchangeRepository = moneyExchangeRepository;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
        this.moneyExchangeMapper = moneyExchangeMapper;
    }

    @Override
    public MoneyExchangeDTO save(MoneyExchangeDTO moneyExchangeDTO) {
        log.debug("Request to save MoneyExchange : {}", moneyExchangeDTO);
        MoneyExchange moneyExchange = moneyExchangeMapper.toEntity(moneyExchangeDTO);

        moneyExchange.setCreatedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        moneyExchange.createdDate(Instant.now());

        moneyExchange.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        moneyExchange.lastModifiedDate(Instant.now());

        moneyExchange = moneyExchangeRepository.save(moneyExchange);
        return moneyExchangeMapper.toDto(moneyExchange);
    }

    @Override
    public MoneyExchangeDTO update(MoneyExchangeDTO moneyExchangeDTO) {
        log.debug("Request to update MoneyExchange : {}", moneyExchangeDTO);
        MoneyExchange persistMoneyExchange = moneyExchangeRepository
            .findById(moneyExchangeDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "moneyExchange", "idnotfound"));

        MoneyExchange moneyExchange = moneyExchangeMapper.toEntity(moneyExchangeDTO);

        moneyExchange.setCreatedDate(persistMoneyExchange.getCreatedDate());
        moneyExchange.setCreatedBy(persistMoneyExchange.getCreatedBy());
        moneyExchange.setLastModifiedDate(Instant.now());
        moneyExchange.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

        moneyExchange = moneyExchangeRepository.save(moneyExchange);
        return moneyExchangeMapper.toDto(moneyExchange);
    }

    @Override
    public Optional<MoneyExchangeDTO> partialUpdate(MoneyExchangeDTO moneyExchangeDTO) {
        log.debug("Request to partially update MoneyExchange : {}", moneyExchangeDTO);

        return moneyExchangeRepository
            .findById(moneyExchangeDTO.getId())
            .map(existingMoneyExchange -> {
                moneyExchangeMapper.partialUpdate(existingMoneyExchange, moneyExchangeDTO);

                return existingMoneyExchange;
            })
            .map(moneyExchangeRepository::save)
            .map(moneyExchangeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyExchangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoneyExchanges");
        return moneyExchangeRepository.findAll(pageable).map(moneyExchangeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyExchangeDTO> findOne(Long id) {
        log.debug("Request to get MoneyExchange : {}", id);
        return moneyExchangeRepository.findById(id).map(moneyExchangeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MoneyExchange : {}", id);
        moneyExchangeRepository.deleteById(id);
    }
}
