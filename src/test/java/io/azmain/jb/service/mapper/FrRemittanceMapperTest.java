package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrRemittanceMapperTest {

    private FrRemittanceMapper frRemittanceMapper;

    @BeforeEach
    public void setUp() {
        frRemittanceMapper = new FrRemittanceMapperImpl();
    }
}
