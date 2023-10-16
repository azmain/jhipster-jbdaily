package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Fertilizer.
 */
@Entity
@Table(name = "fertilizer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fertilizer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "bn_name", length = 255, nullable = false)
    private String bnName;

    @NotNull
    @Size(max = 255)
    @Column(name = "account_no", length = 255, nullable = false)
    private String accountNo;

    @NotNull
    @Size(max = 255)
    @Column(name = "account_title", length = 255, nullable = false)
    private String accountTitle;

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

    @OneToMany(mappedBy = "fertilizer")
    @JsonIgnoreProperties(value = { "fertilizer", "dealer" }, allowSetters = true)
    private Set<PayOrder> payOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fertilizer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Fertilizer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return this.bnName;
    }

    public Fertilizer bnName(String bnName) {
        this.setBnName(bnName);
        return this;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public Fertilizer accountNo(String accountNo) {
        this.setAccountNo(accountNo);
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountTitle() {
        return this.accountTitle;
    }

    public Fertilizer accountTitle(String accountTitle) {
        this.setAccountTitle(accountTitle);
        return this;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Fertilizer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Fertilizer createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Fertilizer lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Fertilizer lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<PayOrder> getPayOrders() {
        return this.payOrders;
    }

    public void setPayOrders(Set<PayOrder> payOrders) {
        if (this.payOrders != null) {
            this.payOrders.forEach(i -> i.setFertilizer(null));
        }
        if (payOrders != null) {
            payOrders.forEach(i -> i.setFertilizer(this));
        }
        this.payOrders = payOrders;
    }

    public Fertilizer payOrders(Set<PayOrder> payOrders) {
        this.setPayOrders(payOrders);
        return this;
    }

    public Fertilizer addPayOrder(PayOrder payOrder) {
        this.payOrders.add(payOrder);
        payOrder.setFertilizer(this);
        return this;
    }

    public Fertilizer removePayOrder(PayOrder payOrder) {
        this.payOrders.remove(payOrder);
        payOrder.setFertilizer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fertilizer)) {
            return false;
        }
        return id != null && id.equals(((Fertilizer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fertilizer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", accountNo='" + getAccountNo() + "'" +
            ", accountTitle='" + getAccountTitle() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
