package io.azmain.jb.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.Dealer} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String bnName;

    @NotNull
    @Size(max = 255)
    private String shortName;

    @Size(max = 255)
    private String mobile;

    private UpazilaDTO upazila;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UpazilaDTO getUpazila() {
        return upazila;
    }

    public void setUpazila(UpazilaDTO upazila) {
        this.upazila = upazila;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealerDTO)) {
            return false;
        }

        DealerDTO dealerDTO = (DealerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dealerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", upazila=" + getUpazila() +
            "}";
    }
}
