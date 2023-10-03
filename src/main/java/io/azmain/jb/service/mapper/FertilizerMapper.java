package io.azmain.jb.service.mapper;

import io.azmain.jb.domain.Fertilizer;
import io.azmain.jb.service.dto.FertilizerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fertilizer} and its DTO {@link FertilizerDTO}.
 */
@Mapper(componentModel = "spring")
public interface FertilizerMapper extends EntityMapper<FertilizerDTO, Fertilizer> {}
