package io.azmain.jb.service;

import io.azmain.jb.service.dto.FertilizerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.Fertilizer}.
 */
public interface FertilizerService {
    /**
     * Save a fertilizer.
     *
     * @param fertilizerDTO the entity to save.
     * @return the persisted entity.
     */
    FertilizerDTO save(FertilizerDTO fertilizerDTO);

    /**
     * Updates a fertilizer.
     *
     * @param fertilizerDTO the entity to update.
     * @return the persisted entity.
     */
    FertilizerDTO update(FertilizerDTO fertilizerDTO);

    /**
     * Partially updates a fertilizer.
     *
     * @param fertilizerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FertilizerDTO> partialUpdate(FertilizerDTO fertilizerDTO);

    /**
     * Get all the fertilizers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FertilizerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fertilizer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FertilizerDTO> findOne(Long id);

    /**
     * Delete the "id" fertilizer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
