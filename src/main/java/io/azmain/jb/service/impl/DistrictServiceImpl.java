package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.District;
import io.azmain.jb.domain.Division;
import io.azmain.jb.repository.DistrictRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.DistrictService;
import io.azmain.jb.service.dto.DistrictDTO;
import io.azmain.jb.service.mapper.DistrictMapper;
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
 * Service Implementation for managing {@link District}.
 */
@Service
@Transactional
public class DistrictServiceImpl implements DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictServiceImpl.class);

    private final DistrictRepository districtRepository;
    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    private final DistrictMapper districtMapper;

    public DistrictServiceImpl(
        DistrictRepository districtRepository,
        SpringSecurityAuditorAware springSecurityAuditorAware,
        DistrictMapper districtMapper
    ) {
        this.districtRepository = districtRepository;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
        this.districtMapper = districtMapper;
    }

    @Override
    public DistrictDTO save(DistrictDTO districtDTO) {
        log.debug("Request to save District : {}", districtDTO);
        District district = districtMapper.toEntity(districtDTO);

        district.setCreatedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        district.createdDate(Instant.now());

        district.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));
        district.setLastModifiedDate(Instant.now());

        district = districtRepository.save(district);
        return districtMapper.toDto(district);
    }

    @Override
    public DistrictDTO update(DistrictDTO districtDTO) {
        log.debug("Request to update District : {}", districtDTO);

        District persistDistrict = districtRepository
            .findById(districtDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "district", "idnotfound"));

        District district = districtMapper.toEntity(districtDTO);

        district.setCreatedDate(persistDistrict.getCreatedDate());
        district.setCreatedBy(persistDistrict.getCreatedBy());
        district.setLastModifiedDate(Instant.now());
        district.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

        district = districtRepository.save(district);
        return districtMapper.toDto(district);
    }

    @Override
    public Optional<DistrictDTO> partialUpdate(DistrictDTO districtDTO) {
        log.debug("Request to partially update District : {}", districtDTO);

        return districtRepository
            .findById(districtDTO.getId())
            .map(existingDistrict -> {
                districtMapper.partialUpdate(existingDistrict, districtDTO);

                return existingDistrict;
            })
            .map(districtRepository::save)
            .map(districtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DistrictDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Districts");
        return districtRepository.findAll(pageable).map(districtMapper::toDto);
    }

    public Page<DistrictDTO> findAllWithEagerRelationships(Pageable pageable) {
        return districtRepository.findAllWithEagerRelationships(pageable).map(districtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DistrictDTO> findOne(Long id) {
        log.debug("Request to get District : {}", id);
        return districtRepository.findOneWithEagerRelationships(id).map(districtMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        districtRepository.deleteById(id);
    }
}
