package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,              // @Autowired por ctor
        unmappedTargetPolicy = ReportingPolicy.IGNORE,                  // o ERROR si quieres estricto
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, // PATCH: ignora nulls
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS          // siempre chequea null
)
public interface CentralMapperConfig {}