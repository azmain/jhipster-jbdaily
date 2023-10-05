package io.azmain.jb.web.rest;

import io.azmain.jb.repository.MoneyExchangeRepository;
import io.azmain.jb.service.MoneyExchangeService;
import io.azmain.jb.service.dto.MoneyExchangeDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.azmain.jb.domain.MoneyExchange}.
 */
@RestController
@RequestMapping("/api")
public class MoneyExchangeResource {

    private final Logger log = LoggerFactory.getLogger(MoneyExchangeResource.class);

    private static final String ENTITY_NAME = "moneyExchange";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyExchangeService moneyExchangeService;

    private final MoneyExchangeRepository moneyExchangeRepository;

    public MoneyExchangeResource(MoneyExchangeService moneyExchangeService, MoneyExchangeRepository moneyExchangeRepository) {
        this.moneyExchangeService = moneyExchangeService;
        this.moneyExchangeRepository = moneyExchangeRepository;
    }

    /**
     * {@code POST  /money-exchanges} : Create a new moneyExchange.
     *
     * @param moneyExchangeDTO the moneyExchangeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyExchangeDTO, or with status {@code 400 (Bad Request)} if the moneyExchange has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/money-exchanges")
    public ResponseEntity<MoneyExchangeDTO> createMoneyExchange(@Valid @RequestBody MoneyExchangeDTO moneyExchangeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MoneyExchange : {}", moneyExchangeDTO);
        if (moneyExchangeDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyExchange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoneyExchangeDTO result = moneyExchangeService.save(moneyExchangeDTO);
        return ResponseEntity
            .created(new URI("/api/money-exchanges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /money-exchanges/:id} : Updates an existing moneyExchange.
     *
     * @param id the id of the moneyExchangeDTO to save.
     * @param moneyExchangeDTO the moneyExchangeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyExchangeDTO,
     * or with status {@code 400 (Bad Request)} if the moneyExchangeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyExchangeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/money-exchanges/{id}")
    public ResponseEntity<MoneyExchangeDTO> updateMoneyExchange(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyExchangeDTO moneyExchangeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MoneyExchange : {}, {}", id, moneyExchangeDTO);
        if (moneyExchangeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyExchangeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyExchangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoneyExchangeDTO result = moneyExchangeService.update(moneyExchangeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyExchangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /money-exchanges/:id} : Partial updates given fields of an existing moneyExchange, field will ignore if it is null
     *
     * @param id the id of the moneyExchangeDTO to save.
     * @param moneyExchangeDTO the moneyExchangeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyExchangeDTO,
     * or with status {@code 400 (Bad Request)} if the moneyExchangeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyExchangeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyExchangeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/money-exchanges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyExchangeDTO> partialUpdateMoneyExchange(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyExchangeDTO moneyExchangeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MoneyExchange partially : {}, {}", id, moneyExchangeDTO);
        if (moneyExchangeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyExchangeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyExchangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyExchangeDTO> result = moneyExchangeService.partialUpdate(moneyExchangeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyExchangeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-exchanges} : get all the moneyExchanges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyExchanges in body.
     */
    @GetMapping("/money-exchanges")
    public ResponseEntity<List<MoneyExchangeDTO>> getAllMoneyExchanges(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MoneyExchanges");
        Page<MoneyExchangeDTO> page = moneyExchangeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-exchanges/:id} : get the "id" moneyExchange.
     *
     * @param id the id of the moneyExchangeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyExchangeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/money-exchanges/{id}")
    public ResponseEntity<MoneyExchangeDTO> getMoneyExchange(@PathVariable Long id) {
        log.debug("REST request to get MoneyExchange : {}", id);
        Optional<MoneyExchangeDTO> moneyExchangeDTO = moneyExchangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyExchangeDTO);
    }

    /**
     * {@code DELETE  /money-exchanges/:id} : delete the "id" moneyExchange.
     *
     * @param id the id of the moneyExchangeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/money-exchanges/{id}")
    public ResponseEntity<Void> deleteMoneyExchange(@PathVariable Long id) {
        log.debug("REST request to delete MoneyExchange : {}", id);
        moneyExchangeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
