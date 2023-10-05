package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Division.
 */
@Entity
@Table(name = "division")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Division implements Serializable {

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

    @OneToMany(mappedBy = "division")
    @JsonIgnoreProperties(value = { "upazilas", "division" }, allowSetters = true)
    private Set<District> districts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Division id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Division name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return this.bnName;
    }

    public Division bnName(String bnName) {
        this.setBnName(bnName);
        return this;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public Set<District> getDistricts() {
        return this.districts;
    }

    public void setDistricts(Set<District> districts) {
        if (this.districts != null) {
            this.districts.forEach(i -> i.setDivision(null));
        }
        if (districts != null) {
            districts.forEach(i -> i.setDivision(this));
        }
        this.districts = districts;
    }

    public Division districts(Set<District> districts) {
        this.setDistricts(districts);
        return this;
    }

    public Division addDistrict(District district) {
        this.districts.add(district);
        district.setDivision(this);
        return this;
    }

    public Division removeDistrict(District district) {
        this.districts.remove(district);
        district.setDivision(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Division)) {
            return false;
        }
        return id != null && id.equals(((Division) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Division{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            "}";
    }
}
