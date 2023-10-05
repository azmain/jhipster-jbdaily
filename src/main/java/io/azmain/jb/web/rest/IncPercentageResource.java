package io.azmain.jb.web.rest;

import io.azmain.jb.repository.IncPercentageRepository;
import io.azmain.jb.service.IncPercentageService;
import io.azmain.jb.service.dto.IncPercentageDTO;
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
 * REST controller for managing {@link io.azmain.jb.domain.IncPercentage}.
 */
@RestController
@RequestMapping("/api")
public class IncPercentageResource {

    private final Logger log = LoggerFactory.getLogger(IncPercentageResource.class);

    private static final String ENTITY_NAME = "incPercentage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncPercentageService incPercentageService;

    private final IncPercentageRepository incPercentageRepository;

    public IncPercentageResource(IncPercentageService incPercentageService, IncPercentageRepository incPercentageRepository) {
        this.incPercentageService = incPercentageService;
        this.incPercentageRepository = incPercentageRepository;
    }

    /**
     * {@code POST  /inc-percentages} : Create a new incPercentage.
     *
     * @param incPercentageDTO the incPercentageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incPercentageDTO, or with status {@code 400 (Bad Request)} if the incPercentage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inc-percentages")
    public ResponseEntity<IncPercentageDTO> createIncPercentage(@Valid @RequestBody IncPercentageDTO incPercentageDTO)
        throws URISyntaxException {
        log.debug("REST request to save IncPercentage : {}", incPercentageDTO);
        if (incPercentageDTO.getId() != null) {
            throw new BadRequestAlertException("A new incPercentage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IncPercentageDTO result = incPercentageService.save(incPercentageDTO);
        return ResponseEntity
            .created(new URI("/api/inc-percentages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inc-percentages/:id} : Updates an existing incPercentage.
     *
     * @param id the id of the incPercentageDTO to save.
     * @param incPercentageDTO the incPercentageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incPercentageDTO,
     * or with status {@code 400 (Bad Request)} if the incPercentageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incPercentageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inc-percentages/{id}")
    public ResponseEntity<IncPercentageDTO> updateIncPercentage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncPercentageDTO incPercentageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IncPercentage : {}, {}", id, incPercentageDTO);
        if (incPercentageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incPercentageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incPercentageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IncPercentageDTO result = incPercentageService.update(incPercentageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incPercentageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inc-percentages/:id} : Partial updates given fields of an existing incPercentage, field will ignore if it is null
     *
     * @param id the id of the incPercentageDTO to save.
     * @param incPercentageDTO the incPercentageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incPercentageDTO,
     * or with status {@code 400 (Bad Request)} if the incPercentageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the incPercentageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the incPercentageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inc-percentages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncPercentageDTO> partialUpdateIncPercentage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncPercentageDTO incPercentageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IncPercentage partially : {}, {}", id, incPercentageDTO);
        if (incPercentageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incPercentageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incPercentageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncPercentageDTO> result = incPercentageService.partialUpdate(incPercentageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incPercentageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inc-percentages} : get all the incPercentages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incPercentages in body.
     */
    @GetMapping("/inc-percentages")
    public ResponseEntity<List<IncPercentageDTO>> getAllIncPercentages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of IncPercentages");
        Page<IncPercentageDTO> page = incPercentageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inc-percentages/:id} : get the "id" incPercentage.
     *
     * @param id the id of the incPercentageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incPercentageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inc-percentages/{id}")
    public ResponseEntity<IncPercentageDTO> getIncPercentage(@PathVariable Long id) {
        log.debug("REST request to get IncPercentage : {}", id);
        Optional<IncPercentageDTO> incPercentageDTO = incPercentageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incPercentageDTO);
    }

    /**
     * {@code DELETE  /inc-percentages/:id} : delete the "id" incPercentage.
     *
     * @param id the id of the incPercentageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inc-percentages/{id}")
    public ResponseEntity<Void> deleteIncPercentage(@PathVariable Long id) {
        log.debug("REST request to delete IncPercentage : {}", id);
        incPercentageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
