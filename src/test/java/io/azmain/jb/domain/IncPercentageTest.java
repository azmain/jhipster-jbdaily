package io.azmain.jb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncPercentageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncPercentage.class);
        IncPercentage incPercentage1 = new IncPercentage();
        incPercentage1.setId(1L);
        IncPercentage incPercentage2 = new IncPercentage();
        incPercentage2.setId(incPercentage1.getId());
        assertThat(incPercentage1).isEqualTo(incPercentage2);
        incPercentage2.setId(2L);
        assertThat(incPercentage1).isNotEqualTo(incPercentage2);
        incPercentage1.setId(null);
        assertThat(incPercentage1).isNotEqualTo(incPercentage2);
    }
}
