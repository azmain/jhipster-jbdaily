package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncPercentageMapperTest {

    private IncPercentageMapper incPercentageMapper;

    @BeforeEach
    public void setUp() {
        incPercentageMapper = new IncPercentageMapperImpl();
    }
}
