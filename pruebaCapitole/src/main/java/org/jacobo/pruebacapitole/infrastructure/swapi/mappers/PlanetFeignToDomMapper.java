package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanetFeignToDomMapper {

    PlanetDom planetFeignToDom(PlanetFeignDto planetResultFeignDto);

    PlanetResultDom planetFeignResultToDom(PlanetResultFeignDto planetResultFeignDtos);
}
