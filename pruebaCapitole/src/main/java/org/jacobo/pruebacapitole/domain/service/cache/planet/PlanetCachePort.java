package org.jacobo.pruebacapitole.domain.service.cache.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;

import java.util.List;
import java.util.Optional;

public interface PlanetCachePort {
    List<PlanetResultDom> findByName(String name);
    Optional<PlanetResultDom> findById(Integer id);
    void save(String name, PlanetResultDom json);
}
