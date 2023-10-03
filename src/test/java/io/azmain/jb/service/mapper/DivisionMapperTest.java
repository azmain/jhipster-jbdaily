package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DivisionMapperTest {

    private DivisionMapper divisionMapper;

    @BeforeEach
    public void setUp() {
        divisionMapper = new DivisionMapperImpl();
    }
}
