package io.azmain.jb.web.rest;

import io.azmain.jb.domain.Upazila;
import io.azmain.jb.repository.UpazilaRepository;
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
 * REST controller for managing {@link io.azmain.jb.domain.Upazila}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UpazilaResource {

    private final Logger log = LoggerFactory.getLogger(UpazilaResource.class);

    private static final String ENTITY_NAME = "upazila";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpazilaRepository upazilaRepository;

    public UpazilaResource(UpazilaRepository upazilaRepository) {
        this.upazilaRepository = upazilaRepository;
    }

    /**
     * {@code POST  /upazilas} : Create a new upazila.
     *
     * @param upazila the upazila to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new upazila, or with status {@code 400 (Bad Request)} if the upazila has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upazilas")
    public ResponseEntity<Upazila> createUpazila(@Valid @RequestBody Upazila upazila) throws URISyntaxException {
        log.debug("REST request to save Upazila : {}", upazila);
        if (upazila.getId() != null) {
            throw new BadRequestAlertException("A new upazila cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Upazila result = upazilaRepository.save(upazila);
        return ResponseEntity
            .created(new URI("/api/upazilas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upazilas/:id} : Updates an existing upazila.
     *
     * @param id the id of the upazila to save.
     * @param upazila the upazila to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upazila,
     * or with status {@code 400 (Bad Request)} if the upazila is not valid,
     * or with status {@code 500 (Internal Server Error)} if the upazila couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upazilas/{id}")
    public ResponseEntity<Upazila> updateUpazila(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Upazila upazila
    ) throws URISyntaxException {
        log.debug("REST request to update Upazila : {}, {}", id, upazila);
        if (upazila.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upazila.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upazilaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Upazila result = upazilaRepository.save(upazila);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upazila.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /upazilas/:id} : Partial updates given fields of an existing upazila, field will ignore if it is null
     *
     * @param id the id of the upazila to save.
     * @param upazila the upazila to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upazila,
     * or with status {@code 400 (Bad Request)} if the upazila is not valid,
     * or with status {@code 404 (Not Found)} if the upazila is not found,
     * or with status {@code 500 (Internal Server Error)} if the upazila couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/upazilas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Upazila> partialUpdateUpazila(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Upazila upazila
    ) throws URISyntaxException {
        log.debug("REST request to partial update Upazila partially : {}, {}", id, upazila);
        if (upazila.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upazila.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upazilaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Upazila> result = upazilaRepository
            .findById(upazila.getId())
            .map(existingUpazila -> {
                if (upazila.getName() != null) {
                    existingUpazila.setName(upazila.getName());
                }
                if (upazila.getBnName() != null) {
                    existingUpazila.setBnName(upazila.getBnName());
                }

                return existingUpazila;
            })
            .map(upazilaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upazila.getId().toString())
        );
    }

    /**
     * {@code GET  /upazilas} : get all the upazilas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of upazilas in body.
     */
    @GetMapping("/upazilas")
    public List<Upazila> getAllUpazilas() {
        log.debug("REST request to get all Upazilas");
        return upazilaRepository.findAll();
    }

    /**
     * {@code GET  /upazilas/:id} : get the "id" upazila.
     *
     * @param id the id of the upazila to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upazila, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upazilas/{id}")
    public ResponseEntity<Upazila> getUpazila(@PathVariable Long id) {
        log.debug("REST request to get Upazila : {}", id);
        Optional<Upazila> upazila = upazilaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(upazila);
    }

    /**
     * {@code DELETE  /upazilas/:id} : delete the "id" upazila.
     *
     * @param id the id of the upazila to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upazilas/{id}")
    public ResponseEntity<Void> deleteUpazila(@PathVariable Long id) {
        log.debug("REST request to delete Upazila : {}", id);
        upazilaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
