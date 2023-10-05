package io.azmain.jb.repository;

import io.azmain.jb.domain.MoneyExchange;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyExchange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyExchangeRepository extends JpaRepository<MoneyExchange, Long> {}
