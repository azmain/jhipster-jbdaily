package io.azmain.jb.web.rest;

import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.azmain.jb.domain.PayOrder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PayOrderResource {

    private final Logger log = LoggerFactory.getLogger(PayOrderResource.class);

    private static final String ENTITY_NAME = "payOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayOrderRepository payOrderRepository;

    public PayOrderResource(PayOrderRepository payOrderRepository) {
        this.payOrderRepository = payOrderRepository;
    }

    /**
     * {@code POST  /pay-orders} : Create a new payOrder.
     *
     * @param payOrder the payOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payOrder, or with status {@code 400 (Bad Request)} if the payOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pay-orders")
    public ResponseEntity<PayOrder> createPayOrder(@Valid @RequestBody PayOrder payOrder) throws URISyntaxException {
        log.debug("REST request to save PayOrder : {}", payOrder);
        if (payOrder.getId() != null) {
            throw new BadRequestAlertException("A new payOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayOrder result = payOrderRepository.save(payOrder);
        return ResponseEntity
            .created(new URI("/api/pay-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pay-orders/:id} : Updates an existing payOrder.
     *
     * @param id the id of the payOrder to save.
     * @param payOrder the payOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payOrder,
     * or with status {@code 400 (Bad Request)} if the payOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pay-orders/{id}")
    public ResponseEntity<PayOrder> updatePayOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PayOrder payOrder
    ) throws URISyntaxException {
        log.debug("REST request to update PayOrder : {}, {}", id, payOrder);
        if (payOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayOrder result = payOrderRepository.save(payOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pay-orders/:id} : Partial updates given fields of an existing payOrder, field will ignore if it is null
     *
     * @param id the id of the payOrder to save.
     * @param payOrder the payOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payOrder,
     * or with status {@code 400 (Bad Request)} if the payOrder is not valid,
     * or with status {@code 404 (Not Found)} if the payOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the payOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pay-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayOrder> partialUpdatePayOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PayOrder payOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update PayOrder partially : {}, {}", id, payOrder);
        if (payOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayOrder> result = payOrderRepository
            .findById(payOrder.getId())
            .map(existingPayOrder -> {
                if (payOrder.getPayOrderNumber() != null) {
                    existingPayOrder.setPayOrderNumber(payOrder.getPayOrderNumber());
                }
                if (payOrder.getPayOrderDate() != null) {
                    existingPayOrder.setPayOrderDate(payOrder.getPayOrderDate());
                }
                if (payOrder.getAmount() != null) {
                    existingPayOrder.setAmount(payOrder.getAmount());
                }
                if (payOrder.getSlipNo() != null) {
                    existingPayOrder.setSlipNo(payOrder.getSlipNo());
                }
                if (payOrder.getControllingNo() != null) {
                    existingPayOrder.setControllingNo(payOrder.getControllingNo());
                }

                return existingPayOrder;
            })
            .map(payOrderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /pay-orders} : get all the payOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payOrders in body.
     */
    @GetMapping("/pay-orders")
    public List<PayOrder> getAllPayOrders() {
        log.debug("REST request to get all PayOrders");
        return payOrderRepository.findAll();
    }

    /**
     * {@code GET  /pay-orders/:id} : get the "id" payOrder.
     *
     * @param id the id of the payOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pay-orders/{id}")
    public ResponseEntity<PayOrder> getPayOrder(@PathVariable Long id) {
        log.debug("REST request to get PayOrder : {}", id);
        Optional<PayOrder> payOrder = payOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(payOrder);
    }

    /**
     * {@code DELETE  /pay-orders/:id} : delete the "id" payOrder.
     *
     * @param id the id of the payOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pay-orders/{id}")
    public ResponseEntity<Void> deletePayOrder(@PathVariable Long id) {
        log.debug("REST request to delete PayOrder : {}", id);
        payOrderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
