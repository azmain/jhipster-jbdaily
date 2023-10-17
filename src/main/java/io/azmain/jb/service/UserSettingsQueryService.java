package io.azmain.jb.service;

import io.azmain.jb.domain.*;
import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.repository.UserSettingsRepository;
import io.azmain.jb.service.criteria.PayOrderCriteria;
import io.azmain.jb.service.criteria.UserSettingsCriteria;
import io.azmain.jb.service.dto.PayOrderDTO;
import io.azmain.jb.service.dto.UserSettingsDTO;
import io.azmain.jb.service.mapper.PayOrderMapper;
import io.azmain.jb.service.mapper.UserSettingsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class UserSettingsQueryService extends QueryService<UserSettings> {

    private final Logger log = LoggerFactory.getLogger(UserSettingsQueryService.class);

    private final UserSettingsRepository userSettingsRepository;

    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsQueryService(UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
    }

    @Transactional(readOnly = true)
    public Page<UserSettingsDTO> findByCriteria(UserSettingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserSettings> specification = createSpecification(criteria);
        return userSettingsRepository.findAll(specification, page).map(userSettingsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(UserSettingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserSettings> specification = createSpecification(criteria);
        return userSettingsRepository.count(specification);
    }

    protected Specification<UserSettings> createSpecification(UserSettingsCriteria criteria) {
        Specification<UserSettings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserSettings_.id));
            }

            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), UserSettings_.createdBy));
            }
        }
        return specification;
    }
}
