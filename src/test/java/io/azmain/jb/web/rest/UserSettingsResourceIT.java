package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.UserSettings;
import io.azmain.jb.repository.UserSettingsRepository;
import io.azmain.jb.service.dto.UserSettingsDTO;
import io.azmain.jb.service.mapper.UserSettingsMapper;
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
 * Integration tests for the {@link UserSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSettingsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_ORDER_NUM_SEQ = "AAAAAAAAAA";
    private static final String UPDATED_PAY_ORDER_NUM_SEQ = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_ORDER_CONTROL_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PAY_ORDER_CONTROL_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSettingsMockMvc;

    private UserSettings userSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createEntity(EntityManager em) {
        UserSettings userSettings = new UserSettings()
            .name(DEFAULT_NAME)
            .payOrderNumSeq(DEFAULT_PAY_ORDER_NUM_SEQ)
            .payOrderControlNum(DEFAULT_PAY_ORDER_CONTROL_NUM)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return userSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createUpdatedEntity(EntityManager em) {
        UserSettings userSettings = new UserSettings()
            .name(UPDATED_NAME)
            .payOrderNumSeq(UPDATED_PAY_ORDER_NUM_SEQ)
            .payOrderControlNum(UPDATED_PAY_ORDER_CONTROL_NUM)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return userSettings;
    }

    @BeforeEach
    public void initTest() {
        userSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createUserSettings() throws Exception {
        int databaseSizeBeforeCreate = userSettingsRepository.findAll().size();
        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);
        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserSettings.getPayOrderNumSeq()).isEqualTo(DEFAULT_PAY_ORDER_NUM_SEQ);
        assertThat(testUserSettings.getPayOrderControlNum()).isEqualTo(DEFAULT_PAY_ORDER_CONTROL_NUM);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createUserSettingsWithExistingId() throws Exception {
        // Create the UserSettings with an existing ID
        userSettings.setId(1L);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        int databaseSizeBeforeCreate = userSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setName(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayOrderNumSeqIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setPayOrderNumSeq(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayOrderControlNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setPayOrderControlNum(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setCreatedBy(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setCreatedDate(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        // Get all the userSettingsList
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].payOrderNumSeq").value(hasItem(DEFAULT_PAY_ORDER_NUM_SEQ)))
            .andExpect(jsonPath("$.[*].payOrderControlNum").value(hasItem(DEFAULT_PAY_ORDER_CONTROL_NUM)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        // Get the userSettings
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, userSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSettings.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.payOrderNumSeq").value(DEFAULT_PAY_ORDER_NUM_SEQ))
            .andExpect(jsonPath("$.payOrderControlNum").value(DEFAULT_PAY_ORDER_CONTROL_NUM))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserSettings() throws Exception {
        // Get the userSettings
        restUserSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

        // Update the userSettings
        UserSettings updatedUserSettings = userSettingsRepository.findById(userSettings.getId()).get();
        // Disconnect from session so that the updates on updatedUserSettings are not directly saved in db
        em.detach(updatedUserSettings);
        updatedUserSettings
            .name(UPDATED_NAME)
            .payOrderNumSeq(UPDATED_PAY_ORDER_NUM_SEQ)
            .payOrderControlNum(UPDATED_PAY_ORDER_CONTROL_NUM)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(updatedUserSettings);

        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserSettings.getPayOrderNumSeq()).isEqualTo(UPDATED_PAY_ORDER_NUM_SEQ);
        assertThat(testUserSettings.getPayOrderControlNum()).isEqualTo(UPDATED_PAY_ORDER_CONTROL_NUM);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .payOrderControlNum(UPDATED_PAY_ORDER_CONTROL_NUM)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserSettings.getPayOrderNumSeq()).isEqualTo(DEFAULT_PAY_ORDER_NUM_SEQ);
        assertThat(testUserSettings.getPayOrderControlNum()).isEqualTo(UPDATED_PAY_ORDER_CONTROL_NUM);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .name(UPDATED_NAME)
            .payOrderNumSeq(UPDATED_PAY_ORDER_NUM_SEQ)
            .payOrderControlNum(UPDATED_PAY_ORDER_CONTROL_NUM)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserSettings.getPayOrderNumSeq()).isEqualTo(UPDATED_PAY_ORDER_NUM_SEQ);
        assertThat(testUserSettings.getPayOrderControlNum()).isEqualTo(UPDATED_PAY_ORDER_CONTROL_NUM);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(count.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeDelete = userSettingsRepository.findAll().size();

        // Delete the userSettings
        restUserSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
