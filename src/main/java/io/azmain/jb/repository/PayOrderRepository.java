package io.azmain.jb.repository;

import io.azmain.jb.domain.PayOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PayOrder entity.
 */
@Repository
public interface PayOrderRepository extends JpaRepository<PayOrder, Long>, JpaSpecificationExecutor<PayOrder> {
    default Optional<PayOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PayOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PayOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct payOrder from PayOrder payOrder left join fetch payOrder.fertilizer left join fetch payOrder.dealer",
        countQuery = "select count(distinct payOrder) from PayOrder payOrder"
    )
    Page<PayOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct payOrder from PayOrder payOrder left join fetch payOrder.fertilizer left join fetch payOrder.dealer")
    List<PayOrder> findAllWithToOneRelationships();

    @Query(
        "select payOrder from PayOrder payOrder left join fetch payOrder.fertilizer left join fetch payOrder.dealer where payOrder.id =:id"
    )
    Optional<PayOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
