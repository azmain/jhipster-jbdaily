package io.azmain.jb.service.criteria;

import io.azmain.jb.domain.enumeration.DocumentType;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.Gender;
import io.azmain.jb.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.azmain.jb.domain.FrRemittance} entity. This class is used
 * in {@link io.azmain.jb.web.rest.FrRemittanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fr-remittances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrRemittanceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }
    }

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {

        public DocumentTypeFilter() {}

        public DocumentTypeFilter(DocumentTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocumentTypeFilter copy() {
            return new DocumentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pin;

    private StringFilter remitersName;

    private StringFilter amount;

    private StringFilter incentiveAmount;

    private LocalDateFilter paymentDate;

    private LocalDateFilter incPaymentDate;

    private LocalDateFilter remiSendingDate;

    private StringFilter remiFrCurrency;

    private StringFilter currency;

    private StringFilter country;

    private StringFilter exchangeRate;

    private TransactionTypeFilter transactionType;

    private StringFilter recvMobileNo;

    private StringFilter recvName;

    private StringFilter recvLegalId;

    private StringFilter moneyExchangeName;

    private LocalDateFilter amountReimDate;

    private LocalDateFilter incAmountReimDate;

    private GenderFilter recvGender;

    private GenderFilter remiGender;

    private DocumentTypeFilter documentType;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter moneyExchangeId;

    private LongFilter incPercentageId;

    private Boolean distinct;

    public FrRemittanceCriteria() {}

    public FrRemittanceCriteria(FrRemittanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pin = other.pin == null ? null : other.pin.copy();
        this.remitersName = other.remitersName == null ? null : other.remitersName.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.incentiveAmount = other.incentiveAmount == null ? null : other.incentiveAmount.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.incPaymentDate = other.incPaymentDate == null ? null : other.incPaymentDate.copy();
        this.remiSendingDate = other.remiSendingDate == null ? null : other.remiSendingDate.copy();
        this.remiFrCurrency = other.remiFrCurrency == null ? null : other.remiFrCurrency.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.exchangeRate = other.exchangeRate == null ? null : other.exchangeRate.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.recvMobileNo = other.recvMobileNo == null ? null : other.recvMobileNo.copy();
        this.recvName = other.recvName == null ? null : other.recvName.copy();
        this.recvLegalId = other.recvLegalId == null ? null : other.recvLegalId.copy();
        this.moneyExchangeName = other.moneyExchangeName == null ? null : other.moneyExchangeName.copy();
        this.amountReimDate = other.amountReimDate == null ? null : other.amountReimDate.copy();
        this.incAmountReimDate = other.incAmountReimDate == null ? null : other.incAmountReimDate.copy();
        this.recvGender = other.recvGender == null ? null : other.recvGender.copy();
        this.remiGender = other.remiGender == null ? null : other.remiGender.copy();
        this.documentType = other.documentType == null ? null : other.documentType.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.moneyExchangeId = other.moneyExchangeId == null ? null : other.moneyExchangeId.copy();
        this.incPercentageId = other.incPercentageId == null ? null : other.incPercentageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FrRemittanceCriteria copy() {
        return new FrRemittanceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPin() {
        return pin;
    }

    public StringFilter pin() {
        if (pin == null) {
            pin = new StringFilter();
        }
        return pin;
    }

    public void setPin(StringFilter pin) {
        this.pin = pin;
    }

    public StringFilter getRemitersName() {
        return remitersName;
    }

    public StringFilter remitersName() {
        if (remitersName == null) {
            remitersName = new StringFilter();
        }
        return remitersName;
    }

    public void setRemitersName(StringFilter remitersName) {
        this.remitersName = remitersName;
    }

    public StringFilter getAmount() {
        return amount;
    }

    public StringFilter amount() {
        if (amount == null) {
            amount = new StringFilter();
        }
        return amount;
    }

    public void setAmount(StringFilter amount) {
        this.amount = amount;
    }

    public StringFilter getIncentiveAmount() {
        return incentiveAmount;
    }

    public StringFilter incentiveAmount() {
        if (incentiveAmount == null) {
            incentiveAmount = new StringFilter();
        }
        return incentiveAmount;
    }

    public void setIncentiveAmount(StringFilter incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public LocalDateFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new LocalDateFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateFilter getIncPaymentDate() {
        return incPaymentDate;
    }

    public LocalDateFilter incPaymentDate() {
        if (incPaymentDate == null) {
            incPaymentDate = new LocalDateFilter();
        }
        return incPaymentDate;
    }

    public void setIncPaymentDate(LocalDateFilter incPaymentDate) {
        this.incPaymentDate = incPaymentDate;
    }

    public LocalDateFilter getRemiSendingDate() {
        return remiSendingDate;
    }

    public LocalDateFilter remiSendingDate() {
        if (remiSendingDate == null) {
            remiSendingDate = new LocalDateFilter();
        }
        return remiSendingDate;
    }

    public void setRemiSendingDate(LocalDateFilter remiSendingDate) {
        this.remiSendingDate = remiSendingDate;
    }

    public StringFilter getRemiFrCurrency() {
        return remiFrCurrency;
    }

    public StringFilter remiFrCurrency() {
        if (remiFrCurrency == null) {
            remiFrCurrency = new StringFilter();
        }
        return remiFrCurrency;
    }

    public void setRemiFrCurrency(StringFilter remiFrCurrency) {
        this.remiFrCurrency = remiFrCurrency;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getExchangeRate() {
        return exchangeRate;
    }

    public StringFilter exchangeRate() {
        if (exchangeRate == null) {
            exchangeRate = new StringFilter();
        }
        return exchangeRate;
    }

    public void setExchangeRate(StringFilter exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public TransactionTypeFilter transactionType() {
        if (transactionType == null) {
            transactionType = new TransactionTypeFilter();
        }
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public StringFilter getRecvMobileNo() {
        return recvMobileNo;
    }

    public StringFilter recvMobileNo() {
        if (recvMobileNo == null) {
            recvMobileNo = new StringFilter();
        }
        return recvMobileNo;
    }

    public void setRecvMobileNo(StringFilter recvMobileNo) {
        this.recvMobileNo = recvMobileNo;
    }

    public StringFilter getRecvName() {
        return recvName;
    }

    public StringFilter recvName() {
        if (recvName == null) {
            recvName = new StringFilter();
        }
        return recvName;
    }

    public void setRecvName(StringFilter recvName) {
        this.recvName = recvName;
    }

    public StringFilter getRecvLegalId() {
        return recvLegalId;
    }

    public StringFilter recvLegalId() {
        if (recvLegalId == null) {
            recvLegalId = new StringFilter();
        }
        return recvLegalId;
    }

    public void setRecvLegalId(StringFilter recvLegalId) {
        this.recvLegalId = recvLegalId;
    }

    public StringFilter getMoneyExchangeName() {
        return moneyExchangeName;
    }

    public StringFilter moneyExchangeName() {
        if (moneyExchangeName == null) {
            moneyExchangeName = new StringFilter();
        }
        return moneyExchangeName;
    }

    public void setMoneyExchangeName(StringFilter moneyExchangeName) {
        this.moneyExchangeName = moneyExchangeName;
    }

    public LocalDateFilter getAmountReimDate() {
        return amountReimDate;
    }

    public LocalDateFilter amountReimDate() {
        if (amountReimDate == null) {
            amountReimDate = new LocalDateFilter();
        }
        return amountReimDate;
    }

    public void setAmountReimDate(LocalDateFilter amountReimDate) {
        this.amountReimDate = amountReimDate;
    }

    public LocalDateFilter getIncAmountReimDate() {
        return incAmountReimDate;
    }

    public LocalDateFilter incAmountReimDate() {
        if (incAmountReimDate == null) {
            incAmountReimDate = new LocalDateFilter();
        }
        return incAmountReimDate;
    }

    public void setIncAmountReimDate(LocalDateFilter incAmountReimDate) {
        this.incAmountReimDate = incAmountReimDate;
    }

    public GenderFilter getRecvGender() {
        return recvGender;
    }

    public GenderFilter recvGender() {
        if (recvGender == null) {
            recvGender = new GenderFilter();
        }
        return recvGender;
    }

    public void setRecvGender(GenderFilter recvGender) {
        this.recvGender = recvGender;
    }

    public GenderFilter getRemiGender() {
        return remiGender;
    }

    public GenderFilter remiGender() {
        if (remiGender == null) {
            remiGender = new GenderFilter();
        }
        return remiGender;
    }

    public void setRemiGender(GenderFilter remiGender) {
        this.remiGender = remiGender;
    }

    public DocumentTypeFilter getDocumentType() {
        return documentType;
    }

    public DocumentTypeFilter documentType() {
        if (documentType == null) {
            documentType = new DocumentTypeFilter();
        }
        return documentType;
    }

    public void setDocumentType(DocumentTypeFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getMoneyExchangeId() {
        return moneyExchangeId;
    }

    public LongFilter moneyExchangeId() {
        if (moneyExchangeId == null) {
            moneyExchangeId = new LongFilter();
        }
        return moneyExchangeId;
    }

    public void setMoneyExchangeId(LongFilter moneyExchangeId) {
        this.moneyExchangeId = moneyExchangeId;
    }

    public LongFilter getIncPercentageId() {
        return incPercentageId;
    }

    public LongFilter incPercentageId() {
        if (incPercentageId == null) {
            incPercentageId = new LongFilter();
        }
        return incPercentageId;
    }

    public void setIncPercentageId(LongFilter incPercentageId) {
        this.incPercentageId = incPercentageId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FrRemittanceCriteria that = (FrRemittanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pin, that.pin) &&
            Objects.equals(remitersName, that.remitersName) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(incentiveAmount, that.incentiveAmount) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(incPaymentDate, that.incPaymentDate) &&
            Objects.equals(remiSendingDate, that.remiSendingDate) &&
            Objects.equals(remiFrCurrency, that.remiFrCurrency) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(country, that.country) &&
            Objects.equals(exchangeRate, that.exchangeRate) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(recvMobileNo, that.recvMobileNo) &&
            Objects.equals(recvName, that.recvName) &&
            Objects.equals(recvLegalId, that.recvLegalId) &&
            Objects.equals(moneyExchangeName, that.moneyExchangeName) &&
            Objects.equals(amountReimDate, that.amountReimDate) &&
            Objects.equals(incAmountReimDate, that.incAmountReimDate) &&
            Objects.equals(recvGender, that.recvGender) &&
            Objects.equals(remiGender, that.remiGender) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(moneyExchangeId, that.moneyExchangeId) &&
            Objects.equals(incPercentageId, that.incPercentageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pin,
            remitersName,
            amount,
            incentiveAmount,
            paymentDate,
            incPaymentDate,
            remiSendingDate,
            remiFrCurrency,
            currency,
            country,
            exchangeRate,
            transactionType,
            recvMobileNo,
            recvName,
            recvLegalId,
            moneyExchangeName,
            amountReimDate,
            incAmountReimDate,
            recvGender,
            remiGender,
            documentType,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            moneyExchangeId,
            incPercentageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrRemittanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pin != null ? "pin=" + pin + ", " : "") +
            (remitersName != null ? "remitersName=" + remitersName + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (incentiveAmount != null ? "incentiveAmount=" + incentiveAmount + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (incPaymentDate != null ? "incPaymentDate=" + incPaymentDate + ", " : "") +
            (remiSendingDate != null ? "remiSendingDate=" + remiSendingDate + ", " : "") +
            (remiFrCurrency != null ? "remiFrCurrency=" + remiFrCurrency + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (exchangeRate != null ? "exchangeRate=" + exchangeRate + ", " : "") +
            (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
            (recvMobileNo != null ? "recvMobileNo=" + recvMobileNo + ", " : "") +
            (recvName != null ? "recvName=" + recvName + ", " : "") +
            (recvLegalId != null ? "recvLegalId=" + recvLegalId + ", " : "") +
            (moneyExchangeName != null ? "moneyExchangeName=" + moneyExchangeName + ", " : "") +
            (amountReimDate != null ? "amountReimDate=" + amountReimDate + ", " : "") +
            (incAmountReimDate != null ? "incAmountReimDate=" + incAmountReimDate + ", " : "") +
            (recvGender != null ? "recvGender=" + recvGender + ", " : "") +
            (remiGender != null ? "remiGender=" + remiGender + ", " : "") +
            (documentType != null ? "documentType=" + documentType + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (moneyExchangeId != null ? "moneyExchangeId=" + moneyExchangeId + ", " : "") +
            (incPercentageId != null ? "incPercentageId=" + incPercentageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
