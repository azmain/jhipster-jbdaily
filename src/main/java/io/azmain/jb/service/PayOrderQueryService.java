package io.azmain.jb.service;

import io.azmain.jb.domain.*; // for static metamodels
import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.service.criteria.PayOrderCriteria;
import io.azmain.jb.service.dto.PayOrderDTO;
import io.azmain.jb.service.mapper.PayOrderMapper;
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

/**
 * Service for executing complex queries for {@link PayOrder} entities in the database.
 * The main input is a {@link PayOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PayOrderDTO} or a {@link Page} of {@link PayOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PayOrderQueryService extends QueryService<PayOrder> {

    private final Logger log = LoggerFactory.getLogger(PayOrderQueryService.class);

    private final PayOrderRepository payOrderRepository;

    private final PayOrderMapper payOrderMapper;

    public PayOrderQueryService(PayOrderRepository payOrderRepository, PayOrderMapper payOrderMapper) {
        this.payOrderRepository = payOrderRepository;
        this.payOrderMapper = payOrderMapper;
    }

    /**
     * Return a {@link List} of {@link PayOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PayOrderDTO> findByCriteria(PayOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PayOrder> specification = createSpecification(criteria);
        return payOrderMapper.toDto(payOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PayOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PayOrderDTO> findByCriteria(PayOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PayOrder> specification = createSpecification(criteria);
        return payOrderRepository.findAll(specification, page).map(payOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PayOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PayOrder> specification = createSpecification(criteria);
        return payOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link PayOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PayOrder> createSpecification(PayOrderCriteria criteria) {
        Specification<PayOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PayOrder_.id));
            }
            if (criteria.getPayOrderNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayOrderNumber(), PayOrder_.payOrderNumber));
            }
            if (criteria.getPayOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayOrderDate(), PayOrder_.payOrderDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PayOrder_.amount));
            }
            if (criteria.getSlipNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlipNo(), PayOrder_.slipNo));
            }
            if (criteria.getControllingNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getControllingNo(), PayOrder_.controllingNo));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), PayOrder_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), PayOrder_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), PayOrder_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), PayOrder_.lastModifiedDate));
            }
            if (criteria.getFertilizerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFertilizerId(),
                            root -> root.join(PayOrder_.fertilizer, JoinType.LEFT).get(Fertilizer_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDealerId(), root -> root.join(PayOrder_.dealer, JoinType.LEFT).get(Dealer_.id))
                    );
            }
        }
        return specification;
    }
}
