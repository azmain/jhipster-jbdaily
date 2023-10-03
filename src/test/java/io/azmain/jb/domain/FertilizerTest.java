package io.azmain.jb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FertilizerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fertilizer.class);
        Fertilizer fertilizer1 = new Fertilizer();
        fertilizer1.setId(1L);
        Fertilizer fertilizer2 = new Fertilizer();
        fertilizer2.setId(fertilizer1.getId());
        assertThat(fertilizer1).isEqualTo(fertilizer2);
        fertilizer2.setId(2L);
        assertThat(fertilizer1).isNotEqualTo(fertilizer2);
        fertilizer1.setId(null);
        assertThat(fertilizer1).isNotEqualTo(fertilizer2);
    }
}
