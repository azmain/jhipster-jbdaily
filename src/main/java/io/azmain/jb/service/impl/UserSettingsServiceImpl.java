package io.azmain.jb.service.impl;

import io.azmain.jb.domain.UserSettings;
import io.azmain.jb.repository.UserSettingsRepository;
import io.azmain.jb.service.UserSettingsService;
import io.azmain.jb.service.dto.UserSettingsDTO;
import io.azmain.jb.service.mapper.UserSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserSettings}.
 */
@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final Logger log = LoggerFactory.getLogger(UserSettingsServiceImpl.class);

    private final UserSettingsRepository userSettingsRepository;

    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsServiceImpl(UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
    }

    @Override
    public UserSettingsDTO save(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to save UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public UserSettingsDTO update(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to update UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public Optional<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to partially update UserSettings : {}", userSettingsDTO);

        return userSettingsRepository
            .findById(userSettingsDTO.getId())
            .map(existingUserSettings -> {
                userSettingsMapper.partialUpdate(existingUserSettings, userSettingsDTO);

                return existingUserSettings;
            })
            .map(userSettingsRepository::save)
            .map(userSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSettings");
        return userSettingsRepository.findAll(pageable).map(userSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSettingsDTO> findOne(Long id) {
        log.debug("Request to get UserSettings : {}", id);
        return userSettingsRepository.findById(id).map(userSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSettings : {}", id);
        userSettingsRepository.deleteById(id);
    }
}
