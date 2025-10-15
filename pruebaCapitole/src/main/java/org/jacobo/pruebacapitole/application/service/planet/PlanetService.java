package org.jacobo.pruebacapitole.application.service.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;

public interface PlanetService {
    PlanetDom findByName(String name, String orderBy, String order, Integer page);

    PlanetResultDom findById(Integer id);

}
