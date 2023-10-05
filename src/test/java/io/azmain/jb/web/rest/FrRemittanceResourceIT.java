package io.azmain.jb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.FrRemittance;
import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.domain.enumeration.DocumentType;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.TransactionType;
import io.azmain.jb.repository.FrRemittanceRepository;
import io.azmain.jb.service.criteria.FrRemittanceCriteria;
import io.azmain.jb.service.dto.FrRemittanceDTO;
import io.azmain.jb.service.mapper.FrRemittanceMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FrRemittanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FrRemittanceResourceIT {

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_REMITERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REMITERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_INCENTIVE_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_INCENTIVE_AMOUNT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_INC_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INC_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INC_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_REMI_SENDING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMI_SENDING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMI_SENDING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REMI_FR_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_REMI_FR_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_EXCHANGE_RATE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE_RATE = "BBBBBBBBBB";

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.SPOT;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.ACCOUNT;

    private static final String DEFAULT_RECV_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_RECV_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_RECV_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECV_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECV_LEGAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECV_LEGAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MONEY_EXCHANGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MONEY_EXCHANGE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_AMOUNT_REIM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_AMOUNT_REIM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_AMOUNT_REIM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_INC_AMOUNT_REIM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INC_AMOUNT_REIM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INC_AMOUNT_REIM_DATE = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_RECV_GENDER = Gender.MALE;
    private static final Gender UPDATED_RECV_GENDER = Gender.FEMALE;

    private static final Gender DEFAULT_REMI_GENDER = Gender.MALE;
    private static final Gender UPDATED_REMI_GENDER = Gender.FEMALE;

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.NID;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.PASSPORT;

    private static final String ENTITY_API_URL = "/api/fr-remittances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrRemittanceRepository frRemittanceRepository;

    @Autowired
    private FrRemittanceMapper frRemittanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrRemittanceMockMvc;

    private FrRemittance frRemittance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrRemittance createEntity(EntityManager em) {
        FrRemittance frRemittance = new FrRemittance()
            .pin(DEFAULT_PIN)
            .remitersName(DEFAULT_REMITERS_NAME)
            .amount(DEFAULT_AMOUNT)
            .incentiveAmount(DEFAULT_INCENTIVE_AMOUNT)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .incPaymentDate(DEFAULT_INC_PAYMENT_DATE)
            .remiSendingDate(DEFAULT_REMI_SENDING_DATE)
            .remiFrCurrency(DEFAULT_REMI_FR_CURRENCY)
            .currency(DEFAULT_CURRENCY)
            .country(DEFAULT_COUNTRY)
            .exchangeRate(DEFAULT_EXCHANGE_RATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .recvMobileNo(DEFAULT_RECV_MOBILE_NO)
            .recvName(DEFAULT_RECV_NAME)
            .recvLegalId(DEFAULT_RECV_LEGAL_ID)
            .moneyExchangeName(DEFAULT_MONEY_EXCHANGE_NAME)
            .amountReimDate(DEFAULT_AMOUNT_REIM_DATE)
            .incAmountReimDate(DEFAULT_INC_AMOUNT_REIM_DATE)
            .recvGender(DEFAULT_RECV_GENDER)
            .remiGender(DEFAULT_REMI_GENDER)
            .documentType(DEFAULT_DOCUMENT_TYPE);
        return frRemittance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrRemittance createUpdatedEntity(EntityManager em) {
        FrRemittance frRemittance = new FrRemittance()
            .pin(UPDATED_PIN)
            .remitersName(UPDATED_REMITERS_NAME)
            .amount(UPDATED_AMOUNT)
            .incentiveAmount(UPDATED_INCENTIVE_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .incPaymentDate(UPDATED_INC_PAYMENT_DATE)
            .remiSendingDate(UPDATED_REMI_SENDING_DATE)
            .remiFrCurrency(UPDATED_REMI_FR_CURRENCY)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .recvMobileNo(UPDATED_RECV_MOBILE_NO)
            .recvName(UPDATED_RECV_NAME)
            .recvLegalId(UPDATED_RECV_LEGAL_ID)
            .moneyExchangeName(UPDATED_MONEY_EXCHANGE_NAME)
            .amountReimDate(UPDATED_AMOUNT_REIM_DATE)
            .incAmountReimDate(UPDATED_INC_AMOUNT_REIM_DATE)
            .recvGender(UPDATED_RECV_GENDER)
            .remiGender(UPDATED_REMI_GENDER)
            .documentType(UPDATED_DOCUMENT_TYPE);
        return frRemittance;
    }

    @BeforeEach
    public void initTest() {
        frRemittance = createEntity(em);
    }

    @Test
    @Transactional
    void createFrRemittance() throws Exception {
        int databaseSizeBeforeCreate = frRemittanceRepository.findAll().size();
        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);
        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeCreate + 1);
        FrRemittance testFrRemittance = frRemittanceList.get(frRemittanceList.size() - 1);
        assertThat(testFrRemittance.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testFrRemittance.getRemitersName()).isEqualTo(DEFAULT_REMITERS_NAME);
        assertThat(testFrRemittance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testFrRemittance.getIncentiveAmount()).isEqualTo(DEFAULT_INCENTIVE_AMOUNT);
        assertThat(testFrRemittance.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testFrRemittance.getIncPaymentDate()).isEqualTo(DEFAULT_INC_PAYMENT_DATE);
        assertThat(testFrRemittance.getRemiSendingDate()).isEqualTo(DEFAULT_REMI_SENDING_DATE);
        assertThat(testFrRemittance.getRemiFrCurrency()).isEqualTo(DEFAULT_REMI_FR_CURRENCY);
        assertThat(testFrRemittance.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testFrRemittance.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testFrRemittance.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
        assertThat(testFrRemittance.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testFrRemittance.getRecvMobileNo()).isEqualTo(DEFAULT_RECV_MOBILE_NO);
        assertThat(testFrRemittance.getRecvName()).isEqualTo(DEFAULT_RECV_NAME);
        assertThat(testFrRemittance.getRecvLegalId()).isEqualTo(DEFAULT_RECV_LEGAL_ID);
        assertThat(testFrRemittance.getMoneyExchangeName()).isEqualTo(DEFAULT_MONEY_EXCHANGE_NAME);
        assertThat(testFrRemittance.getAmountReimDate()).isEqualTo(DEFAULT_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getIncAmountReimDate()).isEqualTo(DEFAULT_INC_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getRecvGender()).isEqualTo(DEFAULT_RECV_GENDER);
        assertThat(testFrRemittance.getRemiGender()).isEqualTo(DEFAULT_REMI_GENDER);
        assertThat(testFrRemittance.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void createFrRemittanceWithExistingId() throws Exception {
        // Create the FrRemittance with an existing ID
        frRemittance.setId(1L);
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        int databaseSizeBeforeCreate = frRemittanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPinIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setPin(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemitersNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRemitersName(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setAmount(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncentiveAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setIncentiveAmount(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setPaymentDate(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncPaymentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setIncPaymentDate(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setTransactionType(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecvMobileNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRecvMobileNo(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecvNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRecvName(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecvLegalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRecvLegalId(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecvGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRecvGender(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemiGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setRemiGender(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frRemittanceRepository.findAll().size();
        // set the field null
        frRemittance.setDocumentType(null);

        // Create the FrRemittance, which fails.
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        restFrRemittanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFrRemittances() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frRemittance.getId().intValue())))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].remitersName").value(hasItem(DEFAULT_REMITERS_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].incentiveAmount").value(hasItem(DEFAULT_INCENTIVE_AMOUNT)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].incPaymentDate").value(hasItem(DEFAULT_INC_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].remiSendingDate").value(hasItem(DEFAULT_REMI_SENDING_DATE.toString())))
            .andExpect(jsonPath("$.[*].remiFrCurrency").value(hasItem(DEFAULT_REMI_FR_CURRENCY)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(DEFAULT_EXCHANGE_RATE)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recvMobileNo").value(hasItem(DEFAULT_RECV_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].recvName").value(hasItem(DEFAULT_RECV_NAME)))
            .andExpect(jsonPath("$.[*].recvLegalId").value(hasItem(DEFAULT_RECV_LEGAL_ID)))
            .andExpect(jsonPath("$.[*].moneyExchangeName").value(hasItem(DEFAULT_MONEY_EXCHANGE_NAME)))
            .andExpect(jsonPath("$.[*].amountReimDate").value(hasItem(DEFAULT_AMOUNT_REIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].incAmountReimDate").value(hasItem(DEFAULT_INC_AMOUNT_REIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].recvGender").value(hasItem(DEFAULT_RECV_GENDER.toString())))
            .andExpect(jsonPath("$.[*].remiGender").value(hasItem(DEFAULT_REMI_GENDER.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getFrRemittance() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get the frRemittance
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL_ID, frRemittance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frRemittance.getId().intValue()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.remitersName").value(DEFAULT_REMITERS_NAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.incentiveAmount").value(DEFAULT_INCENTIVE_AMOUNT))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.incPaymentDate").value(DEFAULT_INC_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.remiSendingDate").value(DEFAULT_REMI_SENDING_DATE.toString()))
            .andExpect(jsonPath("$.remiFrCurrency").value(DEFAULT_REMI_FR_CURRENCY))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.exchangeRate").value(DEFAULT_EXCHANGE_RATE))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.recvMobileNo").value(DEFAULT_RECV_MOBILE_NO))
            .andExpect(jsonPath("$.recvName").value(DEFAULT_RECV_NAME))
            .andExpect(jsonPath("$.recvLegalId").value(DEFAULT_RECV_LEGAL_ID))
            .andExpect(jsonPath("$.moneyExchangeName").value(DEFAULT_MONEY_EXCHANGE_NAME))
            .andExpect(jsonPath("$.amountReimDate").value(DEFAULT_AMOUNT_REIM_DATE.toString()))
            .andExpect(jsonPath("$.incAmountReimDate").value(DEFAULT_INC_AMOUNT_REIM_DATE.toString()))
            .andExpect(jsonPath("$.recvGender").value(DEFAULT_RECV_GENDER.toString()))
            .andExpect(jsonPath("$.remiGender").value(DEFAULT_REMI_GENDER.toString()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getFrRemittancesByIdFiltering() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        Long id = frRemittance.getId();

        defaultFrRemittanceShouldBeFound("id.equals=" + id);
        defaultFrRemittanceShouldNotBeFound("id.notEquals=" + id);

        defaultFrRemittanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFrRemittanceShouldNotBeFound("id.greaterThan=" + id);

        defaultFrRemittanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFrRemittanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPinIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where pin equals to DEFAULT_PIN
        defaultFrRemittanceShouldBeFound("pin.equals=" + DEFAULT_PIN);

        // Get all the frRemittanceList where pin equals to UPDATED_PIN
        defaultFrRemittanceShouldNotBeFound("pin.equals=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPinIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where pin in DEFAULT_PIN or UPDATED_PIN
        defaultFrRemittanceShouldBeFound("pin.in=" + DEFAULT_PIN + "," + UPDATED_PIN);

        // Get all the frRemittanceList where pin equals to UPDATED_PIN
        defaultFrRemittanceShouldNotBeFound("pin.in=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPinIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where pin is not null
        defaultFrRemittanceShouldBeFound("pin.specified=true");

        // Get all the frRemittanceList where pin is null
        defaultFrRemittanceShouldNotBeFound("pin.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPinContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where pin contains DEFAULT_PIN
        defaultFrRemittanceShouldBeFound("pin.contains=" + DEFAULT_PIN);

        // Get all the frRemittanceList where pin contains UPDATED_PIN
        defaultFrRemittanceShouldNotBeFound("pin.contains=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPinNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where pin does not contain DEFAULT_PIN
        defaultFrRemittanceShouldNotBeFound("pin.doesNotContain=" + DEFAULT_PIN);

        // Get all the frRemittanceList where pin does not contain UPDATED_PIN
        defaultFrRemittanceShouldBeFound("pin.doesNotContain=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemitersNameIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remitersName equals to DEFAULT_REMITERS_NAME
        defaultFrRemittanceShouldBeFound("remitersName.equals=" + DEFAULT_REMITERS_NAME);

        // Get all the frRemittanceList where remitersName equals to UPDATED_REMITERS_NAME
        defaultFrRemittanceShouldNotBeFound("remitersName.equals=" + UPDATED_REMITERS_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemitersNameIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remitersName in DEFAULT_REMITERS_NAME or UPDATED_REMITERS_NAME
        defaultFrRemittanceShouldBeFound("remitersName.in=" + DEFAULT_REMITERS_NAME + "," + UPDATED_REMITERS_NAME);

        // Get all the frRemittanceList where remitersName equals to UPDATED_REMITERS_NAME
        defaultFrRemittanceShouldNotBeFound("remitersName.in=" + UPDATED_REMITERS_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemitersNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remitersName is not null
        defaultFrRemittanceShouldBeFound("remitersName.specified=true");

        // Get all the frRemittanceList where remitersName is null
        defaultFrRemittanceShouldNotBeFound("remitersName.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemitersNameContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remitersName contains DEFAULT_REMITERS_NAME
        defaultFrRemittanceShouldBeFound("remitersName.contains=" + DEFAULT_REMITERS_NAME);

        // Get all the frRemittanceList where remitersName contains UPDATED_REMITERS_NAME
        defaultFrRemittanceShouldNotBeFound("remitersName.contains=" + UPDATED_REMITERS_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemitersNameNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remitersName does not contain DEFAULT_REMITERS_NAME
        defaultFrRemittanceShouldNotBeFound("remitersName.doesNotContain=" + DEFAULT_REMITERS_NAME);

        // Get all the frRemittanceList where remitersName does not contain UPDATED_REMITERS_NAME
        defaultFrRemittanceShouldBeFound("remitersName.doesNotContain=" + UPDATED_REMITERS_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amount equals to DEFAULT_AMOUNT
        defaultFrRemittanceShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the frRemittanceList where amount equals to UPDATED_AMOUNT
        defaultFrRemittanceShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultFrRemittanceShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the frRemittanceList where amount equals to UPDATED_AMOUNT
        defaultFrRemittanceShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amount is not null
        defaultFrRemittanceShouldBeFound("amount.specified=true");

        // Get all the frRemittanceList where amount is null
        defaultFrRemittanceShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amount contains DEFAULT_AMOUNT
        defaultFrRemittanceShouldBeFound("amount.contains=" + DEFAULT_AMOUNT);

        // Get all the frRemittanceList where amount contains UPDATED_AMOUNT
        defaultFrRemittanceShouldNotBeFound("amount.contains=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amount does not contain DEFAULT_AMOUNT
        defaultFrRemittanceShouldNotBeFound("amount.doesNotContain=" + DEFAULT_AMOUNT);

        // Get all the frRemittanceList where amount does not contain UPDATED_AMOUNT
        defaultFrRemittanceShouldBeFound("amount.doesNotContain=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncentiveAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incentiveAmount equals to DEFAULT_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldBeFound("incentiveAmount.equals=" + DEFAULT_INCENTIVE_AMOUNT);

        // Get all the frRemittanceList where incentiveAmount equals to UPDATED_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldNotBeFound("incentiveAmount.equals=" + UPDATED_INCENTIVE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncentiveAmountIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incentiveAmount in DEFAULT_INCENTIVE_AMOUNT or UPDATED_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldBeFound("incentiveAmount.in=" + DEFAULT_INCENTIVE_AMOUNT + "," + UPDATED_INCENTIVE_AMOUNT);

        // Get all the frRemittanceList where incentiveAmount equals to UPDATED_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldNotBeFound("incentiveAmount.in=" + UPDATED_INCENTIVE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncentiveAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incentiveAmount is not null
        defaultFrRemittanceShouldBeFound("incentiveAmount.specified=true");

        // Get all the frRemittanceList where incentiveAmount is null
        defaultFrRemittanceShouldNotBeFound("incentiveAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncentiveAmountContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incentiveAmount contains DEFAULT_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldBeFound("incentiveAmount.contains=" + DEFAULT_INCENTIVE_AMOUNT);

        // Get all the frRemittanceList where incentiveAmount contains UPDATED_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldNotBeFound("incentiveAmount.contains=" + UPDATED_INCENTIVE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncentiveAmountNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incentiveAmount does not contain DEFAULT_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldNotBeFound("incentiveAmount.doesNotContain=" + DEFAULT_INCENTIVE_AMOUNT);

        // Get all the frRemittanceList where incentiveAmount does not contain UPDATED_INCENTIVE_AMOUNT
        defaultFrRemittanceShouldBeFound("incentiveAmount.doesNotContain=" + UPDATED_INCENTIVE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate is not null
        defaultFrRemittanceShouldBeFound("paymentDate.specified=true");

        // Get all the frRemittanceList where paymentDate is null
        defaultFrRemittanceShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the frRemittanceList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate equals to DEFAULT_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.equals=" + DEFAULT_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate equals to UPDATED_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.equals=" + UPDATED_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate in DEFAULT_INC_PAYMENT_DATE or UPDATED_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.in=" + DEFAULT_INC_PAYMENT_DATE + "," + UPDATED_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate equals to UPDATED_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.in=" + UPDATED_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate is not null
        defaultFrRemittanceShouldBeFound("incPaymentDate.specified=true");

        // Get all the frRemittanceList where incPaymentDate is null
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate is greater than or equal to DEFAULT_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.greaterThanOrEqual=" + DEFAULT_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate is greater than or equal to UPDATED_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.greaterThanOrEqual=" + UPDATED_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate is less than or equal to DEFAULT_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.lessThanOrEqual=" + DEFAULT_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate is less than or equal to SMALLER_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.lessThanOrEqual=" + SMALLER_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate is less than DEFAULT_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.lessThan=" + DEFAULT_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate is less than UPDATED_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.lessThan=" + UPDATED_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incPaymentDate is greater than DEFAULT_INC_PAYMENT_DATE
        defaultFrRemittanceShouldNotBeFound("incPaymentDate.greaterThan=" + DEFAULT_INC_PAYMENT_DATE);

        // Get all the frRemittanceList where incPaymentDate is greater than SMALLER_INC_PAYMENT_DATE
        defaultFrRemittanceShouldBeFound("incPaymentDate.greaterThan=" + SMALLER_INC_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate equals to DEFAULT_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.equals=" + DEFAULT_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate equals to UPDATED_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.equals=" + UPDATED_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate in DEFAULT_REMI_SENDING_DATE or UPDATED_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.in=" + DEFAULT_REMI_SENDING_DATE + "," + UPDATED_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate equals to UPDATED_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.in=" + UPDATED_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate is not null
        defaultFrRemittanceShouldBeFound("remiSendingDate.specified=true");

        // Get all the frRemittanceList where remiSendingDate is null
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate is greater than or equal to DEFAULT_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.greaterThanOrEqual=" + DEFAULT_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate is greater than or equal to UPDATED_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.greaterThanOrEqual=" + UPDATED_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate is less than or equal to DEFAULT_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.lessThanOrEqual=" + DEFAULT_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate is less than or equal to SMALLER_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.lessThanOrEqual=" + SMALLER_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate is less than DEFAULT_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.lessThan=" + DEFAULT_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate is less than UPDATED_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.lessThan=" + UPDATED_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiSendingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiSendingDate is greater than DEFAULT_REMI_SENDING_DATE
        defaultFrRemittanceShouldNotBeFound("remiSendingDate.greaterThan=" + DEFAULT_REMI_SENDING_DATE);

        // Get all the frRemittanceList where remiSendingDate is greater than SMALLER_REMI_SENDING_DATE
        defaultFrRemittanceShouldBeFound("remiSendingDate.greaterThan=" + SMALLER_REMI_SENDING_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiFrCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiFrCurrency equals to DEFAULT_REMI_FR_CURRENCY
        defaultFrRemittanceShouldBeFound("remiFrCurrency.equals=" + DEFAULT_REMI_FR_CURRENCY);

        // Get all the frRemittanceList where remiFrCurrency equals to UPDATED_REMI_FR_CURRENCY
        defaultFrRemittanceShouldNotBeFound("remiFrCurrency.equals=" + UPDATED_REMI_FR_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiFrCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiFrCurrency in DEFAULT_REMI_FR_CURRENCY or UPDATED_REMI_FR_CURRENCY
        defaultFrRemittanceShouldBeFound("remiFrCurrency.in=" + DEFAULT_REMI_FR_CURRENCY + "," + UPDATED_REMI_FR_CURRENCY);

        // Get all the frRemittanceList where remiFrCurrency equals to UPDATED_REMI_FR_CURRENCY
        defaultFrRemittanceShouldNotBeFound("remiFrCurrency.in=" + UPDATED_REMI_FR_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiFrCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiFrCurrency is not null
        defaultFrRemittanceShouldBeFound("remiFrCurrency.specified=true");

        // Get all the frRemittanceList where remiFrCurrency is null
        defaultFrRemittanceShouldNotBeFound("remiFrCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiFrCurrencyContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiFrCurrency contains DEFAULT_REMI_FR_CURRENCY
        defaultFrRemittanceShouldBeFound("remiFrCurrency.contains=" + DEFAULT_REMI_FR_CURRENCY);

        // Get all the frRemittanceList where remiFrCurrency contains UPDATED_REMI_FR_CURRENCY
        defaultFrRemittanceShouldNotBeFound("remiFrCurrency.contains=" + UPDATED_REMI_FR_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiFrCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiFrCurrency does not contain DEFAULT_REMI_FR_CURRENCY
        defaultFrRemittanceShouldNotBeFound("remiFrCurrency.doesNotContain=" + DEFAULT_REMI_FR_CURRENCY);

        // Get all the frRemittanceList where remiFrCurrency does not contain UPDATED_REMI_FR_CURRENCY
        defaultFrRemittanceShouldBeFound("remiFrCurrency.doesNotContain=" + UPDATED_REMI_FR_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where currency equals to DEFAULT_CURRENCY
        defaultFrRemittanceShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the frRemittanceList where currency equals to UPDATED_CURRENCY
        defaultFrRemittanceShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultFrRemittanceShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the frRemittanceList where currency equals to UPDATED_CURRENCY
        defaultFrRemittanceShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where currency is not null
        defaultFrRemittanceShouldBeFound("currency.specified=true");

        // Get all the frRemittanceList where currency is null
        defaultFrRemittanceShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where currency contains DEFAULT_CURRENCY
        defaultFrRemittanceShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the frRemittanceList where currency contains UPDATED_CURRENCY
        defaultFrRemittanceShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where currency does not contain DEFAULT_CURRENCY
        defaultFrRemittanceShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the frRemittanceList where currency does not contain UPDATED_CURRENCY
        defaultFrRemittanceShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where country equals to DEFAULT_COUNTRY
        defaultFrRemittanceShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the frRemittanceList where country equals to UPDATED_COUNTRY
        defaultFrRemittanceShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultFrRemittanceShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the frRemittanceList where country equals to UPDATED_COUNTRY
        defaultFrRemittanceShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where country is not null
        defaultFrRemittanceShouldBeFound("country.specified=true");

        // Get all the frRemittanceList where country is null
        defaultFrRemittanceShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCountryContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where country contains DEFAULT_COUNTRY
        defaultFrRemittanceShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the frRemittanceList where country contains UPDATED_COUNTRY
        defaultFrRemittanceShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where country does not contain DEFAULT_COUNTRY
        defaultFrRemittanceShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the frRemittanceList where country does not contain UPDATED_COUNTRY
        defaultFrRemittanceShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByExchangeRateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where exchangeRate equals to DEFAULT_EXCHANGE_RATE
        defaultFrRemittanceShouldBeFound("exchangeRate.equals=" + DEFAULT_EXCHANGE_RATE);

        // Get all the frRemittanceList where exchangeRate equals to UPDATED_EXCHANGE_RATE
        defaultFrRemittanceShouldNotBeFound("exchangeRate.equals=" + UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByExchangeRateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where exchangeRate in DEFAULT_EXCHANGE_RATE or UPDATED_EXCHANGE_RATE
        defaultFrRemittanceShouldBeFound("exchangeRate.in=" + DEFAULT_EXCHANGE_RATE + "," + UPDATED_EXCHANGE_RATE);

        // Get all the frRemittanceList where exchangeRate equals to UPDATED_EXCHANGE_RATE
        defaultFrRemittanceShouldNotBeFound("exchangeRate.in=" + UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByExchangeRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where exchangeRate is not null
        defaultFrRemittanceShouldBeFound("exchangeRate.specified=true");

        // Get all the frRemittanceList where exchangeRate is null
        defaultFrRemittanceShouldNotBeFound("exchangeRate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByExchangeRateContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where exchangeRate contains DEFAULT_EXCHANGE_RATE
        defaultFrRemittanceShouldBeFound("exchangeRate.contains=" + DEFAULT_EXCHANGE_RATE);

        // Get all the frRemittanceList where exchangeRate contains UPDATED_EXCHANGE_RATE
        defaultFrRemittanceShouldNotBeFound("exchangeRate.contains=" + UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByExchangeRateNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where exchangeRate does not contain DEFAULT_EXCHANGE_RATE
        defaultFrRemittanceShouldNotBeFound("exchangeRate.doesNotContain=" + DEFAULT_EXCHANGE_RATE);

        // Get all the frRemittanceList where exchangeRate does not contain UPDATED_EXCHANGE_RATE
        defaultFrRemittanceShouldBeFound("exchangeRate.doesNotContain=" + UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultFrRemittanceShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the frRemittanceList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultFrRemittanceShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultFrRemittanceShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the frRemittanceList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultFrRemittanceShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where transactionType is not null
        defaultFrRemittanceShouldBeFound("transactionType.specified=true");

        // Get all the frRemittanceList where transactionType is null
        defaultFrRemittanceShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvMobileNoIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvMobileNo equals to DEFAULT_RECV_MOBILE_NO
        defaultFrRemittanceShouldBeFound("recvMobileNo.equals=" + DEFAULT_RECV_MOBILE_NO);

        // Get all the frRemittanceList where recvMobileNo equals to UPDATED_RECV_MOBILE_NO
        defaultFrRemittanceShouldNotBeFound("recvMobileNo.equals=" + UPDATED_RECV_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvMobileNoIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvMobileNo in DEFAULT_RECV_MOBILE_NO or UPDATED_RECV_MOBILE_NO
        defaultFrRemittanceShouldBeFound("recvMobileNo.in=" + DEFAULT_RECV_MOBILE_NO + "," + UPDATED_RECV_MOBILE_NO);

        // Get all the frRemittanceList where recvMobileNo equals to UPDATED_RECV_MOBILE_NO
        defaultFrRemittanceShouldNotBeFound("recvMobileNo.in=" + UPDATED_RECV_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvMobileNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvMobileNo is not null
        defaultFrRemittanceShouldBeFound("recvMobileNo.specified=true");

        // Get all the frRemittanceList where recvMobileNo is null
        defaultFrRemittanceShouldNotBeFound("recvMobileNo.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvMobileNoContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvMobileNo contains DEFAULT_RECV_MOBILE_NO
        defaultFrRemittanceShouldBeFound("recvMobileNo.contains=" + DEFAULT_RECV_MOBILE_NO);

        // Get all the frRemittanceList where recvMobileNo contains UPDATED_RECV_MOBILE_NO
        defaultFrRemittanceShouldNotBeFound("recvMobileNo.contains=" + UPDATED_RECV_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvMobileNoNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvMobileNo does not contain DEFAULT_RECV_MOBILE_NO
        defaultFrRemittanceShouldNotBeFound("recvMobileNo.doesNotContain=" + DEFAULT_RECV_MOBILE_NO);

        // Get all the frRemittanceList where recvMobileNo does not contain UPDATED_RECV_MOBILE_NO
        defaultFrRemittanceShouldBeFound("recvMobileNo.doesNotContain=" + UPDATED_RECV_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvNameIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvName equals to DEFAULT_RECV_NAME
        defaultFrRemittanceShouldBeFound("recvName.equals=" + DEFAULT_RECV_NAME);

        // Get all the frRemittanceList where recvName equals to UPDATED_RECV_NAME
        defaultFrRemittanceShouldNotBeFound("recvName.equals=" + UPDATED_RECV_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvNameIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvName in DEFAULT_RECV_NAME or UPDATED_RECV_NAME
        defaultFrRemittanceShouldBeFound("recvName.in=" + DEFAULT_RECV_NAME + "," + UPDATED_RECV_NAME);

        // Get all the frRemittanceList where recvName equals to UPDATED_RECV_NAME
        defaultFrRemittanceShouldNotBeFound("recvName.in=" + UPDATED_RECV_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvName is not null
        defaultFrRemittanceShouldBeFound("recvName.specified=true");

        // Get all the frRemittanceList where recvName is null
        defaultFrRemittanceShouldNotBeFound("recvName.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvNameContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvName contains DEFAULT_RECV_NAME
        defaultFrRemittanceShouldBeFound("recvName.contains=" + DEFAULT_RECV_NAME);

        // Get all the frRemittanceList where recvName contains UPDATED_RECV_NAME
        defaultFrRemittanceShouldNotBeFound("recvName.contains=" + UPDATED_RECV_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvNameNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvName does not contain DEFAULT_RECV_NAME
        defaultFrRemittanceShouldNotBeFound("recvName.doesNotContain=" + DEFAULT_RECV_NAME);

        // Get all the frRemittanceList where recvName does not contain UPDATED_RECV_NAME
        defaultFrRemittanceShouldBeFound("recvName.doesNotContain=" + UPDATED_RECV_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvLegalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvLegalId equals to DEFAULT_RECV_LEGAL_ID
        defaultFrRemittanceShouldBeFound("recvLegalId.equals=" + DEFAULT_RECV_LEGAL_ID);

        // Get all the frRemittanceList where recvLegalId equals to UPDATED_RECV_LEGAL_ID
        defaultFrRemittanceShouldNotBeFound("recvLegalId.equals=" + UPDATED_RECV_LEGAL_ID);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvLegalIdIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvLegalId in DEFAULT_RECV_LEGAL_ID or UPDATED_RECV_LEGAL_ID
        defaultFrRemittanceShouldBeFound("recvLegalId.in=" + DEFAULT_RECV_LEGAL_ID + "," + UPDATED_RECV_LEGAL_ID);

        // Get all the frRemittanceList where recvLegalId equals to UPDATED_RECV_LEGAL_ID
        defaultFrRemittanceShouldNotBeFound("recvLegalId.in=" + UPDATED_RECV_LEGAL_ID);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvLegalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvLegalId is not null
        defaultFrRemittanceShouldBeFound("recvLegalId.specified=true");

        // Get all the frRemittanceList where recvLegalId is null
        defaultFrRemittanceShouldNotBeFound("recvLegalId.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvLegalIdContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvLegalId contains DEFAULT_RECV_LEGAL_ID
        defaultFrRemittanceShouldBeFound("recvLegalId.contains=" + DEFAULT_RECV_LEGAL_ID);

        // Get all the frRemittanceList where recvLegalId contains UPDATED_RECV_LEGAL_ID
        defaultFrRemittanceShouldNotBeFound("recvLegalId.contains=" + UPDATED_RECV_LEGAL_ID);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvLegalIdNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvLegalId does not contain DEFAULT_RECV_LEGAL_ID
        defaultFrRemittanceShouldNotBeFound("recvLegalId.doesNotContain=" + DEFAULT_RECV_LEGAL_ID);

        // Get all the frRemittanceList where recvLegalId does not contain UPDATED_RECV_LEGAL_ID
        defaultFrRemittanceShouldBeFound("recvLegalId.doesNotContain=" + UPDATED_RECV_LEGAL_ID);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where moneyExchangeName equals to DEFAULT_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldBeFound("moneyExchangeName.equals=" + DEFAULT_MONEY_EXCHANGE_NAME);

        // Get all the frRemittanceList where moneyExchangeName equals to UPDATED_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldNotBeFound("moneyExchangeName.equals=" + UPDATED_MONEY_EXCHANGE_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeNameIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where moneyExchangeName in DEFAULT_MONEY_EXCHANGE_NAME or UPDATED_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldBeFound("moneyExchangeName.in=" + DEFAULT_MONEY_EXCHANGE_NAME + "," + UPDATED_MONEY_EXCHANGE_NAME);

        // Get all the frRemittanceList where moneyExchangeName equals to UPDATED_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldNotBeFound("moneyExchangeName.in=" + UPDATED_MONEY_EXCHANGE_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where moneyExchangeName is not null
        defaultFrRemittanceShouldBeFound("moneyExchangeName.specified=true");

        // Get all the frRemittanceList where moneyExchangeName is null
        defaultFrRemittanceShouldNotBeFound("moneyExchangeName.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeNameContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where moneyExchangeName contains DEFAULT_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldBeFound("moneyExchangeName.contains=" + DEFAULT_MONEY_EXCHANGE_NAME);

        // Get all the frRemittanceList where moneyExchangeName contains UPDATED_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldNotBeFound("moneyExchangeName.contains=" + UPDATED_MONEY_EXCHANGE_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeNameNotContainsSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where moneyExchangeName does not contain DEFAULT_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldNotBeFound("moneyExchangeName.doesNotContain=" + DEFAULT_MONEY_EXCHANGE_NAME);

        // Get all the frRemittanceList where moneyExchangeName does not contain UPDATED_MONEY_EXCHANGE_NAME
        defaultFrRemittanceShouldBeFound("moneyExchangeName.doesNotContain=" + UPDATED_MONEY_EXCHANGE_NAME);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate equals to DEFAULT_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.equals=" + DEFAULT_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate equals to UPDATED_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.equals=" + UPDATED_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate in DEFAULT_AMOUNT_REIM_DATE or UPDATED_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.in=" + DEFAULT_AMOUNT_REIM_DATE + "," + UPDATED_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate equals to UPDATED_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.in=" + UPDATED_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate is not null
        defaultFrRemittanceShouldBeFound("amountReimDate.specified=true");

        // Get all the frRemittanceList where amountReimDate is null
        defaultFrRemittanceShouldNotBeFound("amountReimDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate is greater than or equal to DEFAULT_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.greaterThanOrEqual=" + DEFAULT_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate is greater than or equal to UPDATED_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.greaterThanOrEqual=" + UPDATED_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate is less than or equal to DEFAULT_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.lessThanOrEqual=" + DEFAULT_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate is less than or equal to SMALLER_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.lessThanOrEqual=" + SMALLER_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsLessThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate is less than DEFAULT_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.lessThan=" + DEFAULT_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate is less than UPDATED_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.lessThan=" + UPDATED_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByAmountReimDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where amountReimDate is greater than DEFAULT_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("amountReimDate.greaterThan=" + DEFAULT_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where amountReimDate is greater than SMALLER_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("amountReimDate.greaterThan=" + SMALLER_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate equals to DEFAULT_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.equals=" + DEFAULT_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate equals to UPDATED_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.equals=" + UPDATED_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate in DEFAULT_INC_AMOUNT_REIM_DATE or UPDATED_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.in=" + DEFAULT_INC_AMOUNT_REIM_DATE + "," + UPDATED_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate equals to UPDATED_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.in=" + UPDATED_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate is not null
        defaultFrRemittanceShouldBeFound("incAmountReimDate.specified=true");

        // Get all the frRemittanceList where incAmountReimDate is null
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate is greater than or equal to DEFAULT_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.greaterThanOrEqual=" + DEFAULT_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate is greater than or equal to UPDATED_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.greaterThanOrEqual=" + UPDATED_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate is less than or equal to DEFAULT_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.lessThanOrEqual=" + DEFAULT_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate is less than or equal to SMALLER_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.lessThanOrEqual=" + SMALLER_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsLessThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate is less than DEFAULT_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.lessThan=" + DEFAULT_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate is less than UPDATED_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.lessThan=" + UPDATED_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncAmountReimDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where incAmountReimDate is greater than DEFAULT_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldNotBeFound("incAmountReimDate.greaterThan=" + DEFAULT_INC_AMOUNT_REIM_DATE);

        // Get all the frRemittanceList where incAmountReimDate is greater than SMALLER_INC_AMOUNT_REIM_DATE
        defaultFrRemittanceShouldBeFound("incAmountReimDate.greaterThan=" + SMALLER_INC_AMOUNT_REIM_DATE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvGender equals to DEFAULT_RECV_GENDER
        defaultFrRemittanceShouldBeFound("recvGender.equals=" + DEFAULT_RECV_GENDER);

        // Get all the frRemittanceList where recvGender equals to UPDATED_RECV_GENDER
        defaultFrRemittanceShouldNotBeFound("recvGender.equals=" + UPDATED_RECV_GENDER);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvGenderIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvGender in DEFAULT_RECV_GENDER or UPDATED_RECV_GENDER
        defaultFrRemittanceShouldBeFound("recvGender.in=" + DEFAULT_RECV_GENDER + "," + UPDATED_RECV_GENDER);

        // Get all the frRemittanceList where recvGender equals to UPDATED_RECV_GENDER
        defaultFrRemittanceShouldNotBeFound("recvGender.in=" + UPDATED_RECV_GENDER);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRecvGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where recvGender is not null
        defaultFrRemittanceShouldBeFound("recvGender.specified=true");

        // Get all the frRemittanceList where recvGender is null
        defaultFrRemittanceShouldNotBeFound("recvGender.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiGender equals to DEFAULT_REMI_GENDER
        defaultFrRemittanceShouldBeFound("remiGender.equals=" + DEFAULT_REMI_GENDER);

        // Get all the frRemittanceList where remiGender equals to UPDATED_REMI_GENDER
        defaultFrRemittanceShouldNotBeFound("remiGender.equals=" + UPDATED_REMI_GENDER);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiGenderIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiGender in DEFAULT_REMI_GENDER or UPDATED_REMI_GENDER
        defaultFrRemittanceShouldBeFound("remiGender.in=" + DEFAULT_REMI_GENDER + "," + UPDATED_REMI_GENDER);

        // Get all the frRemittanceList where remiGender equals to UPDATED_REMI_GENDER
        defaultFrRemittanceShouldNotBeFound("remiGender.in=" + UPDATED_REMI_GENDER);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByRemiGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where remiGender is not null
        defaultFrRemittanceShouldBeFound("remiGender.specified=true");

        // Get all the frRemittanceList where remiGender is null
        defaultFrRemittanceShouldNotBeFound("remiGender.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where documentType equals to DEFAULT_DOCUMENT_TYPE
        defaultFrRemittanceShouldBeFound("documentType.equals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the frRemittanceList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultFrRemittanceShouldNotBeFound("documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where documentType in DEFAULT_DOCUMENT_TYPE or UPDATED_DOCUMENT_TYPE
        defaultFrRemittanceShouldBeFound("documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE);

        // Get all the frRemittanceList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultFrRemittanceShouldNotBeFound("documentType.in=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllFrRemittancesByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        // Get all the frRemittanceList where documentType is not null
        defaultFrRemittanceShouldBeFound("documentType.specified=true");

        // Get all the frRemittanceList where documentType is null
        defaultFrRemittanceShouldNotBeFound("documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllFrRemittancesByMoneyExchangeIsEqualToSomething() throws Exception {
        MoneyExchange moneyExchange;
        if (TestUtil.findAll(em, MoneyExchange.class).isEmpty()) {
            frRemittanceRepository.saveAndFlush(frRemittance);
            moneyExchange = MoneyExchangeResourceIT.createEntity(em);
        } else {
            moneyExchange = TestUtil.findAll(em, MoneyExchange.class).get(0);
        }
        em.persist(moneyExchange);
        em.flush();
        frRemittance.setMoneyExchange(moneyExchange);
        frRemittanceRepository.saveAndFlush(frRemittance);
        Long moneyExchangeId = moneyExchange.getId();

        // Get all the frRemittanceList where moneyExchange equals to moneyExchangeId
        defaultFrRemittanceShouldBeFound("moneyExchangeId.equals=" + moneyExchangeId);

        // Get all the frRemittanceList where moneyExchange equals to (moneyExchangeId + 1)
        defaultFrRemittanceShouldNotBeFound("moneyExchangeId.equals=" + (moneyExchangeId + 1));
    }

    @Test
    @Transactional
    void getAllFrRemittancesByIncPercentageIsEqualToSomething() throws Exception {
        IncPercentage incPercentage;
        if (TestUtil.findAll(em, IncPercentage.class).isEmpty()) {
            frRemittanceRepository.saveAndFlush(frRemittance);
            incPercentage = IncPercentageResourceIT.createEntity(em);
        } else {
            incPercentage = TestUtil.findAll(em, IncPercentage.class).get(0);
        }
        em.persist(incPercentage);
        em.flush();
        frRemittance.setIncPercentage(incPercentage);
        frRemittanceRepository.saveAndFlush(frRemittance);
        Long incPercentageId = incPercentage.getId();

        // Get all the frRemittanceList where incPercentage equals to incPercentageId
        defaultFrRemittanceShouldBeFound("incPercentageId.equals=" + incPercentageId);

        // Get all the frRemittanceList where incPercentage equals to (incPercentageId + 1)
        defaultFrRemittanceShouldNotBeFound("incPercentageId.equals=" + (incPercentageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFrRemittanceShouldBeFound(String filter) throws Exception {
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frRemittance.getId().intValue())))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].remitersName").value(hasItem(DEFAULT_REMITERS_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].incentiveAmount").value(hasItem(DEFAULT_INCENTIVE_AMOUNT)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].incPaymentDate").value(hasItem(DEFAULT_INC_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].remiSendingDate").value(hasItem(DEFAULT_REMI_SENDING_DATE.toString())))
            .andExpect(jsonPath("$.[*].remiFrCurrency").value(hasItem(DEFAULT_REMI_FR_CURRENCY)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(DEFAULT_EXCHANGE_RATE)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recvMobileNo").value(hasItem(DEFAULT_RECV_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].recvName").value(hasItem(DEFAULT_RECV_NAME)))
            .andExpect(jsonPath("$.[*].recvLegalId").value(hasItem(DEFAULT_RECV_LEGAL_ID)))
            .andExpect(jsonPath("$.[*].moneyExchangeName").value(hasItem(DEFAULT_MONEY_EXCHANGE_NAME)))
            .andExpect(jsonPath("$.[*].amountReimDate").value(hasItem(DEFAULT_AMOUNT_REIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].incAmountReimDate").value(hasItem(DEFAULT_INC_AMOUNT_REIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].recvGender").value(hasItem(DEFAULT_RECV_GENDER.toString())))
            .andExpect(jsonPath("$.[*].remiGender").value(hasItem(DEFAULT_REMI_GENDER.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())));

        // Check, that the count call also returns 1
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFrRemittanceShouldNotBeFound(String filter) throws Exception {
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFrRemittanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFrRemittance() throws Exception {
        // Get the frRemittance
        restFrRemittanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFrRemittance() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();

        // Update the frRemittance
        FrRemittance updatedFrRemittance = frRemittanceRepository.findById(frRemittance.getId()).get();
        // Disconnect from session so that the updates on updatedFrRemittance are not directly saved in db
        em.detach(updatedFrRemittance);
        updatedFrRemittance
            .pin(UPDATED_PIN)
            .remitersName(UPDATED_REMITERS_NAME)
            .amount(UPDATED_AMOUNT)
            .incentiveAmount(UPDATED_INCENTIVE_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .incPaymentDate(UPDATED_INC_PAYMENT_DATE)
            .remiSendingDate(UPDATED_REMI_SENDING_DATE)
            .remiFrCurrency(UPDATED_REMI_FR_CURRENCY)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .recvMobileNo(UPDATED_RECV_MOBILE_NO)
            .recvName(UPDATED_RECV_NAME)
            .recvLegalId(UPDATED_RECV_LEGAL_ID)
            .moneyExchangeName(UPDATED_MONEY_EXCHANGE_NAME)
            .amountReimDate(UPDATED_AMOUNT_REIM_DATE)
            .incAmountReimDate(UPDATED_INC_AMOUNT_REIM_DATE)
            .recvGender(UPDATED_RECV_GENDER)
            .remiGender(UPDATED_REMI_GENDER)
            .documentType(UPDATED_DOCUMENT_TYPE);
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(updatedFrRemittance);

        restFrRemittanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frRemittanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
        FrRemittance testFrRemittance = frRemittanceList.get(frRemittanceList.size() - 1);
        assertThat(testFrRemittance.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testFrRemittance.getRemitersName()).isEqualTo(UPDATED_REMITERS_NAME);
        assertThat(testFrRemittance.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testFrRemittance.getIncentiveAmount()).isEqualTo(UPDATED_INCENTIVE_AMOUNT);
        assertThat(testFrRemittance.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testFrRemittance.getIncPaymentDate()).isEqualTo(UPDATED_INC_PAYMENT_DATE);
        assertThat(testFrRemittance.getRemiSendingDate()).isEqualTo(UPDATED_REMI_SENDING_DATE);
        assertThat(testFrRemittance.getRemiFrCurrency()).isEqualTo(UPDATED_REMI_FR_CURRENCY);
        assertThat(testFrRemittance.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testFrRemittance.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFrRemittance.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
        assertThat(testFrRemittance.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testFrRemittance.getRecvMobileNo()).isEqualTo(UPDATED_RECV_MOBILE_NO);
        assertThat(testFrRemittance.getRecvName()).isEqualTo(UPDATED_RECV_NAME);
        assertThat(testFrRemittance.getRecvLegalId()).isEqualTo(UPDATED_RECV_LEGAL_ID);
        assertThat(testFrRemittance.getMoneyExchangeName()).isEqualTo(UPDATED_MONEY_EXCHANGE_NAME);
        assertThat(testFrRemittance.getAmountReimDate()).isEqualTo(UPDATED_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getIncAmountReimDate()).isEqualTo(UPDATED_INC_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getRecvGender()).isEqualTo(UPDATED_RECV_GENDER);
        assertThat(testFrRemittance.getRemiGender()).isEqualTo(UPDATED_REMI_GENDER);
        assertThat(testFrRemittance.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frRemittanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFrRemittanceWithPatch() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();

        // Update the frRemittance using partial update
        FrRemittance partialUpdatedFrRemittance = new FrRemittance();
        partialUpdatedFrRemittance.setId(frRemittance.getId());

        partialUpdatedFrRemittance
            .pin(UPDATED_PIN)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .incPaymentDate(UPDATED_INC_PAYMENT_DATE)
            .remiFrCurrency(UPDATED_REMI_FR_CURRENCY)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .recvMobileNo(UPDATED_RECV_MOBILE_NO)
            .recvName(UPDATED_RECV_NAME)
            .recvLegalId(UPDATED_RECV_LEGAL_ID)
            .amountReimDate(UPDATED_AMOUNT_REIM_DATE)
            .recvGender(UPDATED_RECV_GENDER)
            .remiGender(UPDATED_REMI_GENDER)
            .documentType(UPDATED_DOCUMENT_TYPE);

        restFrRemittanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrRemittance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrRemittance))
            )
            .andExpect(status().isOk());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
        FrRemittance testFrRemittance = frRemittanceList.get(frRemittanceList.size() - 1);
        assertThat(testFrRemittance.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testFrRemittance.getRemitersName()).isEqualTo(DEFAULT_REMITERS_NAME);
        assertThat(testFrRemittance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testFrRemittance.getIncentiveAmount()).isEqualTo(DEFAULT_INCENTIVE_AMOUNT);
        assertThat(testFrRemittance.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testFrRemittance.getIncPaymentDate()).isEqualTo(UPDATED_INC_PAYMENT_DATE);
        assertThat(testFrRemittance.getRemiSendingDate()).isEqualTo(DEFAULT_REMI_SENDING_DATE);
        assertThat(testFrRemittance.getRemiFrCurrency()).isEqualTo(UPDATED_REMI_FR_CURRENCY);
        assertThat(testFrRemittance.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testFrRemittance.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFrRemittance.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
        assertThat(testFrRemittance.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testFrRemittance.getRecvMobileNo()).isEqualTo(UPDATED_RECV_MOBILE_NO);
        assertThat(testFrRemittance.getRecvName()).isEqualTo(UPDATED_RECV_NAME);
        assertThat(testFrRemittance.getRecvLegalId()).isEqualTo(UPDATED_RECV_LEGAL_ID);
        assertThat(testFrRemittance.getMoneyExchangeName()).isEqualTo(DEFAULT_MONEY_EXCHANGE_NAME);
        assertThat(testFrRemittance.getAmountReimDate()).isEqualTo(UPDATED_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getIncAmountReimDate()).isEqualTo(DEFAULT_INC_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getRecvGender()).isEqualTo(UPDATED_RECV_GENDER);
        assertThat(testFrRemittance.getRemiGender()).isEqualTo(UPDATED_REMI_GENDER);
        assertThat(testFrRemittance.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFrRemittanceWithPatch() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();

        // Update the frRemittance using partial update
        FrRemittance partialUpdatedFrRemittance = new FrRemittance();
        partialUpdatedFrRemittance.setId(frRemittance.getId());

        partialUpdatedFrRemittance
            .pin(UPDATED_PIN)
            .remitersName(UPDATED_REMITERS_NAME)
            .amount(UPDATED_AMOUNT)
            .incentiveAmount(UPDATED_INCENTIVE_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .incPaymentDate(UPDATED_INC_PAYMENT_DATE)
            .remiSendingDate(UPDATED_REMI_SENDING_DATE)
            .remiFrCurrency(UPDATED_REMI_FR_CURRENCY)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .recvMobileNo(UPDATED_RECV_MOBILE_NO)
            .recvName(UPDATED_RECV_NAME)
            .recvLegalId(UPDATED_RECV_LEGAL_ID)
            .moneyExchangeName(UPDATED_MONEY_EXCHANGE_NAME)
            .amountReimDate(UPDATED_AMOUNT_REIM_DATE)
            .incAmountReimDate(UPDATED_INC_AMOUNT_REIM_DATE)
            .recvGender(UPDATED_RECV_GENDER)
            .remiGender(UPDATED_REMI_GENDER)
            .documentType(UPDATED_DOCUMENT_TYPE);

        restFrRemittanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrRemittance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrRemittance))
            )
            .andExpect(status().isOk());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
        FrRemittance testFrRemittance = frRemittanceList.get(frRemittanceList.size() - 1);
        assertThat(testFrRemittance.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testFrRemittance.getRemitersName()).isEqualTo(UPDATED_REMITERS_NAME);
        assertThat(testFrRemittance.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testFrRemittance.getIncentiveAmount()).isEqualTo(UPDATED_INCENTIVE_AMOUNT);
        assertThat(testFrRemittance.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testFrRemittance.getIncPaymentDate()).isEqualTo(UPDATED_INC_PAYMENT_DATE);
        assertThat(testFrRemittance.getRemiSendingDate()).isEqualTo(UPDATED_REMI_SENDING_DATE);
        assertThat(testFrRemittance.getRemiFrCurrency()).isEqualTo(UPDATED_REMI_FR_CURRENCY);
        assertThat(testFrRemittance.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testFrRemittance.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFrRemittance.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
        assertThat(testFrRemittance.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testFrRemittance.getRecvMobileNo()).isEqualTo(UPDATED_RECV_MOBILE_NO);
        assertThat(testFrRemittance.getRecvName()).isEqualTo(UPDATED_RECV_NAME);
        assertThat(testFrRemittance.getRecvLegalId()).isEqualTo(UPDATED_RECV_LEGAL_ID);
        assertThat(testFrRemittance.getMoneyExchangeName()).isEqualTo(UPDATED_MONEY_EXCHANGE_NAME);
        assertThat(testFrRemittance.getAmountReimDate()).isEqualTo(UPDATED_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getIncAmountReimDate()).isEqualTo(UPDATED_INC_AMOUNT_REIM_DATE);
        assertThat(testFrRemittance.getRecvGender()).isEqualTo(UPDATED_RECV_GENDER);
        assertThat(testFrRemittance.getRemiGender()).isEqualTo(UPDATED_REMI_GENDER);
        assertThat(testFrRemittance.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frRemittanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrRemittance() throws Exception {
        int databaseSizeBeforeUpdate = frRemittanceRepository.findAll().size();
        frRemittance.setId(count.incrementAndGet());

        // Create the FrRemittance
        FrRemittanceDTO frRemittanceDTO = frRemittanceMapper.toDto(frRemittance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrRemittanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frRemittanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrRemittance in the database
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrRemittance() throws Exception {
        // Initialize the database
        frRemittanceRepository.saveAndFlush(frRemittance);

        int databaseSizeBeforeDelete = frRemittanceRepository.findAll().size();

        // Delete the frRemittance
        restFrRemittanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, frRemittance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FrRemittance> frRemittanceList = frRemittanceRepository.findAll();
        assertThat(frRemittanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
