package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DivisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DivisionDTO.class);
        DivisionDTO divisionDTO1 = new DivisionDTO();
        divisionDTO1.setId(1L);
        DivisionDTO divisionDTO2 = new DivisionDTO();
        assertThat(divisionDTO1).isNotEqualTo(divisionDTO2);
        divisionDTO2.setId(divisionDTO1.getId());
        assertThat(divisionDTO1).isEqualTo(divisionDTO2);
        divisionDTO2.setId(2L);
        assertThat(divisionDTO1).isNotEqualTo(divisionDTO2);
        divisionDTO1.setId(null);
        assertThat(divisionDTO1).isNotEqualTo(divisionDTO2);
    }
}
