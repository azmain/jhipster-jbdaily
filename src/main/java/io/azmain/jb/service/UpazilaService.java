package io.azmain.jb.service;

import io.azmain.jb.service.dto.UpazilaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.Upazila}.
 */
public interface UpazilaService {
    /**
     * Save a upazila.
     *
     * @param upazilaDTO the entity to save.
     * @return the persisted entity.
     */
    UpazilaDTO save(UpazilaDTO upazilaDTO);

    /**
     * Updates a upazila.
     *
     * @param upazilaDTO the entity to update.
     * @return the persisted entity.
     */
    UpazilaDTO update(UpazilaDTO upazilaDTO);

    /**
     * Partially updates a upazila.
     *
     * @param upazilaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UpazilaDTO> partialUpdate(UpazilaDTO upazilaDTO);

    /**
     * Get all the upazilas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UpazilaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" upazila.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UpazilaDTO> findOne(Long id);

    /**
     * Delete the "id" upazila.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
