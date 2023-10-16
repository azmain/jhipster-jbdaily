package io.azmain.jb.service.impl;

import io.azmain.jb.domain.Upazila;
import io.azmain.jb.repository.UpazilaRepository;
import io.azmain.jb.service.UpazilaService;
import io.azmain.jb.service.dto.UpazilaDTO;
import io.azmain.jb.service.mapper.UpazilaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Upazila}.
 */
@Service
@Transactional
public class UpazilaServiceImpl implements UpazilaService {

    private final Logger log = LoggerFactory.getLogger(UpazilaServiceImpl.class);

    private final UpazilaRepository upazilaRepository;

    private final UpazilaMapper upazilaMapper;

    public UpazilaServiceImpl(UpazilaRepository upazilaRepository, UpazilaMapper upazilaMapper) {
        this.upazilaRepository = upazilaRepository;
        this.upazilaMapper = upazilaMapper;
    }

    @Override
    public UpazilaDTO save(UpazilaDTO upazilaDTO) {
        log.debug("Request to save Upazila : {}", upazilaDTO);
        Upazila upazila = upazilaMapper.toEntity(upazilaDTO);
        upazila = upazilaRepository.save(upazila);
        return upazilaMapper.toDto(upazila);
    }

    @Override
    public UpazilaDTO update(UpazilaDTO upazilaDTO) {
        log.debug("Request to update Upazila : {}", upazilaDTO);
        Upazila upazila = upazilaMapper.toEntity(upazilaDTO);
        upazila = upazilaRepository.save(upazila);
        return upazilaMapper.toDto(upazila);
    }

    @Override
    public Optional<UpazilaDTO> partialUpdate(UpazilaDTO upazilaDTO) {
        log.debug("Request to partially update Upazila : {}", upazilaDTO);

        return upazilaRepository
            .findById(upazilaDTO.getId())
            .map(existingUpazila -> {
                upazilaMapper.partialUpdate(existingUpazila, upazilaDTO);

                return existingUpazila;
            })
            .map(upazilaRepository::save)
            .map(upazilaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UpazilaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Upazilas");
        return upazilaRepository.findAll(pageable).map(upazilaMapper::toDto);
    }

    public Page<UpazilaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return upazilaRepository.findAllWithEagerRelationships(pageable).map(upazilaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UpazilaDTO> findOne(Long id) {
        log.debug("Request to get Upazila : {}", id);
        return upazilaRepository.findOneWithEagerRelationships(id).map(upazilaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Upazila : {}", id);
        upazilaRepository.deleteById(id);
    }
}
