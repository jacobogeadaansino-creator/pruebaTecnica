package org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets;

import java.time.OffsetDateTime;
import java.util.List;

public record PlanetResultFeignDto(String name, String rotationPeriod, String orbitalPeriod,
                                   String diameter, String climate, String gravity,
                                   String terrain, String surfaceWater, String population,
                                   List<String> residents, List<String> films,
                                   OffsetDateTime created, OffsetDateTime edited, String url) {
}
