package io.azmain.jb.service;

import io.azmain.jb.service.dto.IncPercentageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.IncPercentage}.
 */
public interface IncPercentageService {
    /**
     * Save a incPercentage.
     *
     * @param incPercentageDTO the entity to save.
     * @return the persisted entity.
     */
    IncPercentageDTO save(IncPercentageDTO incPercentageDTO);

    /**
     * Updates a incPercentage.
     *
     * @param incPercentageDTO the entity to update.
     * @return the persisted entity.
     */
    IncPercentageDTO update(IncPercentageDTO incPercentageDTO);

    /**
     * Partially updates a incPercentage.
     *
     * @param incPercentageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IncPercentageDTO> partialUpdate(IncPercentageDTO incPercentageDTO);

    /**
     * Get all the incPercentages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IncPercentageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" incPercentage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IncPercentageDTO> findOne(Long id);

    /**
     * Delete the "id" incPercentage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
