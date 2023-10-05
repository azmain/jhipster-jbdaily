package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.repository.MoneyExchangeRepository;
import io.azmain.jb.service.dto.MoneyExchangeDTO;
import io.azmain.jb.service.mapper.MoneyExchangeMapper;
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
 * Integration tests for the {@link MoneyExchangeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyExchangeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DIGIT = "AAAAAAAAAA";
    private static final String UPDATED_DIGIT = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/money-exchanges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoneyExchangeRepository moneyExchangeRepository;

    @Autowired
    private MoneyExchangeMapper moneyExchangeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyExchangeMockMvc;

    private MoneyExchange moneyExchange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyExchange createEntity(EntityManager em) {
        MoneyExchange moneyExchange = new MoneyExchange()
            .name(DEFAULT_NAME)
            .digit(DEFAULT_DIGIT)
            .link(DEFAULT_LINK)
            .shortName(DEFAULT_SHORT_NAME);
        return moneyExchange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyExchange createUpdatedEntity(EntityManager em) {
        MoneyExchange moneyExchange = new MoneyExchange()
            .name(UPDATED_NAME)
            .digit(UPDATED_DIGIT)
            .link(UPDATED_LINK)
            .shortName(UPDATED_SHORT_NAME);
        return moneyExchange;
    }

    @BeforeEach
    public void initTest() {
        moneyExchange = createEntity(em);
    }

    @Test
    @Transactional
    void createMoneyExchange() throws Exception {
        int databaseSizeBeforeCreate = moneyExchangeRepository.findAll().size();
        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);
        restMoneyExchangeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeCreate + 1);
        MoneyExchange testMoneyExchange = moneyExchangeList.get(moneyExchangeList.size() - 1);
        assertThat(testMoneyExchange.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMoneyExchange.getDigit()).isEqualTo(DEFAULT_DIGIT);
        assertThat(testMoneyExchange.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMoneyExchange.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void createMoneyExchangeWithExistingId() throws Exception {
        // Create the MoneyExchange with an existing ID
        moneyExchange.setId(1L);
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        int databaseSizeBeforeCreate = moneyExchangeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyExchangeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyExchangeRepository.findAll().size();
        // set the field null
        moneyExchange.setName(null);

        // Create the MoneyExchange, which fails.
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        restMoneyExchangeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDigitIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyExchangeRepository.findAll().size();
        // set the field null
        moneyExchange.setDigit(null);

        // Create the MoneyExchange, which fails.
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        restMoneyExchangeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyExchanges() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        // Get all the moneyExchangeList
        restMoneyExchangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyExchange.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].digit").value(hasItem(DEFAULT_DIGIT)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)));
    }

    @Test
    @Transactional
    void getMoneyExchange() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        // Get the moneyExchange
        restMoneyExchangeMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyExchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyExchange.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.digit").value(DEFAULT_DIGIT))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMoneyExchange() throws Exception {
        // Get the moneyExchange
        restMoneyExchangeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyExchange() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();

        // Update the moneyExchange
        MoneyExchange updatedMoneyExchange = moneyExchangeRepository.findById(moneyExchange.getId()).get();
        // Disconnect from session so that the updates on updatedMoneyExchange are not directly saved in db
        em.detach(updatedMoneyExchange);
        updatedMoneyExchange.name(UPDATED_NAME).digit(UPDATED_DIGIT).link(UPDATED_LINK).shortName(UPDATED_SHORT_NAME);
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(updatedMoneyExchange);

        restMoneyExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyExchangeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
        MoneyExchange testMoneyExchange = moneyExchangeList.get(moneyExchangeList.size() - 1);
        assertThat(testMoneyExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMoneyExchange.getDigit()).isEqualTo(UPDATED_DIGIT);
        assertThat(testMoneyExchange.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMoneyExchange.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyExchangeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyExchangeWithPatch() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();

        // Update the moneyExchange using partial update
        MoneyExchange partialUpdatedMoneyExchange = new MoneyExchange();
        partialUpdatedMoneyExchange.setId(moneyExchange.getId());

        partialUpdatedMoneyExchange.name(UPDATED_NAME);

        restMoneyExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyExchange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyExchange))
            )
            .andExpect(status().isOk());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
        MoneyExchange testMoneyExchange = moneyExchangeList.get(moneyExchangeList.size() - 1);
        assertThat(testMoneyExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMoneyExchange.getDigit()).isEqualTo(DEFAULT_DIGIT);
        assertThat(testMoneyExchange.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMoneyExchange.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMoneyExchangeWithPatch() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();

        // Update the moneyExchange using partial update
        MoneyExchange partialUpdatedMoneyExchange = new MoneyExchange();
        partialUpdatedMoneyExchange.setId(moneyExchange.getId());

        partialUpdatedMoneyExchange.name(UPDATED_NAME).digit(UPDATED_DIGIT).link(UPDATED_LINK).shortName(UPDATED_SHORT_NAME);

        restMoneyExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyExchange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyExchange))
            )
            .andExpect(status().isOk());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
        MoneyExchange testMoneyExchange = moneyExchangeList.get(moneyExchangeList.size() - 1);
        assertThat(testMoneyExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMoneyExchange.getDigit()).isEqualTo(UPDATED_DIGIT);
        assertThat(testMoneyExchange.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMoneyExchange.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyExchangeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyExchange() throws Exception {
        int databaseSizeBeforeUpdate = moneyExchangeRepository.findAll().size();
        moneyExchange.setId(count.incrementAndGet());

        // Create the MoneyExchange
        MoneyExchangeDTO moneyExchangeDTO = moneyExchangeMapper.toDto(moneyExchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyExchangeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyExchange in the database
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyExchange() throws Exception {
        // Initialize the database
        moneyExchangeRepository.saveAndFlush(moneyExchange);

        int databaseSizeBeforeDelete = moneyExchangeRepository.findAll().size();

        // Delete the moneyExchange
        restMoneyExchangeMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyExchange.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MoneyExchange> moneyExchangeList = moneyExchangeRepository.findAll();
        assertThat(moneyExchangeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
