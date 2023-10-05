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
    @Mapping(target = "moneyExchange", source = "moneyExchange", qualifiedByName = "moneyExchangeId")
    @Mapping(target = "incPercentage", source = "incPercentage", qualifiedByName = "incPercentageId")
    FrRemittanceDTO toDto(FrRemittance s);

    @Named("moneyExchangeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MoneyExchangeDTO toDtoMoneyExchangeId(MoneyExchange moneyExchange);

    @Named("incPercentageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IncPercentageDTO toDtoIncPercentageId(IncPercentage incPercentage);
}
