package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.service.dto.MoneyExchangeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyExchange} and its DTO {@link MoneyExchangeDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyExchangeMapper extends EntityMapper<MoneyExchangeDTO, MoneyExchange> {}
