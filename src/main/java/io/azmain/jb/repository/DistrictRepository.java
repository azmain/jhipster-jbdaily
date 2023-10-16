package io.azmain.jb.repository;

import io.azmain.jb.domain.District;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the District entity.
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    default Optional<District> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<District> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<District> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct district from District district left join fetch district.division",
        countQuery = "select count(distinct district) from District district"
    )
    Page<District> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct district from District district left join fetch district.division")
    List<District> findAllWithToOneRelationships();

    @Query("select district from District district left join fetch district.division where district.id =:id")
    Optional<District> findOneWithToOneRelationships(@Param("id") Long id);
}
