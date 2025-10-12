package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanetFeignToDomMapper {

    PlanetDom planetFeignToDom(PlanetResultFeignDto planetResultFeignDto);

    List<PlanetDom> planetFeignListToDom(List<PlanetResultFeignDto> planetResultFeignDtos);
}
