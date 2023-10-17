package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
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

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

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
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", fertilizer=" + getFertilizer() +
            ", dealer=" + getDealer() +
            "}";
    }
}
