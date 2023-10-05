package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.IncPercentage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncPercentageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncPercentageDTO)) {
            return false;
        }

        IncPercentageDTO incPercentageDTO = (IncPercentageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, incPercentageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncPercentageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
