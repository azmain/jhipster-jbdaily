package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.repository.FertilizerRepository;
import io.azmain.jb.service.dto.FertilizerDTO;
import io.azmain.jb.service.mapper.FertilizerMapper;
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
 * Integration tests for the {@link FertilizerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FertilizerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fertilizers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FertilizerRepository fertilizerRepository;

    @Autowired
    private FertilizerMapper fertilizerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFertilizerMockMvc;

    private Fertilizer fertilizer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fertilizer createEntity(EntityManager em) {
        Fertilizer fertilizer = new Fertilizer()
            .name(DEFAULT_NAME)
            .bnName(DEFAULT_BN_NAME)
            .accountNo(DEFAULT_ACCOUNT_NO)
            .accountTitle(DEFAULT_ACCOUNT_TITLE);
        return fertilizer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fertilizer createUpdatedEntity(EntityManager em) {
        Fertilizer fertilizer = new Fertilizer()
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .accountNo(UPDATED_ACCOUNT_NO)
            .accountTitle(UPDATED_ACCOUNT_TITLE);
        return fertilizer;
    }

    @BeforeEach
    public void initTest() {
        fertilizer = createEntity(em);
    }

    @Test
    @Transactional
    void createFertilizer() throws Exception {
        int databaseSizeBeforeCreate = fertilizerRepository.findAll().size();
        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);
        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isCreated());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeCreate + 1);
        Fertilizer testFertilizer = fertilizerList.get(fertilizerList.size() - 1);
        assertThat(testFertilizer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFertilizer.getBnName()).isEqualTo(DEFAULT_BN_NAME);
        assertThat(testFertilizer.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testFertilizer.getAccountTitle()).isEqualTo(DEFAULT_ACCOUNT_TITLE);
    }

    @Test
    @Transactional
    void createFertilizerWithExistingId() throws Exception {
        // Create the Fertilizer with an existing ID
        fertilizer.setId(1L);
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        int databaseSizeBeforeCreate = fertilizerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fertilizerRepository.findAll().size();
        // set the field null
        fertilizer.setName(null);

        // Create the Fertilizer, which fails.
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isBadRequest());

        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fertilizerRepository.findAll().size();
        // set the field null
        fertilizer.setBnName(null);

        // Create the Fertilizer, which fails.
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isBadRequest());

        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = fertilizerRepository.findAll().size();
        // set the field null
        fertilizer.setAccountNo(null);

        // Create the Fertilizer, which fails.
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isBadRequest());

        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fertilizerRepository.findAll().size();
        // set the field null
        fertilizer.setAccountTitle(null);

        // Create the Fertilizer, which fails.
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        restFertilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isBadRequest());

        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFertilizers() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        // Get all the fertilizerList
        restFertilizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fertilizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bnName").value(hasItem(DEFAULT_BN_NAME)))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].accountTitle").value(hasItem(DEFAULT_ACCOUNT_TITLE)));
    }

    @Test
    @Transactional
    void getFertilizer() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        // Get the fertilizer
        restFertilizerMockMvc
            .perform(get(ENTITY_API_URL_ID, fertilizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fertilizer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bnName").value(DEFAULT_BN_NAME))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO))
            .andExpect(jsonPath("$.accountTitle").value(DEFAULT_ACCOUNT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingFertilizer() throws Exception {
        // Get the fertilizer
        restFertilizerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFertilizer() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();

        // Update the fertilizer
        Fertilizer updatedFertilizer = fertilizerRepository.findById(fertilizer.getId()).get();
        // Disconnect from session so that the updates on updatedFertilizer are not directly saved in db
        em.detach(updatedFertilizer);
        updatedFertilizer.name(UPDATED_NAME).bnName(UPDATED_BN_NAME).accountNo(UPDATED_ACCOUNT_NO).accountTitle(UPDATED_ACCOUNT_TITLE);
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(updatedFertilizer);

        restFertilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fertilizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
        Fertilizer testFertilizer = fertilizerList.get(fertilizerList.size() - 1);
        assertThat(testFertilizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFertilizer.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testFertilizer.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testFertilizer.getAccountTitle()).isEqualTo(UPDATED_ACCOUNT_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fertilizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fertilizerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFertilizerWithPatch() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();

        // Update the fertilizer using partial update
        Fertilizer partialUpdatedFertilizer = new Fertilizer();
        partialUpdatedFertilizer.setId(fertilizer.getId());

        partialUpdatedFertilizer.name(UPDATED_NAME).bnName(UPDATED_BN_NAME).accountNo(UPDATED_ACCOUNT_NO);

        restFertilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFertilizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFertilizer))
            )
            .andExpect(status().isOk());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
        Fertilizer testFertilizer = fertilizerList.get(fertilizerList.size() - 1);
        assertThat(testFertilizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFertilizer.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testFertilizer.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testFertilizer.getAccountTitle()).isEqualTo(DEFAULT_ACCOUNT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateFertilizerWithPatch() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();

        // Update the fertilizer using partial update
        Fertilizer partialUpdatedFertilizer = new Fertilizer();
        partialUpdatedFertilizer.setId(fertilizer.getId());

        partialUpdatedFertilizer
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .accountNo(UPDATED_ACCOUNT_NO)
            .accountTitle(UPDATED_ACCOUNT_TITLE);

        restFertilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFertilizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFertilizer))
            )
            .andExpect(status().isOk());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
        Fertilizer testFertilizer = fertilizerList.get(fertilizerList.size() - 1);
        assertThat(testFertilizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFertilizer.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testFertilizer.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testFertilizer.getAccountTitle()).isEqualTo(UPDATED_ACCOUNT_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fertilizerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFertilizer() throws Exception {
        int databaseSizeBeforeUpdate = fertilizerRepository.findAll().size();
        fertilizer.setId(count.incrementAndGet());

        // Create the Fertilizer
        FertilizerDTO fertilizerDTO = fertilizerMapper.toDto(fertilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFertilizerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fertilizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fertilizer in the database
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFertilizer() throws Exception {
        // Initialize the database
        fertilizerRepository.saveAndFlush(fertilizer);

        int databaseSizeBeforeDelete = fertilizerRepository.findAll().size();

        // Delete the fertilizer
        restFertilizerMockMvc
            .perform(delete(ENTITY_API_URL_ID, fertilizer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fertilizer> fertilizerList = fertilizerRepository.findAll();
        assertThat(fertilizerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
