package io.azmain.jb.web.rest;

import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.service.PayOrderQueryService;
import io.azmain.jb.service.PayOrderService;
import io.azmain.jb.service.criteria.PayOrderCriteria;
import io.azmain.jb.service.dto.PayOrderDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.azmain.jb.domain.PayOrder}.
 */
@RestController
@RequestMapping("/api")
public class PayOrderResource {

    private final Logger log = LoggerFactory.getLogger(PayOrderResource.class);

    private static final String ENTITY_NAME = "payOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayOrderService payOrderService;

    private final PayOrderRepository payOrderRepository;

    private final PayOrderQueryService payOrderQueryService;

    public PayOrderResource(
        PayOrderService payOrderService,
        PayOrderRepository payOrderRepository,
        PayOrderQueryService payOrderQueryService
    ) {
        this.payOrderService = payOrderService;
        this.payOrderRepository = payOrderRepository;
        this.payOrderQueryService = payOrderQueryService;
    }

    /**
     * {@code POST  /pay-orders} : Create a new payOrder.
     *
     * @param payOrderDTO the payOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payOrderDTO, or with status {@code 400 (Bad Request)} if the payOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pay-orders")
    public ResponseEntity<PayOrderDTO> createPayOrder(@Valid @RequestBody PayOrderDTO payOrderDTO) throws URISyntaxException {
        log.debug("REST request to save PayOrder : {}", payOrderDTO);
        if (payOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new payOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (payOrderRepository.existsByPayOrderNumber(payOrderDTO.getPayOrderNumber())) {
            throw new BadRequestAlertException("A PayOrder already have same PayOrderNumber", ENTITY_NAME, "idexists");
        }
        PayOrderDTO result = payOrderService.save(payOrderDTO);
        return ResponseEntity
            .created(new URI("/api/pay-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pay-orders/:id} : Updates an existing payOrder.
     *
     * @param id the id of the payOrderDTO to save.
     * @param payOrderDTO the payOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payOrderDTO,
     * or with status {@code 400 (Bad Request)} if the payOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pay-orders/{id}")
    public ResponseEntity<PayOrderDTO> updatePayOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PayOrderDTO payOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PayOrder : {}, {}", id, payOrderDTO);
        if (payOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayOrderDTO result = payOrderService.update(payOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pay-orders/:id} : Partial updates given fields of an existing payOrder, field will ignore if it is null
     *
     * @param id the id of the payOrderDTO to save.
     * @param payOrderDTO the payOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payOrderDTO,
     * or with status {@code 400 (Bad Request)} if the payOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the payOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the payOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pay-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayOrderDTO> partialUpdatePayOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PayOrderDTO payOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PayOrder partially : {}, {}", id, payOrderDTO);
        if (payOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayOrderDTO> result = payOrderService.partialUpdate(payOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pay-orders} : get all the payOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payOrders in body.
     */
    @GetMapping("/pay-orders")
    public ResponseEntity<List<PayOrderDTO>> getAllPayOrders(
        PayOrderCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PayOrders by criteria: {}", criteria);
        Page<PayOrderDTO> page = payOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-orders/count} : count all the payOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pay-orders/count")
    public ResponseEntity<Long> countPayOrders(PayOrderCriteria criteria) {
        log.debug("REST request to count PayOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(payOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pay-orders/:id} : get the "id" payOrder.
     *
     * @param id the id of the payOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pay-orders/{id}")
    public ResponseEntity<PayOrderDTO> getPayOrder(@PathVariable Long id) {
        log.debug("REST request to get PayOrder : {}", id);
        Optional<PayOrderDTO> payOrderDTO = payOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payOrderDTO);
    }

    /**
     * {@code DELETE  /pay-orders/:id} : delete the "id" payOrder.
     *
     * @param id the id of the payOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pay-orders/{id}")
    public ResponseEntity<Void> deletePayOrder(@PathVariable Long id) {
        log.debug("REST request to delete PayOrder : {}", id);
        payOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
