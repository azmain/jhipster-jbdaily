package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.District;
import io.azmain.jb.domain.Division;
import io.azmain.jb.service.dto.DistrictDTO;
import io.azmain.jb.service.dto.DivisionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link District} and its DTO {@link DistrictDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {
    @Mapping(target = "division", source = "division", qualifiedByName = "divisionName")
    DistrictDTO toDto(District s);

    @Named("divisionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    DivisionDTO toDtoDivisionName(Division division);
}
