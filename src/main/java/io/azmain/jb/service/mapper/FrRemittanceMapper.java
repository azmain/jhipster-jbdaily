package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.FrRemittance;
import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.domain.MoneyExchange;
import io.azmain.jb.service.dto.FrRemittanceDTO;
import io.azmain.jb.service.dto.IncPercentageDTO;
import io.azmain.jb.service.dto.MoneyExchangeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FrRemittance} and its DTO {@link FrRemittanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface FrRemittanceMapper extends EntityMapper<FrRemittanceDTO, FrRemittance> {
    @Mapping(target = "moneyExchange", source = "moneyExchange", qualifiedByName = "moneyExchangeName")
    @Mapping(target = "incPercentage", source = "incPercentage", qualifiedByName = "incPercentageName")
    FrRemittanceDTO toDto(FrRemittance s);

    @Named("moneyExchangeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MoneyExchangeDTO toDtoMoneyExchangeName(MoneyExchange moneyExchange);

    @Named("incPercentageName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IncPercentageDTO toDtoIncPercentageName(IncPercentage incPercentage);
}
