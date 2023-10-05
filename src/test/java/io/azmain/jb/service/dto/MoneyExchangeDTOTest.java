package io.azmain.jb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyExchangeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyExchangeDTO.class);
        MoneyExchangeDTO moneyExchangeDTO1 = new MoneyExchangeDTO();
        moneyExchangeDTO1.setId(1L);
        MoneyExchangeDTO moneyExchangeDTO2 = new MoneyExchangeDTO();
        assertThat(moneyExchangeDTO1).isNotEqualTo(moneyExchangeDTO2);
        moneyExchangeDTO2.setId(moneyExchangeDTO1.getId());
        assertThat(moneyExchangeDTO1).isEqualTo(moneyExchangeDTO2);
        moneyExchangeDTO2.setId(2L);
        assertThat(moneyExchangeDTO1).isNotEqualTo(moneyExchangeDTO2);
        moneyExchangeDTO1.setId(null);
        assertThat(moneyExchangeDTO1).isNotEqualTo(moneyExchangeDTO2);
    }
}
