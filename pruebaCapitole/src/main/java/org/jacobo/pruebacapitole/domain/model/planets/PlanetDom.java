package org.jacobo.pruebacapitole.domain.model.planets;

import java.util.List;

public record PlanetDom(
        Long count,
        Integer page,
        List<PlanetResultDom> results
) {
}
