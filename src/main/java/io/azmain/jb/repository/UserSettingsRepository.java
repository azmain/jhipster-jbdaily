package io.azmain.jb.repository;

import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.domain.UserSettings;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long>, JpaSpecificationExecutor<UserSettings> {
    Optional<UserSettings> findByCreatedBy(String createdBy);
}
