package io.azmain.jb.service;

import io.azmain.jb.service.dto.PayOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.azmain.jb.domain.PayOrder}.
 */
public interface PayOrderService {
    /**
     * Save a payOrder.
     *
     * @param payOrderDTO the entity to save.
     * @return the persisted entity.
     */
    PayOrderDTO save(PayOrderDTO payOrderDTO);

    /**
     * Updates a payOrder.
     *
     * @param payOrderDTO the entity to update.
     * @return the persisted entity.
     */
    PayOrderDTO update(PayOrderDTO payOrderDTO);

    /**
     * Partially updates a payOrder.
     *
     * @param payOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PayOrderDTO> partialUpdate(PayOrderDTO payOrderDTO);

    /**
     * Get all the payOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" payOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayOrderDTO> findOne(Long id);

    /**
     * Delete the "id" payOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
