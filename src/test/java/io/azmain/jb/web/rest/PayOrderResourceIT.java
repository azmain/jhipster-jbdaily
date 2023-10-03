package io.azmain.jb.web.rest;

import static io.azmain.jb.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.azmain.jb.IntegrationTest;
import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.PayOrderRepository;
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

    private static final LocalDate DEFAULT_PAY_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAY_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Long DEFAULT_SLIP_NO = 1L;
    private static final Long UPDATED_SLIP_NO = 2L;

    private static final Long DEFAULT_CONTROLLING_NO = 1L;
    private static final Long UPDATED_CONTROLLING_NO = 2L;

    private static final String ENTITY_API_URL = "/api/pay-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayOrderRepository payOrderRepository;

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
        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        int databaseSizeBeforeCreate = payOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayOrder))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payOrder))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payOrder))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payOrder)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payOrder))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payOrder))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payOrder)))
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
