package org.jacobo.pruebacapitole.application.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;

import java.util.Map;

public interface DataCachePort {
    Map<String, PeopleDom> getPeopleCache();
    Map<String, PlanetDom> getPlanetCache();
}

