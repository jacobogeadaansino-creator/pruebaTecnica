package org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets;

import java.time.OffsetDateTime;
import java.util.List;

public record PlanetResultFeignDto(String name, String rotation_period, String orbital_period,
                                   String diameter, String climate, String gravity,
                                   String terrain, String surface_water, String population,
                                   List<String> residents, List<String> films,
                                   OffsetDateTime created, OffsetDateTime edited, String url) {
}
