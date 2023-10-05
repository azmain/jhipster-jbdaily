package io.azmain.jb.web.rest;

import io.azmain.jb.repository.FrRemittanceRepository;
import io.azmain.jb.service.FrRemittanceQueryService;
import io.azmain.jb.service.FrRemittanceService;
import io.azmain.jb.service.criteria.FrRemittanceCriteria;
import io.azmain.jb.service.dto.FrRemittanceDTO;
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
 * REST controller for managing {@link io.azmain.jb.domain.FrRemittance}.
 */
@RestController
@RequestMapping("/api")
public class FrRemittanceResource {

    private final Logger log = LoggerFactory.getLogger(FrRemittanceResource.class);

    private static final String ENTITY_NAME = "frRemittance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrRemittanceService frRemittanceService;

    private final FrRemittanceRepository frRemittanceRepository;

    private final FrRemittanceQueryService frRemittanceQueryService;

    public FrRemittanceResource(
        FrRemittanceService frRemittanceService,
        FrRemittanceRepository frRemittanceRepository,
        FrRemittanceQueryService frRemittanceQueryService
    ) {
        this.frRemittanceService = frRemittanceService;
        this.frRemittanceRepository = frRemittanceRepository;
        this.frRemittanceQueryService = frRemittanceQueryService;
    }

    /**
     * {@code POST  /fr-remittances} : Create a new frRemittance.
     *
     * @param frRemittanceDTO the frRemittanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frRemittanceDTO, or with status {@code 400 (Bad Request)} if the frRemittance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fr-remittances")
    public ResponseEntity<FrRemittanceDTO> createFrRemittance(@Valid @RequestBody FrRemittanceDTO frRemittanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save FrRemittance : {}", frRemittanceDTO);
        if (frRemittanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new frRemittance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrRemittanceDTO result = frRemittanceService.save(frRemittanceDTO);
        return ResponseEntity
            .created(new URI("/api/fr-remittances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fr-remittances/:id} : Updates an existing frRemittance.
     *
     * @param id the id of the frRemittanceDTO to save.
     * @param frRemittanceDTO the frRemittanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frRemittanceDTO,
     * or with status {@code 400 (Bad Request)} if the frRemittanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frRemittanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fr-remittances/{id}")
    public ResponseEntity<FrRemittanceDTO> updateFrRemittance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrRemittanceDTO frRemittanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FrRemittance : {}, {}", id, frRemittanceDTO);
        if (frRemittanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frRemittanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frRemittanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrRemittanceDTO result = frRemittanceService.update(frRemittanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frRemittanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fr-remittances/:id} : Partial updates given fields of an existing frRemittance, field will ignore if it is null
     *
     * @param id the id of the frRemittanceDTO to save.
     * @param frRemittanceDTO the frRemittanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frRemittanceDTO,
     * or with status {@code 400 (Bad Request)} if the frRemittanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frRemittanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frRemittanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fr-remittances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FrRemittanceDTO> partialUpdateFrRemittance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrRemittanceDTO frRemittanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrRemittance partially : {}, {}", id, frRemittanceDTO);
        if (frRemittanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frRemittanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frRemittanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrRemittanceDTO> result = frRemittanceService.partialUpdate(frRemittanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frRemittanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fr-remittances} : get all the frRemittances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frRemittances in body.
     */
    @GetMapping("/fr-remittances")
    public ResponseEntity<List<FrRemittanceDTO>> getAllFrRemittances(
        FrRemittanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FrRemittances by criteria: {}", criteria);
        Page<FrRemittanceDTO> page = frRemittanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fr-remittances/count} : count all the frRemittances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fr-remittances/count")
    public ResponseEntity<Long> countFrRemittances(FrRemittanceCriteria criteria) {
        log.debug("REST request to count FrRemittances by criteria: {}", criteria);
        return ResponseEntity.ok().body(frRemittanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fr-remittances/:id} : get the "id" frRemittance.
     *
     * @param id the id of the frRemittanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frRemittanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fr-remittances/{id}")
    public ResponseEntity<FrRemittanceDTO> getFrRemittance(@PathVariable Long id) {
        log.debug("REST request to get FrRemittance : {}", id);
        Optional<FrRemittanceDTO> frRemittanceDTO = frRemittanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frRemittanceDTO);
    }

    /**
     * {@code DELETE  /fr-remittances/:id} : delete the "id" frRemittance.
     *
     * @param id the id of the frRemittanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fr-remittances/{id}")
    public ResponseEntity<Void> deleteFrRemittance(@PathVariable Long id) {
        log.debug("REST request to delete FrRemittance : {}", id);
        frRemittanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
