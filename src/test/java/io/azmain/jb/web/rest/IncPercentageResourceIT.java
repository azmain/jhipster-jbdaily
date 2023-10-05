package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.repository.IncPercentageRepository;
import io.azmain.jb.service.dto.IncPercentageDTO;
import io.azmain.jb.service.mapper.IncPercentageMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IncPercentageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncPercentageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inc-percentages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncPercentageRepository incPercentageRepository;

    @Autowired
    private IncPercentageMapper incPercentageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncPercentageMockMvc;

    private IncPercentage incPercentage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncPercentage createEntity(EntityManager em) {
        IncPercentage incPercentage = new IncPercentage().name(DEFAULT_NAME);
        return incPercentage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncPercentage createUpdatedEntity(EntityManager em) {
        IncPercentage incPercentage = new IncPercentage().name(UPDATED_NAME);
        return incPercentage;
    }

    @BeforeEach
    public void initTest() {
        incPercentage = createEntity(em);
    }

    @Test
    @Transactional
    void createIncPercentage() throws Exception {
        int databaseSizeBeforeCreate = incPercentageRepository.findAll().size();
        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);
        restIncPercentageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeCreate + 1);
        IncPercentage testIncPercentage = incPercentageList.get(incPercentageList.size() - 1);
        assertThat(testIncPercentage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createIncPercentageWithExistingId() throws Exception {
        // Create the IncPercentage with an existing ID
        incPercentage.setId(1L);
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        int databaseSizeBeforeCreate = incPercentageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncPercentageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = incPercentageRepository.findAll().size();
        // set the field null
        incPercentage.setName(null);

        // Create the IncPercentage, which fails.
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        restIncPercentageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncPercentages() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        // Get all the incPercentageList
        restIncPercentageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incPercentage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIncPercentage() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        // Get the incPercentage
        restIncPercentageMockMvc
            .perform(get(ENTITY_API_URL_ID, incPercentage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incPercentage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIncPercentage() throws Exception {
        // Get the incPercentage
        restIncPercentageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncPercentage() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();

        // Update the incPercentage
        IncPercentage updatedIncPercentage = incPercentageRepository.findById(incPercentage.getId()).get();
        // Disconnect from session so that the updates on updatedIncPercentage are not directly saved in db
        em.detach(updatedIncPercentage);
        updatedIncPercentage.name(UPDATED_NAME);
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(updatedIncPercentage);

        restIncPercentageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incPercentageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isOk());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
        IncPercentage testIncPercentage = incPercentageList.get(incPercentageList.size() - 1);
        assertThat(testIncPercentage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incPercentageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncPercentageWithPatch() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();

        // Update the incPercentage using partial update
        IncPercentage partialUpdatedIncPercentage = new IncPercentage();
        partialUpdatedIncPercentage.setId(incPercentage.getId());

        restIncPercentageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncPercentage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncPercentage))
            )
            .andExpect(status().isOk());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
        IncPercentage testIncPercentage = incPercentageList.get(incPercentageList.size() - 1);
        assertThat(testIncPercentage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIncPercentageWithPatch() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();

        // Update the incPercentage using partial update
        IncPercentage partialUpdatedIncPercentage = new IncPercentage();
        partialUpdatedIncPercentage.setId(incPercentage.getId());

        partialUpdatedIncPercentage.name(UPDATED_NAME);

        restIncPercentageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncPercentage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncPercentage))
            )
            .andExpect(status().isOk());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
        IncPercentage testIncPercentage = incPercentageList.get(incPercentageList.size() - 1);
        assertThat(testIncPercentage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incPercentageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncPercentage() throws Exception {
        int databaseSizeBeforeUpdate = incPercentageRepository.findAll().size();
        incPercentage.setId(count.incrementAndGet());

        // Create the IncPercentage
        IncPercentageDTO incPercentageDTO = incPercentageMapper.toDto(incPercentage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncPercentageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incPercentageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncPercentage in the database
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncPercentage() throws Exception {
        // Initialize the database
        incPercentageRepository.saveAndFlush(incPercentage);

        int databaseSizeBeforeDelete = incPercentageRepository.findAll().size();

        // Delete the incPercentage
        restIncPercentageMockMvc
            .perform(delete(ENTITY_API_URL_ID, incPercentage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncPercentage> incPercentageList = incPercentageRepository.findAll();
        assertThat(incPercentageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
