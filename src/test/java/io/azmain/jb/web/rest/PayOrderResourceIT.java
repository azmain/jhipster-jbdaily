package io.azmain.jb.web.rest;

import static io.azmain.jb.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.Dealer;
import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.service.criteria.PayOrderCriteria;
import io.azmain.jb.service.dto.PayOrderDTO;
import io.azmain.jb.service.mapper.PayOrderMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PayOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayOrderResourceIT {

    private static final Long DEFAULT_PAY_ORDER_NUMBER = 1L;
    private static final Long UPDATED_PAY_ORDER_NUMBER = 2L;
    private static final Long SMALLER_PAY_ORDER_NUMBER = 1L - 1L;

    private static final LocalDate DEFAULT_PAY_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAY_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAY_ORDER_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Long DEFAULT_SLIP_NO = 1L;
    private static final Long UPDATED_SLIP_NO = 2L;
    private static final Long SMALLER_SLIP_NO = 1L - 1L;

    private static final Long DEFAULT_CONTROLLING_NO = 1L;
    private static final Long UPDATED_CONTROLLING_NO = 2L;
    private static final Long SMALLER_CONTROLLING_NO = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/pay-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayOrderRepository payOrderRepository;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayOrderMockMvc;

    private PayOrder payOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayOrder createEntity(EntityManager em) {
        PayOrder payOrder = new PayOrder()
            .payOrderNumber(DEFAULT_PAY_ORDER_NUMBER)
            .payOrderDate(DEFAULT_PAY_ORDER_DATE)
            .amount(DEFAULT_AMOUNT)
            .slipNo(DEFAULT_SLIP_NO)
            .controllingNo(DEFAULT_CONTROLLING_NO);
        return payOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayOrder createUpdatedEntity(EntityManager em) {
        PayOrder payOrder = new PayOrder()
            .payOrderNumber(UPDATED_PAY_ORDER_NUMBER)
            .payOrderDate(UPDATED_PAY_ORDER_DATE)
            .amount(UPDATED_AMOUNT)
            .slipNo(UPDATED_SLIP_NO)
            .controllingNo(UPDATED_CONTROLLING_NO);
        return payOrder;
    }

    @BeforeEach
    public void initTest() {
        payOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createPayOrder() throws Exception {
        int databaseSizeBeforeCreate = payOrderRepository.findAll().size();
        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);
        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getPayOrderNumber()).isEqualTo(DEFAULT_PAY_ORDER_NUMBER);
        assertThat(testPayOrder.getPayOrderDate()).isEqualTo(DEFAULT_PAY_ORDER_DATE);
        assertThat(testPayOrder.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPayOrder.getSlipNo()).isEqualTo(DEFAULT_SLIP_NO);
        assertThat(testPayOrder.getControllingNo()).isEqualTo(DEFAULT_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void createPayOrderWithExistingId() throws Exception {
        // Create the PayOrder with an existing ID
        payOrder.setId(1L);
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        int databaseSizeBeforeCreate = payOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPayOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = payOrderRepository.findAll().size();
        // set the field null
        payOrder.setPayOrderNumber(null);

        // Create the PayOrder, which fails.
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = payOrderRepository.findAll().size();
        // set the field null
        payOrder.setPayOrderDate(null);

        // Create the PayOrder, which fails.
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = payOrderRepository.findAll().size();
        // set the field null
        payOrder.setAmount(null);

        // Create the PayOrder, which fails.
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlipNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = payOrderRepository.findAll().size();
        // set the field null
        payOrder.setSlipNo(null);

        // Create the PayOrder, which fails.
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkControllingNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = payOrderRepository.findAll().size();
        // set the field null
        payOrder.setControllingNo(null);

        // Create the PayOrder, which fails.
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayOrders() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].payOrderNumber").value(hasItem(DEFAULT_PAY_ORDER_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].payOrderDate").value(hasItem(DEFAULT_PAY_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].slipNo").value(hasItem(DEFAULT_SLIP_NO.intValue())))
            .andExpect(jsonPath("$.[*].controllingNo").value(hasItem(DEFAULT_CONTROLLING_NO.intValue())));
    }

    @Test
    @Transactional
    void getPayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get the payOrder
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, payOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payOrder.getId().intValue()))
            .andExpect(jsonPath("$.payOrderNumber").value(DEFAULT_PAY_ORDER_NUMBER.intValue()))
            .andExpect(jsonPath("$.payOrderDate").value(DEFAULT_PAY_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.slipNo").value(DEFAULT_SLIP_NO.intValue()))
            .andExpect(jsonPath("$.controllingNo").value(DEFAULT_CONTROLLING_NO.intValue()));
    }

    @Test
    @Transactional
    void getPayOrdersByIdFiltering() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        Long id = payOrder.getId();

        defaultPayOrderShouldBeFound("id.equals=" + id);
        defaultPayOrderShouldNotBeFound("id.notEquals=" + id);

        defaultPayOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPayOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultPayOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPayOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber equals to DEFAULT_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.equals=" + DEFAULT_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber equals to UPDATED_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.equals=" + UPDATED_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber in DEFAULT_PAY_ORDER_NUMBER or UPDATED_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.in=" + DEFAULT_PAY_ORDER_NUMBER + "," + UPDATED_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber equals to UPDATED_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.in=" + UPDATED_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber is not null
        defaultPayOrderShouldBeFound("payOrderNumber.specified=true");

        // Get all the payOrderList where payOrderNumber is null
        defaultPayOrderShouldNotBeFound("payOrderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber is greater than or equal to DEFAULT_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.greaterThanOrEqual=" + DEFAULT_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber is greater than or equal to UPDATED_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.greaterThanOrEqual=" + UPDATED_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber is less than or equal to DEFAULT_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.lessThanOrEqual=" + DEFAULT_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber is less than or equal to SMALLER_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.lessThanOrEqual=" + SMALLER_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber is less than DEFAULT_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.lessThan=" + DEFAULT_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber is less than UPDATED_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.lessThan=" + UPDATED_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderNumber is greater than DEFAULT_PAY_ORDER_NUMBER
        defaultPayOrderShouldNotBeFound("payOrderNumber.greaterThan=" + DEFAULT_PAY_ORDER_NUMBER);

        // Get all the payOrderList where payOrderNumber is greater than SMALLER_PAY_ORDER_NUMBER
        defaultPayOrderShouldBeFound("payOrderNumber.greaterThan=" + SMALLER_PAY_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate equals to DEFAULT_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.equals=" + DEFAULT_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate equals to UPDATED_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.equals=" + UPDATED_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate in DEFAULT_PAY_ORDER_DATE or UPDATED_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.in=" + DEFAULT_PAY_ORDER_DATE + "," + UPDATED_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate equals to UPDATED_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.in=" + UPDATED_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate is not null
        defaultPayOrderShouldBeFound("payOrderDate.specified=true");

        // Get all the payOrderList where payOrderDate is null
        defaultPayOrderShouldNotBeFound("payOrderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate is greater than or equal to DEFAULT_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.greaterThanOrEqual=" + DEFAULT_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate is greater than or equal to UPDATED_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.greaterThanOrEqual=" + UPDATED_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate is less than or equal to DEFAULT_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.lessThanOrEqual=" + DEFAULT_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate is less than or equal to SMALLER_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.lessThanOrEqual=" + SMALLER_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate is less than DEFAULT_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.lessThan=" + DEFAULT_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate is less than UPDATED_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.lessThan=" + UPDATED_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByPayOrderDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payOrderDate is greater than DEFAULT_PAY_ORDER_DATE
        defaultPayOrderShouldNotBeFound("payOrderDate.greaterThan=" + DEFAULT_PAY_ORDER_DATE);

        // Get all the payOrderList where payOrderDate is greater than SMALLER_PAY_ORDER_DATE
        defaultPayOrderShouldBeFound("payOrderDate.greaterThan=" + SMALLER_PAY_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount equals to DEFAULT_AMOUNT
        defaultPayOrderShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the payOrderList where amount equals to UPDATED_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPayOrderShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the payOrderList where amount equals to UPDATED_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount is not null
        defaultPayOrderShouldBeFound("amount.specified=true");

        // Get all the payOrderList where amount is null
        defaultPayOrderShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPayOrderShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the payOrderList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPayOrderShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the payOrderList where amount is less than or equal to SMALLER_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount is less than DEFAULT_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the payOrderList where amount is less than UPDATED_AMOUNT
        defaultPayOrderShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where amount is greater than DEFAULT_AMOUNT
        defaultPayOrderShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the payOrderList where amount is greater than SMALLER_AMOUNT
        defaultPayOrderShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo equals to DEFAULT_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.equals=" + DEFAULT_SLIP_NO);

        // Get all the payOrderList where slipNo equals to UPDATED_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.equals=" + UPDATED_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo in DEFAULT_SLIP_NO or UPDATED_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.in=" + DEFAULT_SLIP_NO + "," + UPDATED_SLIP_NO);

        // Get all the payOrderList where slipNo equals to UPDATED_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.in=" + UPDATED_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo is not null
        defaultPayOrderShouldBeFound("slipNo.specified=true");

        // Get all the payOrderList where slipNo is null
        defaultPayOrderShouldNotBeFound("slipNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo is greater than or equal to DEFAULT_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.greaterThanOrEqual=" + DEFAULT_SLIP_NO);

        // Get all the payOrderList where slipNo is greater than or equal to UPDATED_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.greaterThanOrEqual=" + UPDATED_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo is less than or equal to DEFAULT_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.lessThanOrEqual=" + DEFAULT_SLIP_NO);

        // Get all the payOrderList where slipNo is less than or equal to SMALLER_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.lessThanOrEqual=" + SMALLER_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo is less than DEFAULT_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.lessThan=" + DEFAULT_SLIP_NO);

        // Get all the payOrderList where slipNo is less than UPDATED_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.lessThan=" + UPDATED_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersBySlipNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where slipNo is greater than DEFAULT_SLIP_NO
        defaultPayOrderShouldNotBeFound("slipNo.greaterThan=" + DEFAULT_SLIP_NO);

        // Get all the payOrderList where slipNo is greater than SMALLER_SLIP_NO
        defaultPayOrderShouldBeFound("slipNo.greaterThan=" + SMALLER_SLIP_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo equals to DEFAULT_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.equals=" + DEFAULT_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo equals to UPDATED_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.equals=" + UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo in DEFAULT_CONTROLLING_NO or UPDATED_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.in=" + DEFAULT_CONTROLLING_NO + "," + UPDATED_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo equals to UPDATED_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.in=" + UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo is not null
        defaultPayOrderShouldBeFound("controllingNo.specified=true");

        // Get all the payOrderList where controllingNo is null
        defaultPayOrderShouldNotBeFound("controllingNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo is greater than or equal to DEFAULT_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.greaterThanOrEqual=" + DEFAULT_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo is greater than or equal to UPDATED_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.greaterThanOrEqual=" + UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo is less than or equal to DEFAULT_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.lessThanOrEqual=" + DEFAULT_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo is less than or equal to SMALLER_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.lessThanOrEqual=" + SMALLER_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo is less than DEFAULT_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.lessThan=" + DEFAULT_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo is less than UPDATED_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.lessThan=" + UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByControllingNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where controllingNo is greater than DEFAULT_CONTROLLING_NO
        defaultPayOrderShouldNotBeFound("controllingNo.greaterThan=" + DEFAULT_CONTROLLING_NO);

        // Get all the payOrderList where controllingNo is greater than SMALLER_CONTROLLING_NO
        defaultPayOrderShouldBeFound("controllingNo.greaterThan=" + SMALLER_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void getAllPayOrdersByFertilizerIsEqualToSomething() throws Exception {
        Fertilizer fertilizer;
        if (TestUtil.findAll(em, Fertilizer.class).isEmpty()) {
            payOrderRepository.saveAndFlush(payOrder);
            fertilizer = FertilizerResourceIT.createEntity(em);
        } else {
            fertilizer = TestUtil.findAll(em, Fertilizer.class).get(0);
        }
        em.persist(fertilizer);
        em.flush();
        payOrder.setFertilizer(fertilizer);
        payOrderRepository.saveAndFlush(payOrder);
        Long fertilizerId = fertilizer.getId();

        // Get all the payOrderList where fertilizer equals to fertilizerId
        defaultPayOrderShouldBeFound("fertilizerId.equals=" + fertilizerId);

        // Get all the payOrderList where fertilizer equals to (fertilizerId + 1)
        defaultPayOrderShouldNotBeFound("fertilizerId.equals=" + (fertilizerId + 1));
    }

    @Test
    @Transactional
    void getAllPayOrdersByDealerIsEqualToSomething() throws Exception {
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            payOrderRepository.saveAndFlush(payOrder);
            dealer = DealerResourceIT.createEntity(em);
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        payOrder.setDealer(dealer);
        payOrderRepository.saveAndFlush(payOrder);
        Long dealerId = dealer.getId();

        // Get all the payOrderList where dealer equals to dealerId
        defaultPayOrderShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the payOrderList where dealer equals to (dealerId + 1)
        defaultPayOrderShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPayOrderShouldBeFound(String filter) throws Exception {
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].payOrderNumber").value(hasItem(DEFAULT_PAY_ORDER_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].payOrderDate").value(hasItem(DEFAULT_PAY_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].slipNo").value(hasItem(DEFAULT_SLIP_NO.intValue())))
            .andExpect(jsonPath("$.[*].controllingNo").value(hasItem(DEFAULT_CONTROLLING_NO.intValue())));

        // Check, that the count call also returns 1
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPayOrderShouldNotBeFound(String filter) throws Exception {
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPayOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayOrder() throws Exception {
        // Get the payOrder
        restPayOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();

        // Update the payOrder
        PayOrder updatedPayOrder = payOrderRepository.findById(payOrder.getId()).get();
        // Disconnect from session so that the updates on updatedPayOrder are not directly saved in db
        em.detach(updatedPayOrder);
        updatedPayOrder
            .payOrderNumber(UPDATED_PAY_ORDER_NUMBER)
            .payOrderDate(UPDATED_PAY_ORDER_DATE)
            .amount(UPDATED_AMOUNT)
            .slipNo(UPDATED_SLIP_NO)
            .controllingNo(UPDATED_CONTROLLING_NO);
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(updatedPayOrder);

        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getPayOrderNumber()).isEqualTo(UPDATED_PAY_ORDER_NUMBER);
        assertThat(testPayOrder.getPayOrderDate()).isEqualTo(UPDATED_PAY_ORDER_DATE);
        assertThat(testPayOrder.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPayOrder.getSlipNo()).isEqualTo(UPDATED_SLIP_NO);
        assertThat(testPayOrder.getControllingNo()).isEqualTo(UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void putNonExistingPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayOrderWithPatch() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();

        // Update the payOrder using partial update
        PayOrder partialUpdatedPayOrder = new PayOrder();
        partialUpdatedPayOrder.setId(payOrder.getId());

        partialUpdatedPayOrder.payOrderNumber(UPDATED_PAY_ORDER_NUMBER).amount(UPDATED_AMOUNT).controllingNo(UPDATED_CONTROLLING_NO);

        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayOrder))
            )
            .andExpect(status().isOk());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getPayOrderNumber()).isEqualTo(UPDATED_PAY_ORDER_NUMBER);
        assertThat(testPayOrder.getPayOrderDate()).isEqualTo(DEFAULT_PAY_ORDER_DATE);
        assertThat(testPayOrder.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPayOrder.getSlipNo()).isEqualTo(DEFAULT_SLIP_NO);
        assertThat(testPayOrder.getControllingNo()).isEqualTo(UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void fullUpdatePayOrderWithPatch() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();

        // Update the payOrder using partial update
        PayOrder partialUpdatedPayOrder = new PayOrder();
        partialUpdatedPayOrder.setId(payOrder.getId());

        partialUpdatedPayOrder
            .payOrderNumber(UPDATED_PAY_ORDER_NUMBER)
            .payOrderDate(UPDATED_PAY_ORDER_DATE)
            .amount(UPDATED_AMOUNT)
            .slipNo(UPDATED_SLIP_NO)
            .controllingNo(UPDATED_CONTROLLING_NO);

        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayOrder))
            )
            .andExpect(status().isOk());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getPayOrderNumber()).isEqualTo(UPDATED_PAY_ORDER_NUMBER);
        assertThat(testPayOrder.getPayOrderDate()).isEqualTo(UPDATED_PAY_ORDER_DATE);
        assertThat(testPayOrder.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPayOrder.getSlipNo()).isEqualTo(UPDATED_SLIP_NO);
        assertThat(testPayOrder.getControllingNo()).isEqualTo(UPDATED_CONTROLLING_NO);
    }

    @Test
    @Transactional
    void patchNonExistingPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();
        payOrder.setId(count.incrementAndGet());

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        int databaseSizeBeforeDelete = payOrderRepository.findAll().size();

        // Delete the payOrder
        restPayOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, payOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
