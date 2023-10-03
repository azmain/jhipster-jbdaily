package io.azmain.jb.repository;

import io.azmain.jb.domain.Upazila;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Upazila entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpazilaRepository extends JpaRepository<Upazila, Long> {}
