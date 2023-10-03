package io.azmain.jb.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.azmain.jb.domain.PayOrder} entity. This class is used
 * in {@link io.azmain.jb.web.rest.PayOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pay-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter payOrderNumber;

    private LocalDateFilter payOrderDate;

    private BigDecimalFilter amount;

    private LongFilter slipNo;

    private LongFilter controllingNo;

    private LongFilter fertilizerId;

    private LongFilter dealerId;

    private Boolean distinct;

    public PayOrderCriteria() {}

    public PayOrderCriteria(PayOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.payOrderNumber = other.payOrderNumber == null ? null : other.payOrderNumber.copy();
        this.payOrderDate = other.payOrderDate == null ? null : other.payOrderDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.slipNo = other.slipNo == null ? null : other.slipNo.copy();
        this.controllingNo = other.controllingNo == null ? null : other.controllingNo.copy();
        this.fertilizerId = other.fertilizerId == null ? null : other.fertilizerId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PayOrderCriteria copy() {
        return new PayOrderCriteria(this);
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

    public LongFilter getPayOrderNumber() {
        return payOrderNumber;
    }

    public LongFilter payOrderNumber() {
        if (payOrderNumber == null) {
            payOrderNumber = new LongFilter();
        }
        return payOrderNumber;
    }

    public void setPayOrderNumber(LongFilter payOrderNumber) {
        this.payOrderNumber = payOrderNumber;
    }

    public LocalDateFilter getPayOrderDate() {
        return payOrderDate;
    }

    public LocalDateFilter payOrderDate() {
        if (payOrderDate == null) {
            payOrderDate = new LocalDateFilter();
        }
        return payOrderDate;
    }

    public void setPayOrderDate(LocalDateFilter payOrderDate) {
        this.payOrderDate = payOrderDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getSlipNo() {
        return slipNo;
    }

    public LongFilter slipNo() {
        if (slipNo == null) {
            slipNo = new LongFilter();
        }
        return slipNo;
    }

    public void setSlipNo(LongFilter slipNo) {
        this.slipNo = slipNo;
    }

    public LongFilter getControllingNo() {
        return controllingNo;
    }

    public LongFilter controllingNo() {
        if (controllingNo == null) {
            controllingNo = new LongFilter();
        }
        return controllingNo;
    }

    public void setControllingNo(LongFilter controllingNo) {
        this.controllingNo = controllingNo;
    }

    public LongFilter getFertilizerId() {
        return fertilizerId;
    }

    public LongFilter fertilizerId() {
        if (fertilizerId == null) {
            fertilizerId = new LongFilter();
        }
        return fertilizerId;
    }

    public void setFertilizerId(LongFilter fertilizerId) {
        this.fertilizerId = fertilizerId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            dealerId = new LongFilter();
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
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
        final PayOrderCriteria that = (PayOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(payOrderNumber, that.payOrderNumber) &&
            Objects.equals(payOrderDate, that.payOrderDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(slipNo, that.slipNo) &&
            Objects.equals(controllingNo, that.controllingNo) &&
            Objects.equals(fertilizerId, that.fertilizerId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payOrderNumber, payOrderDate, amount, slipNo, controllingNo, fertilizerId, dealerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (payOrderNumber != null ? "payOrderNumber=" + payOrderNumber + ", " : "") +
            (payOrderDate != null ? "payOrderDate=" + payOrderDate + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (slipNo != null ? "slipNo=" + slipNo + ", " : "") +
            (controllingNo != null ? "controllingNo=" + controllingNo + ", " : "") +
            (fertilizerId != null ? "fertilizerId=" + fertilizerId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
