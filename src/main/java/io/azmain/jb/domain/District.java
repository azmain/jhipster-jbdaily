package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "bn_name", nullable = false)
    private String bnName;

    @OneToMany(mappedBy = "district")
    @JsonIgnoreProperties(value = { "dealers", "district" }, allowSetters = true)
    private Set<Upazila> upazilas = new HashSet<>();

    @ManyToOne
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
            "}";
    }
}
