package io.azmain.jb.service;

import io.azmain.jb.service.dto.MoneyExchangeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.MoneyExchange}.
 */
public interface MoneyExchangeService {
    /**
     * Save a moneyExchange.
     *
     * @param moneyExchangeDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyExchangeDTO save(MoneyExchangeDTO moneyExchangeDTO);

    /**
     * Updates a moneyExchange.
     *
     * @param moneyExchangeDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyExchangeDTO update(MoneyExchangeDTO moneyExchangeDTO);

    /**
     * Partially updates a moneyExchange.
     *
     * @param moneyExchangeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyExchangeDTO> partialUpdate(MoneyExchangeDTO moneyExchangeDTO);

    /**
     * Get all the moneyExchanges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyExchangeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moneyExchange.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyExchangeDTO> findOne(Long id);

    /**
     * Delete the "id" moneyExchange.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
