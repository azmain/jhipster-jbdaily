package io.azmain.jb.web.rest;

import io.azmain.jb.repository.UpazilaRepository;
import io.azmain.jb.service.UpazilaService;
import io.azmain.jb.service.dto.UpazilaDTO;
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
 * REST controller for managing {@link io.azmain.jb.domain.Upazila}.
 */
@RestController
@RequestMapping("/api")
public class UpazilaResource {

    private final Logger log = LoggerFactory.getLogger(UpazilaResource.class);

    private static final String ENTITY_NAME = "upazila";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpazilaService upazilaService;

    private final UpazilaRepository upazilaRepository;

    public UpazilaResource(UpazilaService upazilaService, UpazilaRepository upazilaRepository) {
        this.upazilaService = upazilaService;
        this.upazilaRepository = upazilaRepository;
    }

    /**
     * {@code POST  /upazilas} : Create a new upazila.
     *
     * @param upazilaDTO the upazilaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new upazilaDTO, or with status {@code 400 (Bad Request)} if the upazila has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upazilas")
    public ResponseEntity<UpazilaDTO> createUpazila(@Valid @RequestBody UpazilaDTO upazilaDTO) throws URISyntaxException {
        log.debug("REST request to save Upazila : {}", upazilaDTO);
        if (upazilaDTO.getId() != null) {
            throw new BadRequestAlertException("A new upazila cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UpazilaDTO result = upazilaService.save(upazilaDTO);
        return ResponseEntity
            .created(new URI("/api/upazilas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upazilas/:id} : Updates an existing upazila.
     *
     * @param id the id of the upazilaDTO to save.
     * @param upazilaDTO the upazilaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upazilaDTO,
     * or with status {@code 400 (Bad Request)} if the upazilaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the upazilaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upazilas/{id}")
    public ResponseEntity<UpazilaDTO> updateUpazila(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UpazilaDTO upazilaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Upazila : {}, {}", id, upazilaDTO);
        if (upazilaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upazilaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upazilaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UpazilaDTO result = upazilaService.update(upazilaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upazilaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /upazilas/:id} : Partial updates given fields of an existing upazila, field will ignore if it is null
     *
     * @param id the id of the upazilaDTO to save.
     * @param upazilaDTO the upazilaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upazilaDTO,
     * or with status {@code 400 (Bad Request)} if the upazilaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the upazilaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the upazilaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/upazilas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UpazilaDTO> partialUpdateUpazila(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UpazilaDTO upazilaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Upazila partially : {}, {}", id, upazilaDTO);
        if (upazilaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upazilaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upazilaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UpazilaDTO> result = upazilaService.partialUpdate(upazilaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upazilaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upazilas} : get all the upazilas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of upazilas in body.
     */
    @GetMapping("/upazilas")
    public ResponseEntity<List<UpazilaDTO>> getAllUpazilas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Upazilas");
        Page<UpazilaDTO> page = upazilaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /upazilas/:id} : get the "id" upazila.
     *
     * @param id the id of the upazilaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upazilaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upazilas/{id}")
    public ResponseEntity<UpazilaDTO> getUpazila(@PathVariable Long id) {
        log.debug("REST request to get Upazila : {}", id);
        Optional<UpazilaDTO> upazilaDTO = upazilaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(upazilaDTO);
    }

    /**
     * {@code DELETE  /upazilas/:id} : delete the "id" upazila.
     *
     * @param id the id of the upazilaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upazilas/{id}")
    public ResponseEntity<Void> deleteUpazila(@PathVariable Long id) {
        log.debug("REST request to delete Upazila : {}", id);
        upazilaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
