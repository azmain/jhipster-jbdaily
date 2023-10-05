package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.Division} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DivisionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String bnName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DivisionDTO)) {
            return false;
        }

        DivisionDTO divisionDTO = (DivisionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, divisionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DivisionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bnName='" + getBnName() + "'" +
            "}";
    }
}
