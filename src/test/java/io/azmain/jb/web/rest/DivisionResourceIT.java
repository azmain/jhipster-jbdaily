package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.Division;
import io.azmain.jb.repository.DivisionRepository;
import io.azmain.jb.service.dto.DivisionDTO;
import io.azmain.jb.service.mapper.DivisionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DivisionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private DivisionMapper divisionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDivisionMockMvc;

    private Division division;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createEntity(EntityManager em) {
        Division division = new Division()
            .name(DEFAULT_NAME)
            .bnName(DEFAULT_BN_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return division;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createUpdatedEntity(EntityManager em) {
        Division division = new Division()
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return division;
    }

    @BeforeEach
    public void initTest() {
        division = createEntity(em);
    }

    @Test
    @Transactional
    void createDivision() throws Exception {
        int databaseSizeBeforeCreate = divisionRepository.findAll().size();
        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isCreated());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate + 1);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDivision.getBnName()).isEqualTo(DEFAULT_BN_NAME);
        assertThat(testDivision.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDivision.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDivision.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDivision.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createDivisionWithExistingId() throws Exception {
        // Create the Division with an existing ID
        division.setId(1L);
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        int databaseSizeBeforeCreate = divisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setName(null);

        // Create the Division, which fails.
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setBnName(null);

        // Create the Division, which fails.
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setCreatedBy(null);

        // Create the Division, which fails.
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setCreatedDate(null);

        // Create the Division, which fails.
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDivisions() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bnName").value(hasItem(DEFAULT_BN_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get the division
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, division.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(division.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bnName").value(DEFAULT_BN_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDivision() throws Exception {
        // Get the division
        restDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division
        Division updatedDivision = divisionRepository.findById(division.getId()).get();
        // Disconnect from session so that the updates on updatedDivision are not directly saved in db
        em.detach(updatedDivision);
        updatedDivision
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DivisionDTO divisionDTO = divisionMapper.toDto(updatedDivision);

        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, divisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDivision.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDivision.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDivision.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDivision.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDivision.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, divisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDivision.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDivision.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDivision.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDivision.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDivision.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDivision.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDivision.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDivision.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDivision.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDivision.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, divisionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // Create the Division
        DivisionDTO divisionDTO = divisionMapper.toDto(division);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(divisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeDelete = divisionRepository.findAll().size();

        // Delete the division
        restDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, division.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
