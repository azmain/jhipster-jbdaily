package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.Fertilizer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FertilizerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String bnName;

    @NotNull
    @Size(max = 255)
    private String accountNo;

    @NotNull
    @Size(max = 255)
    private String accountTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return bnName;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FertilizerDTO)) {
            return false;
        }

        FertilizerDTO fertilizerDTO = (FertilizerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fertilizerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FertilizerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", accountNo='" + getAccountNo() + "'" +
            ", accountTitle='" + getAccountTitle() + "'" +
            "}";
    }
}
