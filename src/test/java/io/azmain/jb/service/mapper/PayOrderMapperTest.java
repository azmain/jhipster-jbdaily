package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayOrderMapperTest {

    private PayOrderMapper payOrderMapper;

    @BeforeEach
    public void setUp() {
        payOrderMapper = new PayOrderMapperImpl();
    }
}
