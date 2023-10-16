package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.Dealer;
import io.azmain.jb.domain.Upazila;
import io.azmain.jb.service.dto.DealerDTO;
import io.azmain.jb.service.dto.UpazilaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dealer} and its DTO {@link DealerDTO}.
 */
@Mapper(componentModel = "spring")
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {
    @Mapping(target = "upazila", source = "upazila", qualifiedByName = "upazilaName")
    DealerDTO toDto(Dealer s);

    @Named("upazilaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UpazilaDTO toDtoUpazilaName(Upazila upazila);
}
