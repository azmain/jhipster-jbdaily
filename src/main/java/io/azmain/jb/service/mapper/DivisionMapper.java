package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.Division;
import io.azmain.jb.service.dto.DivisionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Division} and its DTO {@link DivisionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DivisionMapper extends EntityMapper<DivisionDTO, Division> {}
