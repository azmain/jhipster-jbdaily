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
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    UpazilaDTO toDto(Upazila s);

    @Named("districtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DistrictDTO toDtoDistrictId(District district);
}
