package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FertilizerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FertilizerDTO.class);
        FertilizerDTO fertilizerDTO1 = new FertilizerDTO();
        fertilizerDTO1.setId(1L);
        FertilizerDTO fertilizerDTO2 = new FertilizerDTO();
        assertThat(fertilizerDTO1).isNotEqualTo(fertilizerDTO2);
        fertilizerDTO2.setId(fertilizerDTO1.getId());
        assertThat(fertilizerDTO1).isEqualTo(fertilizerDTO2);
        fertilizerDTO2.setId(2L);
        assertThat(fertilizerDTO1).isNotEqualTo(fertilizerDTO2);
        fertilizerDTO1.setId(null);
        assertThat(fertilizerDTO1).isNotEqualTo(fertilizerDTO2);
    }
}
