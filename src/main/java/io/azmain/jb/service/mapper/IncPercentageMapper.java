package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.IncPercentage;
import io.azmain.jb.service.dto.IncPercentageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncPercentage} and its DTO {@link IncPercentageDTO}.
 */
@Mapper(componentModel = "spring")
public interface IncPercentageMapper extends EntityMapper<IncPercentageDTO, IncPercentage> {}
