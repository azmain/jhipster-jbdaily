package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.Upazila} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UpazilaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String bnName;

    private DistrictDTO district;

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

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpazilaDTO)) {
            return false;
        }

        UpazilaDTO upazilaDTO = (UpazilaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, upazilaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpazilaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            ", district=" + getDistrict() +
            "}";
    }
}
