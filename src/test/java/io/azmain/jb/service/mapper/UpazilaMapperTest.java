package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpazilaMapperTest {

    private UpazilaMapper upazilaMapper;

    @BeforeEach
    public void setUp() {
        upazilaMapper = new UpazilaMapperImpl();
    }
}
