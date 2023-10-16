package io.azmain.jb.service;

import io.azmain.jb.domain.*; // for static metamodels
import io.azmain.jb.domain.FrRemittance;
import io.azmain.jb.repository.FrRemittanceRepository;
import io.azmain.jb.service.criteria.FrRemittanceCriteria;
import io.azmain.jb.service.dto.FrRemittanceDTO;
import io.azmain.jb.service.mapper.FrRemittanceMapper;
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
 * Service for executing complex queries for {@link FrRemittance} entities in the database.
 * The main input is a {@link FrRemittanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FrRemittanceDTO} or a {@link Page} of {@link FrRemittanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FrRemittanceQueryService extends QueryService<FrRemittance> {

    private final Logger log = LoggerFactory.getLogger(FrRemittanceQueryService.class);

    private final FrRemittanceRepository frRemittanceRepository;

    private final FrRemittanceMapper frRemittanceMapper;

    public FrRemittanceQueryService(FrRemittanceRepository frRemittanceRepository, FrRemittanceMapper frRemittanceMapper) {
        this.frRemittanceRepository = frRemittanceRepository;
        this.frRemittanceMapper = frRemittanceMapper;
    }

    /**
     * Return a {@link List} of {@link FrRemittanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FrRemittanceDTO> findByCriteria(FrRemittanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FrRemittance> specification = createSpecification(criteria);
        return frRemittanceMapper.toDto(frRemittanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FrRemittanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FrRemittanceDTO> findByCriteria(FrRemittanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FrRemittance> specification = createSpecification(criteria);
        return frRemittanceRepository.findAll(specification, page).map(frRemittanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FrRemittanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FrRemittance> specification = createSpecification(criteria);
        return frRemittanceRepository.count(specification);
    }

    /**
     * Function to convert {@link FrRemittanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FrRemittance> createSpecification(FrRemittanceCriteria criteria) {
        Specification<FrRemittance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FrRemittance_.id));
            }
            if (criteria.getPin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPin(), FrRemittance_.pin));
            }
            if (criteria.getRemitersName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemitersName(), FrRemittance_.remitersName));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAmount(), FrRemittance_.amount));
            }
            if (criteria.getIncentiveAmount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIncentiveAmount(), FrRemittance_.incentiveAmount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), FrRemittance_.paymentDate));
            }
            if (criteria.getIncPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIncPaymentDate(), FrRemittance_.incPaymentDate));
            }
            if (criteria.getRemiSendingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemiSendingDate(), FrRemittance_.remiSendingDate));
            }
            if (criteria.getRemiFrCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemiFrCurrency(), FrRemittance_.remiFrCurrency));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), FrRemittance_.currency));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), FrRemittance_.country));
            }
            if (criteria.getExchangeRate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExchangeRate(), FrRemittance_.exchangeRate));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), FrRemittance_.transactionType));
            }
            if (criteria.getRecvMobileNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecvMobileNo(), FrRemittance_.recvMobileNo));
            }
            if (criteria.getRecvName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecvName(), FrRemittance_.recvName));
            }
            if (criteria.getRecvLegalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecvLegalId(), FrRemittance_.recvLegalId));
            }
            if (criteria.getMoneyExchangeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMoneyExchangeName(), FrRemittance_.moneyExchangeName));
            }
            if (criteria.getAmountReimDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountReimDate(), FrRemittance_.amountReimDate));
            }
            if (criteria.getIncAmountReimDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIncAmountReimDate(), FrRemittance_.incAmountReimDate));
            }
            if (criteria.getRecvGender() != null) {
                specification = specification.and(buildSpecification(criteria.getRecvGender(), FrRemittance_.recvGender));
            }
            if (criteria.getRemiGender() != null) {
                specification = specification.and(buildSpecification(criteria.getRemiGender(), FrRemittance_.remiGender));
            }
            if (criteria.getDocumentType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentType(), FrRemittance_.documentType));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), FrRemittance_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FrRemittance_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), FrRemittance_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), FrRemittance_.lastModifiedDate));
            }
            if (criteria.getMoneyExchangeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMoneyExchangeId(),
                            root -> root.join(FrRemittance_.moneyExchange, JoinType.LEFT).get(MoneyExchange_.id)
                        )
                    );
            }
            if (criteria.getIncPercentageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIncPercentageId(),
                            root -> root.join(FrRemittance_.incPercentage, JoinType.LEFT).get(IncPercentage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
