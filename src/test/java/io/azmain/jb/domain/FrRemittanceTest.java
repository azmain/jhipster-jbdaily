package io.azmain.jb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrRemittanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrRemittance.class);
        FrRemittance frRemittance1 = new FrRemittance();
        frRemittance1.setId(1L);
        FrRemittance frRemittance2 = new FrRemittance();
        frRemittance2.setId(frRemittance1.getId());
        assertThat(frRemittance1).isEqualTo(frRemittance2);
        frRemittance2.setId(2L);
        assertThat(frRemittance1).isNotEqualTo(frRemittance2);
        frRemittance1.setId(null);
        assertThat(frRemittance1).isNotEqualTo(frRemittance2);
    }
}
