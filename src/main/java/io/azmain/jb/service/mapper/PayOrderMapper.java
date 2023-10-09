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
    @Mapping(target = "fertilizer", source = "fertilizer", qualifiedByName = "fertilizerId")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerId")
    PayOrderDTO toDto(PayOrder s);

    @Named("fertilizerId")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FertilizerDTO toDtoFertilizerId(Fertilizer fertilizer);

    @Named("dealerId")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DealerDTO toDtoDealerId(Dealer dealer);
}
