package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSettingsMapperTest {

    private UserSettingsMapper userSettingsMapper;

    @BeforeEach
    public void setUp() {
        userSettingsMapper = new UserSettingsMapperImpl();
    }
}
