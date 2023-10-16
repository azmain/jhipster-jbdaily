package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.District;
import io.azmain.jb.domain.Division;
import io.azmain.jb.repository.DistrictRepository;
import io.azmain.jb.service.DistrictService;
import io.azmain.jb.service.dto.DistrictDTO;
import io.azmain.jb.service.mapper.DistrictMapper;
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
 * Integration tests for the {@link DistrictResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DistrictResourceIT {

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

    private static final String ENTITY_API_URL = "/api/districts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DistrictRepository districtRepository;

    @Mock
    private DistrictRepository districtRepositoryMock;

    @Autowired
    private DistrictMapper districtMapper;

    @Mock
    private DistrictService districtServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistrictMockMvc;

    private District district;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createEntity(EntityManager em) {
        District district = new District()
            .name(DEFAULT_NAME)
            .bnName(DEFAULT_BN_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        district.setDivision(division);
        return district;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createUpdatedEntity(EntityManager em) {
        District district = new District()
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createUpdatedEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        district.setDivision(division);
        return district;
    }

    @BeforeEach
    public void initTest() {
        district = createEntity(em);
    }

    @Test
    @Transactional
    void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();
        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDistrict.getBnName()).isEqualTo(DEFAULT_BN_NAME);
        assertThat(testDistrict.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDistrict.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDistrict.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createDistrictWithExistingId() throws Exception {
        // Create the District with an existing ID
        district.setId(1L);
        DistrictDTO districtDTO = districtMapper.toDto(district);

        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setName(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setBnName(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setCreatedBy(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setCreatedDate(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bnName").value(hasItem(DEFAULT_BN_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDistrictsWithEagerRelationshipsIsEnabled() throws Exception {
        when(districtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDistrictMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(districtServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDistrictsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(districtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDistrictMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(districtRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get the district
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL_ID, district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bnName").value(DEFAULT_BN_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        District updatedDistrict = districtRepository.findById(district.getId()).get();
        // Disconnect from session so that the updates on updatedDistrict are not directly saved in db
        em.detach(updatedDistrict);
        updatedDistrict
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DistrictDTO districtDTO = districtMapper.toDto(updatedDistrict);

        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistrict.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDistrict.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDistrict.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDistrict.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistrict.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDistrict.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDistrict.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDistrict.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict
            .name(UPDATED_NAME)
            .bnName(UPDATED_BN_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistrict.getBnName()).isEqualTo(UPDATED_BN_NAME);
        assertThat(testDistrict.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDistrict.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDistrict.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeDelete = districtRepository.findAll().size();

        // Delete the district
        restDistrictMockMvc
            .perform(delete(ENTITY_API_URL_ID, district.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
