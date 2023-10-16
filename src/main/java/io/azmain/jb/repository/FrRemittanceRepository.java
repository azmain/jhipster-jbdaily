package io.azmain.jb.repository;

import io.azmain.jb.domain.FrRemittance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FrRemittance entity.
 */
@Repository
public interface FrRemittanceRepository extends JpaRepository<FrRemittance, Long>, JpaSpecificationExecutor<FrRemittance> {
    default Optional<FrRemittance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FrRemittance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FrRemittance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct frRemittance from FrRemittance frRemittance left join fetch frRemittance.moneyExchange left join fetch frRemittance.incPercentage",
        countQuery = "select count(distinct frRemittance) from FrRemittance frRemittance"
    )
    Page<FrRemittance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct frRemittance from FrRemittance frRemittance left join fetch frRemittance.moneyExchange left join fetch frRemittance.incPercentage"
    )
    List<FrRemittance> findAllWithToOneRelationships();

    @Query(
        "select frRemittance from FrRemittance frRemittance left join fetch frRemittance.moneyExchange left join fetch frRemittance.incPercentage where frRemittance.id =:id"
    )
    Optional<FrRemittance> findOneWithToOneRelationships(@Param("id") Long id);
}
