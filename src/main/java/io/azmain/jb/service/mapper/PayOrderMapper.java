package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.*;
import io.azmain.jb.service.dto.*;
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
    @Mapping(target = "bnName", source = "bnName")
    @Mapping(target = "accountNo", source = "accountNo")
    @Mapping(target = "accountTitle", source = "accountTitle")
    FertilizerDTO toDtoFertilizerName(Fertilizer fertilizer);

    @Named("dealerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    @Mapping(target = "shortName", source = "shortName")
    @Mapping(target = "upazila", source = "upazila", qualifiedByName = "upazilaName")
    DealerDTO toDtoDealerName(Dealer dealer);

    @Named("upazilaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtName")
    UpazilaDTO toDtoUpazilaName(Upazila upazila);

    @Named("districtName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    @Mapping(target = "division", source = "division", qualifiedByName = "divisionName")
    DistrictDTO toDtoDistrictName(District district);

    @Named("divisionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    DivisionDTO toDtoDivisionName(Division division);
}
