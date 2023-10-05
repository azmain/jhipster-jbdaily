package io.azmain.jb.repository;

import io.azmain.jb.domain.FrRemittance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FrRemittance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrRemittanceRepository extends JpaRepository<FrRemittance, Long>, JpaSpecificationExecutor<FrRemittance> {}
