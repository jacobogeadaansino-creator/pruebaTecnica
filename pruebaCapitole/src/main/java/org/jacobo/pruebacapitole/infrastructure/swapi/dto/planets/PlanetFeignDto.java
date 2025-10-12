package org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets;

import java.util.List;

public record PlanetFeignDto(Long count, String next, String previous, List<PlanetResultFeignDto> results){}


