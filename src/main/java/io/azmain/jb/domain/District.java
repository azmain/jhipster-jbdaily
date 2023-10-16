package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A District.
 */
@Entity
@Table(name = "district")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class District implements Serializable {

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

    @OneToMany(mappedBy = "district")
    @JsonIgnoreProperties(value = { "dealers", "district" }, allowSetters = true)
    private Set<Upazila> upazilas = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "districts" }, allowSetters = true)
    private Division division;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public District id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public District name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return this.bnName;
    }

    public District bnName(String bnName) {
        this.setBnName(bnName);
        return this;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public District createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public District createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public District lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public District lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Upazila> getUpazilas() {
        return this.upazilas;
    }

    public void setUpazilas(Set<Upazila> upazilas) {
        if (this.upazilas != null) {
            this.upazilas.forEach(i -> i.setDistrict(null));
        }
        if (upazilas != null) {
            upazilas.forEach(i -> i.setDistrict(this));
        }
        this.upazilas = upazilas;
    }

    public District upazilas(Set<Upazila> upazilas) {
        this.setUpazilas(upazilas);
        return this;
    }

    public District addUpazila(Upazila upazila) {
        this.upazilas.add(upazila);
        upazila.setDistrict(this);
        return this;
    }

    public District removeUpazila(Upazila upazila) {
        this.upazilas.remove(upazila);
        upazila.setDistrict(null);
        return this;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public District division(Division division) {
        this.setDivision(division);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return id != null && id.equals(((District) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
