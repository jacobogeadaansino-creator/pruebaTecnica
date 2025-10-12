package org.jacobo.pruebacapitole.application.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import java.util.List;

public interface DataCachePort {
    List<PeopleDom> getPeopleCache();
    List<PlanetDom> getPlanetCache();
}

