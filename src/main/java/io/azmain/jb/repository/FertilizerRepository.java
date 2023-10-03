package io.azmain.jb.repository;

import io.azmain.jb.domain.Fertilizer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fertilizer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Long> {}
