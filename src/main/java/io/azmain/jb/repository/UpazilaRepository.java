package io.azmain.jb.repository;

import io.azmain.jb.domain.Upazila;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Upazila entity.
 */
@Repository
public interface UpazilaRepository extends JpaRepository<Upazila, Long> {
    default Optional<Upazila> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Upazila> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Upazila> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct upazila from Upazila upazila left join fetch upazila.district",
        countQuery = "select count(distinct upazila) from Upazila upazila"
    )
    Page<Upazila> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct upazila from Upazila upazila left join fetch upazila.district")
    List<Upazila> findAllWithToOneRelationships();

    @Query("select upazila from Upazila upazila left join fetch upazila.district where upazila.id =:id")
    Optional<Upazila> findOneWithToOneRelationships(@Param("id") Long id);
}
