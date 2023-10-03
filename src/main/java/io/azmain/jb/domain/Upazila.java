package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Upazila.
 */
@Entity
@Table(name = "upazila")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Upazila implements Serializable {

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

    @OneToMany(mappedBy = "upazila")
    @JsonIgnoreProperties(value = { "upazila" }, allowSetters = true)
    private Set<Dealer> dealers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "upazilas", "division" }, allowSetters = true)
    private District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Upazila id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Upazila name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return this.bnName;
    }

    public Upazila bnName(String bnName) {
        this.setBnName(bnName);
        return this;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public Set<Dealer> getDealers() {
        return this.dealers;
    }

    public void setDealers(Set<Dealer> dealers) {
        if (this.dealers != null) {
            this.dealers.forEach(i -> i.setUpazila(null));
        }
        if (dealers != null) {
            dealers.forEach(i -> i.setUpazila(this));
        }
        this.dealers = dealers;
    }

    public Upazila dealers(Set<Dealer> dealers) {
        this.setDealers(dealers);
        return this;
    }

    public Upazila addDealer(Dealer dealer) {
        this.dealers.add(dealer);
        dealer.setUpazila(this);
        return this;
    }

    public Upazila removeDealer(Dealer dealer) {
        this.dealers.remove(dealer);
        dealer.setUpazila(null);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazila district(District district) {
        this.setDistrict(district);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upazila)) {
            return false;
        }
        return id != null && id.equals(((Upazila) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Upazila{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            "}";
    }
}
