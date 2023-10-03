package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PayOrder.
 */
@Entity
@Table(name = "pay_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pay_order_number", nullable = false)
    private Long payOrderNumber;

    @NotNull
    @Column(name = "pay_order_date", nullable = false)
    private LocalDate payOrderDate;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "slip_no", nullable = false)
    private Long slipNo;

    @NotNull
    @Column(name = "controlling_no", nullable = false)
    private Long controllingNo;

    @ManyToOne
    private Fertilizer fertilizer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "upazila" }, allowSetters = true)
    private Dealer dealer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PayOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayOrderNumber() {
        return this.payOrderNumber;
    }

    public PayOrder payOrderNumber(Long payOrderNumber) {
        this.setPayOrderNumber(payOrderNumber);
        return this;
    }

    public void setPayOrderNumber(Long payOrderNumber) {
        this.payOrderNumber = payOrderNumber;
    }

    public LocalDate getPayOrderDate() {
        return this.payOrderDate;
    }

    public PayOrder payOrderDate(LocalDate payOrderDate) {
        this.setPayOrderDate(payOrderDate);
        return this;
    }

    public void setPayOrderDate(LocalDate payOrderDate) {
        this.payOrderDate = payOrderDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public PayOrder amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSlipNo() {
        return this.slipNo;
    }

    public PayOrder slipNo(Long slipNo) {
        this.setSlipNo(slipNo);
        return this;
    }

    public void setSlipNo(Long slipNo) {
        this.slipNo = slipNo;
    }

    public Long getControllingNo() {
        return this.controllingNo;
    }

    public PayOrder controllingNo(Long controllingNo) {
        this.setControllingNo(controllingNo);
        return this;
    }

    public void setControllingNo(Long controllingNo) {
        this.controllingNo = controllingNo;
    }

    public Fertilizer getFertilizer() {
        return this.fertilizer;
    }

    public void setFertilizer(Fertilizer fertilizer) {
        this.fertilizer = fertilizer;
    }

    public PayOrder fertilizer(Fertilizer fertilizer) {
        this.setFertilizer(fertilizer);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public PayOrder dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayOrder)) {
            return false;
        }
        return id != null && id.equals(((PayOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayOrder{" +
            "id=" + getId() +
            ", payOrderNumber=" + getPayOrderNumber() +
            ", payOrderDate='" + getPayOrderDate() + "'" +
            ", amount=" + getAmount() +
            ", slipNo=" + getSlipNo() +
            ", controllingNo=" + getControllingNo() +
            "}";
    }
}
