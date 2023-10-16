package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.District;
import io.azmain.jb.domain.Upazila;
import io.azmain.jb.repository.UpazilaRepository;
import io.azmain.jb.service.UpazilaService;
import io.azmain.jb.service.dto.UpazilaDTO;
import io.azmain.jb.service.mapper.UpazilaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UpazilaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UpazilaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/upazilas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UpazilaRepository upazilaRepository;

    @Mock
    private UpazilaRepository upazilaRepositoryMock;

    @Autowired
    private UpazilaMapper upazilaMapper;

    @Mock
    private UpazilaService upazilaServiceMock;

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
        Upazila upazila = new Upazila()
            .name(DEFAULT_NAME)
            .bnName(DEFAULT_BN_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        upazila.setDistrict(district);
        return upazila;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upazila createUpdatedEntity(EntityManager em) {
        Upazila upazila = new Upazila()
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createUpdatedEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        upazila.setDistrict(district);
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
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);
        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
            .andExpect(status().isCreated());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeCreate + 1);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(DEFAULT_BN_NAME);
        assertThat(testUpazila.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUpazila.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUpazila.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUpazila.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createUpazilaWithExistingId() throws Exception {
        // Create the Upazila with an existing ID
        upazila.setId(1L);
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        int databaseSizeBeforeCreate = upazilaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
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
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
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
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
            .andExpect(status().isBadRequest());

        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = upazilaRepository.findAll().size();
        // set the field null
        upazila.setCreatedBy(null);

        // Create the Upazila, which fails.
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
            .andExpect(status().isBadRequest());

        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = upazilaRepository.findAll().size();
        // set the field null
        upazila.setCreatedDate(null);

        // Create the Upazila, which fails.
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
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
            .andExpect(jsonPath("$.[*].bnName").value(hasItem(DEFAULT_BN_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUpazilasWithEagerRelationshipsIsEnabled() throws Exception {
        when(upazilaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUpazilaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(upazilaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUpazilasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(upazilaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUpazilaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(upazilaRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.bnName").value(DEFAULT_BN_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
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
        updatedUpazila
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(updatedUpazila);

        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, upazilaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database
        List<Upazila> upazilaList = upazilaRepository.findAll();
        assertThat(upazilaList).hasSize(databaseSizeBeforeUpdate);
        Upazila testUpazila = upazilaList.get(upazilaList.size() - 1);
        assertThat(testUpazila.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpazila.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testUpazila.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUpazila.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUpazila.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUpazila.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, upazilaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
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

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
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

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(upazilaDTO)))
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

        partialUpdatedUpazila.name(UPDATED_NAME).createdBy(UPDATED_CREATED_BY).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

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
        assertThat(testUpazila.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUpazila.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUpazila.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUpazila.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
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

        partialUpdatedUpazila
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

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
        assertThat(testUpazila.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUpazila.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUpazila.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUpazila.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUpazila() throws Exception {
        int databaseSizeBeforeUpdate = upazilaRepository.findAll().size();
        upazila.setId(count.incrementAndGet());

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, upazilaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
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

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
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

        // Create the Upazila
        UpazilaDTO upazilaDTO = upazilaMapper.toDto(upazila);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(upazilaDTO))
            )
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
