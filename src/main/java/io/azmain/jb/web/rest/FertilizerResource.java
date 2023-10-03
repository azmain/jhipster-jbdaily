package io.azmain.jb.web.rest;

import io.azmain.jb.repository.FertilizerRepository;
import io.azmain.jb.service.FertilizerService;
import io.azmain.jb.service.dto.FertilizerDTO;
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
 * REST controller for managing {@link io.azmain.jb.domain.Fertilizer}.
 */
@RestController
@RequestMapping("/api")
public class FertilizerResource {

    private final Logger log = LoggerFactory.getLogger(FertilizerResource.class);

    private static final String ENTITY_NAME = "fertilizer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FertilizerService fertilizerService;

    private final FertilizerRepository fertilizerRepository;

    public FertilizerResource(FertilizerService fertilizerService, FertilizerRepository fertilizerRepository) {
        this.fertilizerService = fertilizerService;
        this.fertilizerRepository = fertilizerRepository;
    }

    /**
     * {@code POST  /fertilizers} : Create a new fertilizer.
     *
     * @param fertilizerDTO the fertilizerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fertilizerDTO, or with status {@code 400 (Bad Request)} if the fertilizer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fertilizers")
    public ResponseEntity<FertilizerDTO> createFertilizer(@Valid @RequestBody FertilizerDTO fertilizerDTO) throws URISyntaxException {
        log.debug("REST request to save Fertilizer : {}", fertilizerDTO);
        if (fertilizerDTO.getId() != null) {
            throw new BadRequestAlertException("A new fertilizer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FertilizerDTO result = fertilizerService.save(fertilizerDTO);
        return ResponseEntity
            .created(new URI("/api/fertilizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fertilizers/:id} : Updates an existing fertilizer.
     *
     * @param id the id of the fertilizerDTO to save.
     * @param fertilizerDTO the fertilizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fertilizerDTO,
     * or with status {@code 400 (Bad Request)} if the fertilizerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fertilizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fertilizers/{id}")
    public ResponseEntity<FertilizerDTO> updateFertilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FertilizerDTO fertilizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fertilizer : {}, {}", id, fertilizerDTO);
        if (fertilizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fertilizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fertilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FertilizerDTO result = fertilizerService.update(fertilizerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fertilizerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fertilizers/:id} : Partial updates given fields of an existing fertilizer, field will ignore if it is null
     *
     * @param id the id of the fertilizerDTO to save.
     * @param fertilizerDTO the fertilizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fertilizerDTO,
     * or with status {@code 400 (Bad Request)} if the fertilizerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fertilizerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fertilizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fertilizers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FertilizerDTO> partialUpdateFertilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FertilizerDTO fertilizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fertilizer partially : {}, {}", id, fertilizerDTO);
        if (fertilizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fertilizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fertilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FertilizerDTO> result = fertilizerService.partialUpdate(fertilizerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fertilizerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fertilizers} : get all the fertilizers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fertilizers in body.
     */
    @GetMapping("/fertilizers")
    public ResponseEntity<List<FertilizerDTO>> getAllFertilizers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Fertilizers");
        Page<FertilizerDTO> page = fertilizerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fertilizers/:id} : get the "id" fertilizer.
     *
     * @param id the id of the fertilizerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fertilizerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fertilizers/{id}")
    public ResponseEntity<FertilizerDTO> getFertilizer(@PathVariable Long id) {
        log.debug("REST request to get Fertilizer : {}", id);
        Optional<FertilizerDTO> fertilizerDTO = fertilizerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fertilizerDTO);
    }

    /**
     * {@code DELETE  /fertilizers/:id} : delete the "id" fertilizer.
     *
     * @param id the id of the fertilizerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fertilizers/{id}")
    public ResponseEntity<Void> deleteFertilizer(@PathVariable Long id) {
        log.debug("REST request to delete Fertilizer : {}", id);
        fertilizerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
