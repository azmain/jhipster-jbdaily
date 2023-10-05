package io.azmain.jb.domain;

import java.io.Serializable;
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
            "}";
    }
}
