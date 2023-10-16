package io.azmain.jb.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.azmain.jb.domain.UserSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String name;

    @NotNull
    private String payOrderNumSeq;

    @NotNull
    private String payOrderControlNum;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public String getPayOrderNumSeq() {
        return payOrderNumSeq;
    }

    public void setPayOrderNumSeq(String payOrderNumSeq) {
        this.payOrderNumSeq = payOrderNumSeq;
    }

    public String getPayOrderControlNum() {
        return payOrderControlNum;
    }

    public void setPayOrderControlNum(String payOrderControlNum) {
        this.payOrderControlNum = payOrderControlNum;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSettingsDTO)) {
            return false;
        }

        UserSettingsDTO userSettingsDTO = (UserSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSettingsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", payOrderNumSeq='" + getPayOrderNumSeq() + "'" +
            ", payOrderControlNum='" + getPayOrderControlNum() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
