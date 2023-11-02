package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.FertilizerRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.FertilizerService;
import io.azmain.jb.service.dto.FertilizerDTO;
import io.azmain.jb.service.mapper.FertilizerMapper;
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
 * Service Implementation for managing {@link Fertilizer}.
 */
@Service
@Transactional
public class FertilizerServiceImpl implements FertilizerService {

    private final Logger log = LoggerFactory.getLogger(FertilizerServiceImpl.class);

    private final FertilizerRepository fertilizerRepository;

    private final FertilizerMapper fertilizerMapper;
    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    public FertilizerServiceImpl(
        FertilizerRepository fertilizerRepository,
        FertilizerMapper fertilizerMapper,
        SpringSecurityAuditorAware springSecurityAuditorAware
    ) {
        this.fertilizerRepository = fertilizerRepository;
        this.fertilizerMapper = fertilizerMapper;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
    }

    @Override
    public FertilizerDTO save(FertilizerDTO fertilizerDTO) {
        log.debug("Request to save Fertilizer : {}", fertilizerDTO);

        Fertilizer fertilizer = fertilizerMapper.toEntity(fertilizerDTO);

        fertilizer.setCreatedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        fertilizer.createdDate(Instant.now());

        fertilizer.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        fertilizer.setLastModifiedDate(Instant.now());

        fertilizer = fertilizerRepository.save(fertilizer);
        return fertilizerMapper.toDto(fertilizer);
    }

    @Override
    public FertilizerDTO update(FertilizerDTO fertilizerDTO) {
        log.debug("Request to update Fertilizer : {}", fertilizerDTO);
        Fertilizer persistFertilizer = fertilizerRepository
            .findById(fertilizerDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "fertilizer", "idnotfound"));

        Fertilizer fertilizer = fertilizerMapper.toEntity(fertilizerDTO);
        fertilizer.setCreatedDate(persistFertilizer.getCreatedDate());
        fertilizer.setCreatedBy(persistFertilizer.getCreatedBy());
        fertilizer.setLastModifiedDate(Instant.now());
        fertilizer.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

        fertilizer = fertilizerRepository.save(fertilizer);
        return fertilizerMapper.toDto(fertilizer);
    }

    @Override
    public Optional<FertilizerDTO> partialUpdate(FertilizerDTO fertilizerDTO) {
        log.debug("Request to partially update Fertilizer : {}", fertilizerDTO);

        return fertilizerRepository
            .findById(fertilizerDTO.getId())
            .map(existingFertilizer -> {
                fertilizerMapper.partialUpdate(existingFertilizer, fertilizerDTO);

                return existingFertilizer;
            })
            .map(fertilizerRepository::save)
            .map(fertilizerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FertilizerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fertilizers");
        return fertilizerRepository.findAll(pageable).map(fertilizerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FertilizerDTO> findOne(Long id) {
        log.debug("Request to get Fertilizer : {}", id);
        return fertilizerRepository.findById(id).map(fertilizerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fertilizer : {}", id);
        fertilizerRepository.deleteById(id);
    }
}
