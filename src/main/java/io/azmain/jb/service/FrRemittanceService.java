package io.azmain.jb.service;

import io.azmain.jb.service.dto.FrRemittanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.FrRemittance}.
 */
public interface FrRemittanceService {
    /**
     * Save a frRemittance.
     *
     * @param frRemittanceDTO the entity to save.
     * @return the persisted entity.
     */
    FrRemittanceDTO save(FrRemittanceDTO frRemittanceDTO);

    /**
     * Updates a frRemittance.
     *
     * @param frRemittanceDTO the entity to update.
     * @return the persisted entity.
     */
    FrRemittanceDTO update(FrRemittanceDTO frRemittanceDTO);

    /**
     * Partially updates a frRemittance.
     *
     * @param frRemittanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FrRemittanceDTO> partialUpdate(FrRemittanceDTO frRemittanceDTO);

    /**
     * Get all the frRemittances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrRemittanceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" frRemittance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FrRemittanceDTO> findOne(Long id);

    /**
     * Delete the "id" frRemittance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
