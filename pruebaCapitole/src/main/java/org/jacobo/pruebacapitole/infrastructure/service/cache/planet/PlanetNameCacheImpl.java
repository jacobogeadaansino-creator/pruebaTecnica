package org.jacobo.pruebacapitole.infrastructure.service.cache.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetNameCacheImpl implements NameCache<PlanetResultDom> {
    @Qualifier("planetNameCache")
    private final HTreeMap<String, PlanetResultDom> planetNameCache;

    public PlanetNameCacheImpl(@Qualifier("planetNameCache") HTreeMap<String, PlanetResultDom> planetNameCache) {
        this.planetNameCache = planetNameCache;
    }

    @Override
    public Optional<PlanetResultDom> find(String name) {
        return planetNameCache.containsKey(name) ? Optional.ofNullable(planetNameCache.get(name)) : Optional.empty();
    }

    @Override
    public void put(String name, PlanetResultDom json) {
        planetNameCache.put(name, json);
    }

    public boolean containsKey(String name) {
        return planetNameCache.containsKey(name);
    }

    @Override
    public int size() {
        return planetNameCache.size();
    }

    @Override
    public List<String> getAllKeys() {
        return planetNameCache.keySet().stream().toList();
    }
}
