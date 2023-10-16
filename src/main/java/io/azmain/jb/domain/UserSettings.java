package io.azmain.jb.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UserSettings.
 */
@Entity
@Table(name = "user_settings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @NotNull
    @Column(name = "pay_order_num_seq", nullable = false)
    private String payOrderNumSeq;

    @NotNull
    @Column(name = "pay_order_control_num", nullable = false)
    private String payOrderControlNum;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserSettings name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayOrderNumSeq() {
        return this.payOrderNumSeq;
    }

    public UserSettings payOrderNumSeq(String payOrderNumSeq) {
        this.setPayOrderNumSeq(payOrderNumSeq);
        return this;
    }

    public void setPayOrderNumSeq(String payOrderNumSeq) {
        this.payOrderNumSeq = payOrderNumSeq;
    }

    public String getPayOrderControlNum() {
        return this.payOrderControlNum;
    }

    public UserSettings payOrderControlNum(String payOrderControlNum) {
        this.setPayOrderControlNum(payOrderControlNum);
        return this;
    }

    public void setPayOrderControlNum(String payOrderControlNum) {
        this.payOrderControlNum = payOrderControlNum;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserSettings createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserSettings createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserSettings lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserSettings lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSettings)) {
            return false;
        }
        return id != null && id.equals(((UserSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSettings{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", payOrderNumSeq='" + getPayOrderNumSeq() + "'" +
            ", payOrderControlNum='" + getPayOrderControlNum() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
