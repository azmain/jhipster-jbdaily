package io.azmain.jb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MoneyExchange.
 */
@Entity
@Table(name = "money_exchange")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyExchange implements Serializable {

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
    @Size(max = 26)
    @Column(name = "digit", length = 26, nullable = false)
    private String digit;

    @Size(max = 256)
    @Column(name = "link", length = 256)
    private String link;

    @Size(max = 256)
    @Column(name = "short_name", length = 256)
    private String shortName;

    @OneToMany(mappedBy = "moneyExchange")
    @JsonIgnoreProperties(value = { "moneyExchange", "incPercentage" }, allowSetters = true)
    private Set<FrRemittance> frRemittances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyExchange id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MoneyExchange name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigit() {
        return this.digit;
    }

    public MoneyExchange digit(String digit) {
        this.setDigit(digit);
        return this;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getLink() {
        return this.link;
    }

    public MoneyExchange link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortName() {
        return this.shortName;
    }

    public MoneyExchange shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Set<FrRemittance> getFrRemittances() {
        return this.frRemittances;
    }

    public void setFrRemittances(Set<FrRemittance> frRemittances) {
        if (this.frRemittances != null) {
            this.frRemittances.forEach(i -> i.setMoneyExchange(null));
        }
        if (frRemittances != null) {
            frRemittances.forEach(i -> i.setMoneyExchange(this));
        }
        this.frRemittances = frRemittances;
    }

    public MoneyExchange frRemittances(Set<FrRemittance> frRemittances) {
        this.setFrRemittances(frRemittances);
        return this;
    }

    public MoneyExchange addFrRemittance(FrRemittance frRemittance) {
        this.frRemittances.add(frRemittance);
        frRemittance.setMoneyExchange(this);
        return this;
    }

    public MoneyExchange removeFrRemittance(FrRemittance frRemittance) {
        this.frRemittances.remove(frRemittance);
        frRemittance.setMoneyExchange(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyExchange)) {
            return false;
        }
        return id != null && id.equals(((MoneyExchange) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyExchange{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", digit='" + getDigit() + "'" +
            ", link='" + getLink() + "'" +
            ", shortName='" + getShortName() + "'" +
            "}";
    }
}
