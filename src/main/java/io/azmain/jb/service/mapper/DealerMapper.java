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
    @Mapping(target = "upazila", source = "upazila", qualifiedByName = "upazilaId")
    DealerDTO toDto(Dealer s);

    @Named("upazilaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UpazilaDTO toDtoUpazilaId(Upazila upazila);
}
