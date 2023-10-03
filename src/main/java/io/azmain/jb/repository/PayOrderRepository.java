package io.azmain.jb.repository;

import io.azmain.jb.domain.PayOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PayOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayOrderRepository extends JpaRepository<PayOrder, Long> {}
