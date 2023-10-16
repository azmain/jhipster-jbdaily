package io.azmain.jb.domain;

import io.azmain.jb.domain.enumeration.DocumentType;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FrRemittance.
 */
@Entity
@Table(name = "fr_remittance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrRemittance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "pin", length = 256, nullable = false)
    private String pin;

    @NotNull
    @Size(max = 256)
    @Column(name = "remiters_name", length = 256, nullable = false)
    private String remitersName;

    @NotNull
    @Size(max = 256)
    @Column(name = "amount", length = 256, nullable = false)
    private String amount;

    @NotNull
    @Size(max = 256)
    @Column(name = "incentive_amount", length = 256, nullable = false)
    private String incentiveAmount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "inc_payment_date", nullable = false)
    private LocalDate incPaymentDate;

    @Column(name = "remi_sending_date")
    private LocalDate remiSendingDate;

    @Size(max = 256)
    @Column(name = "remi_fr_currency", length = 256)
    private String remiFrCurrency;

    @Size(max = 256)
    @Column(name = "currency", length = 256)
    private String currency;

    @Size(max = 256)
    @Column(name = "country", length = 256)
    private String country;

    @Size(max = 256)
    @Column(name = "exchange_rate", length = 256)
    private String exchangeRate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @NotNull
    @Size(max = 256)
    @Column(name = "recv_mobile_no", length = 256, nullable = false)
    private String recvMobileNo;

    @NotNull
    @Size(max = 255)
    @Column(name = "recv_name", length = 255, nullable = false)
    private String recvName;

    @NotNull
    @Size(max = 255)
    @Column(name = "recv_legal_id", length = 255, nullable = false)
    private String recvLegalId;

    @Size(max = 256)
    @Column(name = "money_exchange_name", length = 256)
    private String moneyExchangeName;

    @Column(name = "amount_reim_date")
    private LocalDate amountReimDate;

    @Column(name = "inc_amount_reim_date")
    private LocalDate incAmountReimDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recv_gender", nullable = false)
    private Gender recvGender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "remi_gender", nullable = false)
    private Gender remiGender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(optional = false)
    @NotNull
    private MoneyExchange moneyExchange;

    @ManyToOne(optional = false)
    @NotNull
    private IncPercentage incPercentage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FrRemittance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return this.pin;
    }

    public FrRemittance pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getRemitersName() {
        return this.remitersName;
    }

    public FrRemittance remitersName(String remitersName) {
        this.setRemitersName(remitersName);
        return this;
    }

    public void setRemitersName(String remitersName) {
        this.remitersName = remitersName;
    }

    public String getAmount() {
        return this.amount;
    }

    public FrRemittance amount(String amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIncentiveAmount() {
        return this.incentiveAmount;
    }

    public FrRemittance incentiveAmount(String incentiveAmount) {
        this.setIncentiveAmount(incentiveAmount);
        return this;
    }

    public void setIncentiveAmount(String incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public FrRemittance paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getIncPaymentDate() {
        return this.incPaymentDate;
    }

    public FrRemittance incPaymentDate(LocalDate incPaymentDate) {
        this.setIncPaymentDate(incPaymentDate);
        return this;
    }

    public void setIncPaymentDate(LocalDate incPaymentDate) {
        this.incPaymentDate = incPaymentDate;
    }

    public LocalDate getRemiSendingDate() {
        return this.remiSendingDate;
    }

    public FrRemittance remiSendingDate(LocalDate remiSendingDate) {
        this.setRemiSendingDate(remiSendingDate);
        return this;
    }

    public void setRemiSendingDate(LocalDate remiSendingDate) {
        this.remiSendingDate = remiSendingDate;
    }

    public String getRemiFrCurrency() {
        return this.remiFrCurrency;
    }

    public FrRemittance remiFrCurrency(String remiFrCurrency) {
        this.setRemiFrCurrency(remiFrCurrency);
        return this;
    }

    public void setRemiFrCurrency(String remiFrCurrency) {
        this.remiFrCurrency = remiFrCurrency;
    }

    public String getCurrency() {
        return this.currency;
    }

    public FrRemittance currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return this.country;
    }

    public FrRemittance country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExchangeRate() {
        return this.exchangeRate;
    }

    public FrRemittance exchangeRate(String exchangeRate) {
        this.setExchangeRate(exchangeRate);
        return this;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public FrRemittance transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecvMobileNo() {
        return this.recvMobileNo;
    }

    public FrRemittance recvMobileNo(String recvMobileNo) {
        this.setRecvMobileNo(recvMobileNo);
        return this;
    }

    public void setRecvMobileNo(String recvMobileNo) {
        this.recvMobileNo = recvMobileNo;
    }

    public String getRecvName() {
        return this.recvName;
    }

    public FrRemittance recvName(String recvName) {
        this.setRecvName(recvName);
        return this;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getRecvLegalId() {
        return this.recvLegalId;
    }

    public FrRemittance recvLegalId(String recvLegalId) {
        this.setRecvLegalId(recvLegalId);
        return this;
    }

    public void setRecvLegalId(String recvLegalId) {
        this.recvLegalId = recvLegalId;
    }

    public String getMoneyExchangeName() {
        return this.moneyExchangeName;
    }

    public FrRemittance moneyExchangeName(String moneyExchangeName) {
        this.setMoneyExchangeName(moneyExchangeName);
        return this;
    }

    public void setMoneyExchangeName(String moneyExchangeName) {
        this.moneyExchangeName = moneyExchangeName;
    }

    public LocalDate getAmountReimDate() {
        return this.amountReimDate;
    }

    public FrRemittance amountReimDate(LocalDate amountReimDate) {
        this.setAmountReimDate(amountReimDate);
        return this;
    }

    public void setAmountReimDate(LocalDate amountReimDate) {
        this.amountReimDate = amountReimDate;
    }

    public LocalDate getIncAmountReimDate() {
        return this.incAmountReimDate;
    }

    public FrRemittance incAmountReimDate(LocalDate incAmountReimDate) {
        this.setIncAmountReimDate(incAmountReimDate);
        return this;
    }

    public void setIncAmountReimDate(LocalDate incAmountReimDate) {
        this.incAmountReimDate = incAmountReimDate;
    }

    public Gender getRecvGender() {
        return this.recvGender;
    }

    public FrRemittance recvGender(Gender recvGender) {
        this.setRecvGender(recvGender);
        return this;
    }

    public void setRecvGender(Gender recvGender) {
        this.recvGender = recvGender;
    }

    public Gender getRemiGender() {
        return this.remiGender;
    }

    public FrRemittance remiGender(Gender remiGender) {
        this.setRemiGender(remiGender);
        return this;
    }

    public void setRemiGender(Gender remiGender) {
        this.remiGender = remiGender;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public FrRemittance documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public FrRemittance createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public FrRemittance createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public FrRemittance lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public FrRemittance lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public MoneyExchange getMoneyExchange() {
        return this.moneyExchange;
    }

    public void setMoneyExchange(MoneyExchange moneyExchange) {
        this.moneyExchange = moneyExchange;
    }

    public FrRemittance moneyExchange(MoneyExchange moneyExchange) {
        this.setMoneyExchange(moneyExchange);
        return this;
    }

    public IncPercentage getIncPercentage() {
        return this.incPercentage;
    }

    public void setIncPercentage(IncPercentage incPercentage) {
        this.incPercentage = incPercentage;
    }

    public FrRemittance incPercentage(IncPercentage incPercentage) {
        this.setIncPercentage(incPercentage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrRemittance)) {
            return false;
        }
        return id != null && id.equals(((FrRemittance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrRemittance{" +
            "id=" + getId() +
            ", pin='" + getPin() + "'" +
            ", remitersName='" + getRemitersName() + "'" +
            ", amount='" + getAmount() + "'" +
            ", incentiveAmount='" + getIncentiveAmount() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", incPaymentDate='" + getIncPaymentDate() + "'" +
            ", remiSendingDate='" + getRemiSendingDate() + "'" +
            ", remiFrCurrency='" + getRemiFrCurrency() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", country='" + getCountry() + "'" +
            ", exchangeRate='" + getExchangeRate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", recvMobileNo='" + getRecvMobileNo() + "'" +
            ", recvName='" + getRecvName() + "'" +
            ", recvLegalId='" + getRecvLegalId() + "'" +
            ", moneyExchangeName='" + getMoneyExchangeName() + "'" +
            ", amountReimDate='" + getAmountReimDate() + "'" +
            ", incAmountReimDate='" + getIncAmountReimDate() + "'" +
            ", recvGender='" + getRecvGender() + "'" +
            ", remiGender='" + getRemiGender() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
