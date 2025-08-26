// package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;
package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // pon ERROR cuando quieras estricto
)
public interface CentralMapperConfig {}