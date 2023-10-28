package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.District;
import io.azmain.jb.domain.Upazila;
import io.azmain.jb.service.dto.DistrictDTO;
import io.azmain.jb.service.dto.UpazilaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Upazila} and its DTO {@link UpazilaDTO}.
 */
@Mapper(componentModel = "spring")
public interface UpazilaMapper extends EntityMapper<UpazilaDTO, Upazila> {
    @Mapping(target = "district", source = "district", qualifiedByName = "districtName")
    UpazilaDTO toDto(Upazila s);

    @Named("districtName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bnName", source = "bnName")
    DistrictDTO toDtoDistrictName(District district);
}
