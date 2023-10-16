package io.azmain.jb.service.impl;

import io.azmain.jb.domain.FrRemittance;
import io.azmain.jb.repository.FrRemittanceRepository;
import io.azmain.jb.service.FrRemittanceService;
import io.azmain.jb.service.dto.FrRemittanceDTO;
import io.azmain.jb.service.mapper.FrRemittanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FrRemittance}.
 */
@Service
@Transactional
public class FrRemittanceServiceImpl implements FrRemittanceService {

    private final Logger log = LoggerFactory.getLogger(FrRemittanceServiceImpl.class);

    private final FrRemittanceRepository frRemittanceRepository;

    private final FrRemittanceMapper frRemittanceMapper;

    public FrRemittanceServiceImpl(FrRemittanceRepository frRemittanceRepository, FrRemittanceMapper frRemittanceMapper) {
        this.frRemittanceRepository = frRemittanceRepository;
        this.frRemittanceMapper = frRemittanceMapper;
    }

    @Override
    public FrRemittanceDTO save(FrRemittanceDTO frRemittanceDTO) {
        log.debug("Request to save FrRemittance : {}", frRemittanceDTO);
        FrRemittance frRemittance = frRemittanceMapper.toEntity(frRemittanceDTO);
        frRemittance = frRemittanceRepository.save(frRemittance);
        return frRemittanceMapper.toDto(frRemittance);
    }

    @Override
    public FrRemittanceDTO update(FrRemittanceDTO frRemittanceDTO) {
        log.debug("Request to update FrRemittance : {}", frRemittanceDTO);
        FrRemittance frRemittance = frRemittanceMapper.toEntity(frRemittanceDTO);
        frRemittance = frRemittanceRepository.save(frRemittance);
        return frRemittanceMapper.toDto(frRemittance);
    }

    @Override
    public Optional<FrRemittanceDTO> partialUpdate(FrRemittanceDTO frRemittanceDTO) {
        log.debug("Request to partially update FrRemittance : {}", frRemittanceDTO);

        return frRemittanceRepository
            .findById(frRemittanceDTO.getId())
            .map(existingFrRemittance -> {
                frRemittanceMapper.partialUpdate(existingFrRemittance, frRemittanceDTO);

                return existingFrRemittance;
            })
            .map(frRemittanceRepository::save)
            .map(frRemittanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrRemittanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrRemittances");
        return frRemittanceRepository.findAll(pageable).map(frRemittanceMapper::toDto);
    }

    public Page<FrRemittanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return frRemittanceRepository.findAllWithEagerRelationships(pageable).map(frRemittanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrRemittanceDTO> findOne(Long id) {
        log.debug("Request to get FrRemittance : {}", id);
        return frRemittanceRepository.findOneWithEagerRelationships(id).map(frRemittanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrRemittance : {}", id);
        frRemittanceRepository.deleteById(id);
    }
}
