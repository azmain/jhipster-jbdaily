package io.azmain.jb.service;

import io.azmain.jb.domain.*;
import io.azmain.jb.repository.DealerRepository;
import io.azmain.jb.service.criteria.DealerCriteria;
import io.azmain.jb.service.criteria.PayOrderCriteria;
import io.azmain.jb.service.dto.DealerDTO;
import io.azmain.jb.service.dto.PayOrderDTO;
import io.azmain.jb.service.mapper.DealerMapper;
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
public class DealerQueryService extends QueryService<Dealer> {

    private final Logger log = LoggerFactory.getLogger(DealerQueryService.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    public DealerQueryService(DealerRepository dealerRepository, DealerMapper dealerMapper) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
    }

    @Transactional(readOnly = true)
    public Page<DealerDTO> findByCriteria(DealerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerRepository.findAll(specification, page).map(dealerMapper::toDto);
    }

    protected Specification<Dealer> createSpecification(DealerCriteria criteria) {
        Specification<Dealer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dealer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Dealer_.name));
            }
        }
        return specification;
    }
}
