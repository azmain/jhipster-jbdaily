package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.Upazila;
import io.azmain.jb.repository.UpazilaRepository;
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
 * Integration tests for the {@link UpazilaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UpazilaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/upazilas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UpazilaRepository upazilaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUpazilaMockMvc;

    private Upazila upazila;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upazila createEntity(EntityManager em) {
        Upazila upazila = new Upazila().name(DEFAULT_NAME).bnName(DEFAULT_BN_NAME);
        return upazila;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upazila createUpdatedEntity(EntityManager em) {
        Upazila upazila = new Upazila().name(UPDATED_NAME).bnName(UPDATED_BN_NAME);
        return upazila;
    }

    @BeforeEach
    public void initTest() {
        upazila = createEntity(em);
    }

    @Test
    @Transactional
    void createUpazila() throws Exception {
        int databaseSizeBeforeCreate = upazilaRepository.findAll().size();
        // Create the Upazila
        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isCreated());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeCreate + 1);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(DEFAULT_BN_NAME);
    }

    @Test
    @Transactional
    void createUpazilaWithExistingId() throws Exception {
        // Create the Upazila with an existing ID
        upazila.setId(1L);

        int databaseSizeBeforeCreate = upazilaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = upazilaRepository.findAll().size();
        // set the field null
        upazila.setName(null);

        // Create the Upazila, which fails.

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isBadRequest());

        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = upazilaRepository.findAll().size();
        // set the field null
        upazila.setBnName(null);

        // Create the Upazila, which fails.

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isBadRequest());

        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUpazilas() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upazila.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bnName").value(hasItem(DEFAULT_BN_NAME)));
    }

    @Test
    @Transactional
    void getUpazila() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        // Get the upazila
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL_ID, upazila.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(upazila.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bnName").value(DEFAULT_BN_NAME));
    }

    @Test
    @Transactional
    void getNonExistingUpazila() throws Exception {
        // Get the upazila
        restUpazilaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUpazila() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();

        // Update the upazila
        Upazila updatedUpazila = upazilaRepository.findById(upazila.getId()).get();
        // Disconnect from session so that the updates on updatedUpazila are not directly saved in db
        em.detach(updatedUpazila);
        updatedUpazila.name(UPDATED_NAME).bnName(UPDATED_BN_NAME);

        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUpazila.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(UPDATED_BN_NAME);
    }

    @Test
    @Transactional
    void putNonExistingUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, upazila.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUpazilaWithPatch() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();

        // Update the upazila using partial update
        Upazila partialUpdatedUpazila = new Upazila();
        partialUpdatedUpazila.setId(upazila.getId());

        partialUpdatedUpazila.name(UPDATED_NAME);

        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpazila.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(DEFAULT_BN_NAME);
    }

    @Test
    @Transactional
    void fullUpdateUpazilaWithPatch() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();

        // Update the upazila using partial update
        Upazila partialUpdatedUpazila = new Upazila();
        partialUpdatedUpazila.setId(upazila.getId());

        partialUpdatedUpazila.name(UPDATED_NAME).bnName(UPDATED_BN_NAME);

        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpazila.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(UPDATED_BN_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, upazila.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(upazila)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUpazila() throws Exception {
        // Initialize the database
        upazilaRepository.saveAndFlush(upazila);

        int databaseSizeBeforeDelete = upazilaRepository.findAll().size();

        // Delete the upazila
        restUpazilaMockMvc
            .perform(delete(ENTITY_API_URL_ID, upazila.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
