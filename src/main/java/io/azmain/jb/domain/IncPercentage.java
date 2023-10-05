package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A IncPercentage.
 */
@Entity
@Table(name = "inc_percentage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncPercentage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @OneToMany(mappedBy = "incPercentage")
    @JsonIgnoreProperties(value = { "moneyExchange", "incPercentage" }, allowSetters = true)
    private Set<FrRemittance> frRemittances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncPercentage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IncPercentage name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FrRemittance> getFrRemittances() {
        return this.frRemittances;
    }

    public void setFrRemittances(Set<FrRemittance> frRemittances) {
        if (this.frRemittances != null) {
            this.frRemittances.forEach(i -> i.setIncPercentage(null));
        }
        if (frRemittances != null) {
            frRemittances.forEach(i -> i.setIncPercentage(this));
        }
        this.frRemittances = frRemittances;
    }

    public IncPercentage frRemittances(Set<FrRemittance> frRemittances) {
        this.setFrRemittances(frRemittances);
        return this;
    }

    public IncPercentage addFrRemittance(FrRemittance frRemittance) {
        this.frRemittances.add(frRemittance);
        frRemittance.setIncPercentage(this);
        return this;
    }

    public IncPercentage removeFrRemittance(FrRemittance frRemittance) {
        this.frRemittances.remove(frRemittance);
        frRemittance.setIncPercentage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncPercentage)) {
            return false;
        }
        return id != null && id.equals(((IncPercentage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncPercentage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
