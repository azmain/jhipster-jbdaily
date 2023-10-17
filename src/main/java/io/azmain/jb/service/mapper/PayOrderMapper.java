package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.Dealer;
import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.service.dto.DealerDTO;
import io.azmain.jb.service.dto.FertilizerDTO;
import io.azmain.jb.service.dto.PayOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayOrder} and its DTO {@link PayOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayOrderMapper extends EntityMapper<PayOrderDTO, PayOrder> {
    @Mapping(target = "fertilizer", source = "fertilizer", qualifiedByName = "fertilizerName")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    PayOrderDTO toDto(PayOrder s);

    @Named("fertilizerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FertilizerDTO toDtoFertilizerName(Fertilizer fertilizer);

    @Named("dealerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "shortName", source = "shortName")
    DealerDTO toDtoDealerName(Dealer dealer);
}
