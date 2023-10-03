package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.PayOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Long payOrderNumber;

    @NotNull
    private LocalDate payOrderDate;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long slipNo;

    @NotNull
    private Long controllingNo;

    private FertilizerDTO fertilizer;

    private DealerDTO dealer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayOrderNumber() {
        return payOrderNumber;
    }

    public void setPayOrderNumber(Long payOrderNumber) {
        this.payOrderNumber = payOrderNumber;
    }

    public LocalDate getPayOrderDate() {
        return payOrderDate;
    }

    public void setPayOrderDate(LocalDate payOrderDate) {
        this.payOrderDate = payOrderDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Long slipNo) {
        this.slipNo = slipNo;
    }

    public Long getControllingNo() {
        return controllingNo;
    }

    public void setControllingNo(Long controllingNo) {
        this.controllingNo = controllingNo;
    }

    public FertilizerDTO getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(FertilizerDTO fertilizer) {
        this.fertilizer = fertilizer;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayOrderDTO)) {
            return false;
        }

        PayOrderDTO payOrderDTO = (PayOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, payOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayOrderDTO{" +
            "id=" + getId() +
            ", payOrderNumber=" + getPayOrderNumber() +
            ", payOrderDate='" + getPayOrderDate() + "'" +
            ", amount=" + getAmount() +
            ", slipNo=" + getSlipNo() +
            ", controllingNo=" + getControllingNo() +
            ", fertilizer=" + getFertilizer() +
            ", dealer=" + getDealer() +
            "}";
    }
}
