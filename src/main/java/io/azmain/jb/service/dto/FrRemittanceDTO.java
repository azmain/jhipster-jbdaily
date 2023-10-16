package io.azmain.jb.service.dto;

import io.azmain.jb.domain.enumeration.DocumentType;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.FrRemittance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrRemittanceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String pin;

    @NotNull
    @Size(max = 256)
    private String remitersName;

    @NotNull
    @Size(max = 256)
    private String amount;

    @NotNull
    @Size(max = 256)
    private String incentiveAmount;

    @NotNull
    private LocalDate paymentDate;

    @NotNull
    private LocalDate incPaymentDate;

    private LocalDate remiSendingDate;

    @Size(max = 256)
    private String remiFrCurrency;

    @Size(max = 256)
    private String currency;

    @Size(max = 256)
    private String country;

    @Size(max = 256)
    private String exchangeRate;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    @Size(max = 256)
    private String recvMobileNo;

    @NotNull
    @Size(max = 255)
    private String recvName;

    @NotNull
    @Size(max = 255)
    private String recvLegalId;

    @Size(max = 256)
    private String moneyExchangeName;

    private LocalDate amountReimDate;

    private LocalDate incAmountReimDate;

    @NotNull
    private Gender recvGender;

    @NotNull
    private Gender remiGender;

    @NotNull
    private DocumentType documentType;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private MoneyExchangeDTO moneyExchange;

    private IncPercentageDTO incPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getRemitersName() {
        return remitersName;
    }

    public void setRemitersName(String remitersName) {
        this.remitersName = remitersName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIncentiveAmount() {
        return incentiveAmount;
    }

    public void setIncentiveAmount(String incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getIncPaymentDate() {
        return incPaymentDate;
    }

    public void setIncPaymentDate(LocalDate incPaymentDate) {
        this.incPaymentDate = incPaymentDate;
    }

    public LocalDate getRemiSendingDate() {
        return remiSendingDate;
    }

    public void setRemiSendingDate(LocalDate remiSendingDate) {
        this.remiSendingDate = remiSendingDate;
    }

    public String getRemiFrCurrency() {
        return remiFrCurrency;
    }

    public void setRemiFrCurrency(String remiFrCurrency) {
        this.remiFrCurrency = remiFrCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecvMobileNo() {
        return recvMobileNo;
    }

    public void setRecvMobileNo(String recvMobileNo) {
        this.recvMobileNo = recvMobileNo;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getRecvLegalId() {
        return recvLegalId;
    }

    public void setRecvLegalId(String recvLegalId) {
        this.recvLegalId = recvLegalId;
    }

    public String getMoneyExchangeName() {
        return moneyExchangeName;
    }

    public void setMoneyExchangeName(String moneyExchangeName) {
        this.moneyExchangeName = moneyExchangeName;
    }

    public LocalDate getAmountReimDate() {
        return amountReimDate;
    }

    public void setAmountReimDate(LocalDate amountReimDate) {
        this.amountReimDate = amountReimDate;
    }

    public LocalDate getIncAmountReimDate() {
        return incAmountReimDate;
    }

    public void setIncAmountReimDate(LocalDate incAmountReimDate) {
        this.incAmountReimDate = incAmountReimDate;
    }

    public Gender getRecvGender() {
        return recvGender;
    }

    public void setRecvGender(Gender recvGender) {
        this.recvGender = recvGender;
    }

    public Gender getRemiGender() {
        return remiGender;
    }

    public void setRemiGender(Gender remiGender) {
        this.remiGender = remiGender;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public MoneyExchangeDTO getMoneyExchange() {
        return moneyExchange;
    }

    public void setMoneyExchange(MoneyExchangeDTO moneyExchange) {
        this.moneyExchange = moneyExchange;
    }

    public IncPercentageDTO getIncPercentage() {
        return incPercentage;
    }

    public void setIncPercentage(IncPercentageDTO incPercentage) {
        this.incPercentage = incPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrRemittanceDTO)) {
            return false;
        }

        FrRemittanceDTO frRemittanceDTO = (FrRemittanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, frRemittanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrRemittanceDTO{" +
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
            ", moneyExchange=" + getMoneyExchange() +
            ", incPercentage=" + getIncPercentage() +
            "}";
    }
}
