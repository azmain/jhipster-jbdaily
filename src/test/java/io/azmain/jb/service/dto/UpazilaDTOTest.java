package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UpazilaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UpazilaDTO.class);
        UpazilaDTO upazilaDTO1 = new UpazilaDTO();
        upazilaDTO1.setId(1L);
        UpazilaDTO upazilaDTO2 = new UpazilaDTO();
        assertThat(upazilaDTO1).isNotEqualTo(upazilaDTO2);
        upazilaDTO2.setId(upazilaDTO1.getId());
        assertThat(upazilaDTO1).isEqualTo(upazilaDTO2);
        upazilaDTO2.setId(2L);
        assertThat(upazilaDTO1).isNotEqualTo(upazilaDTO2);
        upazilaDTO1.setId(null);
        assertThat(upazilaDTO1).isNotEqualTo(upazilaDTO2);
    }
}
