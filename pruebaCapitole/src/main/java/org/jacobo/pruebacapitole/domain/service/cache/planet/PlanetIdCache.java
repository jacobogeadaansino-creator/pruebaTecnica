package org.jacobo.pruebacapitole.domain.service.cache.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PlanetIdCache {

    CompletableFuture<Void> populateCache();
    Optional<PlanetResultDom> getById(Integer id);
}
