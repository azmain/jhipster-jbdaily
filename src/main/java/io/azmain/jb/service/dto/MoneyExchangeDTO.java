package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.MoneyExchange} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyExchangeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String name;

    @NotNull
    @Size(max = 26)
    private String digit;

    @Size(max = 256)
    private String link;

    @Size(max = 256)
    private String shortName;

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

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyExchangeDTO)) {
            return false;
        }

        MoneyExchangeDTO moneyExchangeDTO = (MoneyExchangeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyExchangeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyExchangeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", digit='" + getDigit() + "'" +
            ", link='" + getLink() + "'" +
            ", shortName='" + getShortName() + "'" +
            "}";
    }
}
