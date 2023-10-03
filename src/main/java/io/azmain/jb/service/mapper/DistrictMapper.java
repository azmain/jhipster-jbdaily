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
    @Mapping(target = "division", source = "division", qualifiedByName = "divisionId")
    DistrictDTO toDto(District s);

    @Named("divisionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DivisionDTO toDtoDivisionId(Division division);
}
