package io.azmain.jb.web.rest;

import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.repository.FertilizerRepository;
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
 * REST controller for managing {@link io.azmain.jb.domain.Fertilizer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FertilizerResource {

    private final Logger log = LoggerFactory.getLogger(FertilizerResource.class);

    private static final String ENTITY_NAME = "fertilizer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FertilizerRepository fertilizerRepository;

    public FertilizerResource(FertilizerRepository fertilizerRepository) {
        this.fertilizerRepository = fertilizerRepository;
    }

    /**
     * {@code POST  /fertilizers} : Create a new fertilizer.
     *
     * @param fertilizer the fertilizer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fertilizer, or with status {@code 400 (Bad Request)} if the fertilizer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fertilizers")
    public ResponseEntity<Fertilizer> createFertilizer(@Valid @RequestBody Fertilizer fertilizer) throws URISyntaxException {
        log.debug("REST request to save Fertilizer : {}", fertilizer);
        if (fertilizer.getId() != null) {
            throw new BadRequestAlertException("A new fertilizer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fertilizer result = fertilizerRepository.save(fertilizer);
        return ResponseEntity
            .created(new URI("/api/fertilizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fertilizers/:id} : Updates an existing fertilizer.
     *
     * @param id the id of the fertilizer to save.
     * @param fertilizer the fertilizer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fertilizer,
     * or with status {@code 400 (Bad Request)} if the fertilizer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fertilizer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fertilizers/{id}")
    public ResponseEntity<Fertilizer> updateFertilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fertilizer fertilizer
    ) throws URISyntaxException {
        log.debug("REST request to update Fertilizer : {}, {}", id, fertilizer);
        if (fertilizer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fertilizer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fertilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fertilizer result = fertilizerRepository.save(fertilizer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fertilizer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fertilizers/:id} : Partial updates given fields of an existing fertilizer, field will ignore if it is null
     *
     * @param id the id of the fertilizer to save.
     * @param fertilizer the fertilizer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fertilizer,
     * or with status {@code 400 (Bad Request)} if the fertilizer is not valid,
     * or with status {@code 404 (Not Found)} if the fertilizer is not found,
     * or with status {@code 500 (Internal Server Error)} if the fertilizer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fertilizers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fertilizer> partialUpdateFertilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fertilizer fertilizer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fertilizer partially : {}, {}", id, fertilizer);
        if (fertilizer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fertilizer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fertilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fertilizer> result = fertilizerRepository
            .findById(fertilizer.getId())
            .map(existingFertilizer -> {
                if (fertilizer.getName() != null) {
                    existingFertilizer.setName(fertilizer.getName());
                }
                if (fertilizer.getBnName() != null) {
                    existingFertilizer.setBnName(fertilizer.getBnName());
                }
                if (fertilizer.getAccountNo() != null) {
                    existingFertilizer.setAccountNo(fertilizer.getAccountNo());
                }
                if (fertilizer.getAccountTitle() != null) {
                    existingFertilizer.setAccountTitle(fertilizer.getAccountTitle());
                }

                return existingFertilizer;
            })
            .map(fertilizerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fertilizer.getId().toString())
        );
    }

    /**
     * {@code GET  /fertilizers} : get all the fertilizers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fertilizers in body.
     */
    @GetMapping("/fertilizers")
    public List<Fertilizer> getAllFertilizers() {
        log.debug("REST request to get all Fertilizers");
        return fertilizerRepository.findAll();
    }

    /**
     * {@code GET  /fertilizers/:id} : get the "id" fertilizer.
     *
     * @param id the id of the fertilizer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fertilizer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fertilizers/{id}")
    public ResponseEntity<Fertilizer> getFertilizer(@PathVariable Long id) {
        log.debug("REST request to get Fertilizer : {}", id);
        Optional<Fertilizer> fertilizer = fertilizerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fertilizer);
    }

    /**
     * {@code DELETE  /fertilizers/:id} : delete the "id" fertilizer.
     *
     * @param id the id of the fertilizer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fertilizers/{id}")
    public ResponseEntity<Void> deleteFertilizer(@PathVariable Long id) {
        log.debug("REST request to delete Fertilizer : {}", id);
        fertilizerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
