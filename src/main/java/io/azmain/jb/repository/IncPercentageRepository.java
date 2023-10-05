package io.azmain.jb.repository;

import io.azmain.jb.domain.IncPercentage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncPercentage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncPercentageRepository extends JpaRepository<IncPercentage, Long> {}
