package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayOrderDTO.class);
        PayOrderDTO payOrderDTO1 = new PayOrderDTO();
        payOrderDTO1.setId(1L);
        PayOrderDTO payOrderDTO2 = new PayOrderDTO();
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
        payOrderDTO2.setId(payOrderDTO1.getId());
        assertThat(payOrderDTO1).isEqualTo(payOrderDTO2);
        payOrderDTO2.setId(2L);
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
        payOrderDTO1.setId(null);
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
    }
}
