package io.azmain.jb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyExchangeMapperTest {

    private MoneyExchangeMapper moneyExchangeMapper;

    @BeforeEach
    public void setUp() {
        moneyExchangeMapper = new MoneyExchangeMapperImpl();
    }
}
