package org.jacobo.pruebacapitole.domain.service;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;

public interface PlanetSwapiService {

    PlanetDom findByName(String name);

    PlanetDom getPlanetByPage(int page);

    PlanetResultDom findById(Long id);

    PlanetDom getPlanet();
}
