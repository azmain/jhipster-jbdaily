package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrRemittanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrRemittanceDTO.class);
        FrRemittanceDTO frRemittanceDTO1 = new FrRemittanceDTO();
        frRemittanceDTO1.setId(1L);
        FrRemittanceDTO frRemittanceDTO2 = new FrRemittanceDTO();
        assertThat(frRemittanceDTO1).isNotEqualTo(frRemittanceDTO2);
        frRemittanceDTO2.setId(frRemittanceDTO1.getId());
        assertThat(frRemittanceDTO1).isEqualTo(frRemittanceDTO2);
        frRemittanceDTO2.setId(2L);
        assertThat(frRemittanceDTO1).isNotEqualTo(frRemittanceDTO2);
        frRemittanceDTO1.setId(null);
        assertThat(frRemittanceDTO1).isNotEqualTo(frRemittanceDTO2);
    }
}
