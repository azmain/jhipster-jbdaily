package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.UserSettings;
import io.azmain.jb.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {}
