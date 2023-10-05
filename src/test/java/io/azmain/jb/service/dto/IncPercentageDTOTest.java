package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncPercentageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncPercentageDTO.class);
        IncPercentageDTO incPercentageDTO1 = new IncPercentageDTO();
        incPercentageDTO1.setId(1L);
        IncPercentageDTO incPercentageDTO2 = new IncPercentageDTO();
        assertThat(incPercentageDTO1).isNotEqualTo(incPercentageDTO2);
        incPercentageDTO2.setId(incPercentageDTO1.getId());
        assertThat(incPercentageDTO1).isEqualTo(incPercentageDTO2);
        incPercentageDTO2.setId(2L);
        assertThat(incPercentageDTO1).isNotEqualTo(incPercentageDTO2);
        incPercentageDTO1.setId(null);
        assertThat(incPercentageDTO1).isNotEqualTo(incPercentageDTO2);
    }
}
