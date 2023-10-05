package io.azmain.jb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.azmain.jb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyExchangeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyExchange.class);
        MoneyExchange moneyExchange1 = new MoneyExchange();
        moneyExchange1.setId(1L);
        MoneyExchange moneyExchange2 = new MoneyExchange();
        moneyExchange2.setId(moneyExchange1.getId());
        assertThat(moneyExchange1).isEqualTo(moneyExchange2);
        moneyExchange2.setId(2L);
        assertThat(moneyExchange1).isNotEqualTo(moneyExchange2);
        moneyExchange1.setId(null);
        assertThat(moneyExchange1).isNotEqualTo(moneyExchange2);
    }
}
