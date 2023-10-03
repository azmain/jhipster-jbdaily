package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FertilizerMapperTest {

    private FertilizerMapper fertilizerMapper;

    @BeforeEach
    public void setUp() {
        fertilizerMapper = new FertilizerMapperImpl();
    }
}
