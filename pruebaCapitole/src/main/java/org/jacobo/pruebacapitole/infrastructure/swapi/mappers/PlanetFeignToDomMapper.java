package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanetFeignToDomMapper {

    PlanetDom planetFeignToDom(PlanetFeignDto planetResultFeignDto);

    @Mapping(source = "rotation_period", target = "rotationPeriod")
    @Mapping(source = "orbital_period", target = "orbitalPeriod")
    @Mapping(source = "surface_water", target = "surfaceWater")
    PlanetResultDom planetFeignResultToDom(PlanetResultFeignDto planetResultFeignDtos);
}
