package io.azmain.jb.service.impl;

import io.azmain.jb.config.Constants;
import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.domain.UserSettings;
import io.azmain.jb.repository.UserSettingsRepository;
import io.azmain.jb.security.SpringSecurityAuditorAware;
import io.azmain.jb.service.UserSettingsService;
import io.azmain.jb.service.dto.UserSettingsDTO;
import io.azmain.jb.service.mapper.UserSettingsMapper;
import io.azmain.jb.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
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

    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    public UserSettingsServiceImpl(
        UserSettingsRepository userSettingsRepository,
        UserSettingsMapper userSettingsMapper,
        SpringSecurityAuditorAware springSecurityAuditorAware
    ) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
    }

    @Override
    public UserSettingsDTO save(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to save UserSettings : {}", userSettingsDTO);
        String user = springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM);
        if (userSettingsRepository.findByCreatedBy(user).isPresent()) {
            throw new BadRequestAlertException("A userSettings already exists", "UserSettings", user);
        }
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings.setCreatedBy(user);
        userSettings.createdDate(Instant.now());
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public UserSettingsDTO update(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to update UserSettings : {}", userSettingsDTO);
        UserSettings persistUserSettings = userSettingsRepository
            .findById(userSettingsDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "userSettings", "idnotfound"));

        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);

        userSettings.setCreatedDate(persistUserSettings.getCreatedDate());
        userSettings.setCreatedBy(persistUserSettings.getCreatedBy());
        userSettings.setLastModifiedDate(Instant.now());
        userSettings.setLastModifiedBy(springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM));

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
    @Transactional(readOnly = true)
    public Optional<UserSettingsDTO> findByUser(String user) {
        log.debug("Request to get UserSettings by user : {}", user);
        return userSettingsRepository.findByCreatedBy(user).map(userSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSettings : {}", id);
        userSettingsRepository.deleteById(id);
    }

    @Override
    public void updatePayOrderNumSeqAndControlNum(String payOrderNumSeq, String payOrderControlNum) {
        log.debug("Request to update UserSettings On PayOrder Creation : {} {}", payOrderNumSeq, payOrderControlNum);

        String user = springSecurityAuditorAware.getCurrentAuditor().orElse(Constants.SYSTEM);
        UserSettings userSettings = userSettingsRepository.findByCreatedBy(user).orElse(new UserSettings());

        if (userSettings.getId() == null) {
            userSettings.setName(user);
            userSettings.setCreatedBy(user);
            userSettings.setCreatedDate(Instant.now());
        }
        userSettings.setPayOrderNumSeq(payOrderNumSeq);
        userSettings.setPayOrderControlNum(payOrderControlNum);
        userSettingsRepository.save(userSettings);
    }
}
