package io.azmain.jb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayOrder.class);
        PayOrder payOrder1 = new PayOrder();
        payOrder1.setId(1L);
        PayOrder payOrder2 = new PayOrder();
        payOrder2.setId(payOrder1.getId());
        assertThat(payOrder1).isEqualTo(payOrder2);
        payOrder2.setId(2L);
        assertThat(payOrder1).isNotEqualTo(payOrder2);
        payOrder1.setId(null);
        assertThat(payOrder1).isNotEqualTo(payOrder2);
    }
}
