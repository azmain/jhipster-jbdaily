package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "dealer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dealer implements Serializable {

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
    @Column(name = "short_name", length = 255, nullable = false)
    private String shortName;

    @Size(max = 255)
    @Column(name = "mobile", length = 255)
    private String mobile;

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

    @OneToMany(mappedBy = "dealer")
    @JsonIgnoreProperties(value = { "fertilizer", "dealer" }, allowSetters = true)
    private Set<PayOrder> payOrders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "dealers", "district" }, allowSetters = true)
    private Upazila upazila;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dealer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Dealer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return this.bnName;
    }

    public Dealer bnName(String bnName) {
        this.setBnName(bnName);
        return this;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Dealer shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Dealer mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Dealer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Dealer createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Dealer lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Dealer lastModifiedDate(Instant lastModifiedDate) {
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
            this.payOrders.forEach(i -> i.setDealer(null));
        }
        if (payOrders != null) {
            payOrders.forEach(i -> i.setDealer(this));
        }
        this.payOrders = payOrders;
    }

    public Dealer payOrders(Set<PayOrder> payOrders) {
        this.setPayOrders(payOrders);
        return this;
    }

    public Dealer addPayOrder(PayOrder payOrder) {
        this.payOrders.add(payOrder);
        payOrder.setDealer(this);
        return this;
    }

    public Dealer removePayOrder(PayOrder payOrder) {
        this.payOrders.remove(payOrder);
        payOrder.setDealer(null);
        return this;
    }

    public Upazila getUpazila() {
        return this.upazila;
    }

    public void setUpazila(Upazila upazila) {
        this.upazila = upazila;
    }

    public Dealer upazila(Upazila upazila) {
        this.setUpazila(upazila);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dealer)) {
            return false;
        }
        return id != null && id.equals(((Dealer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dealer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
