package org.jacobo.pruebacapitole.infrastructure.service.cache.planet;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetIdCache;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PlanetIdCacheImpl implements PlanetIdCache {

    @Qualifier("planetNameCache")
    private final HTreeMap<String, PlanetResultDom> planetNameCache;
    @Qualifier("planetIdCache")
    private final HTreeMap<Integer, PlanetResultDom> planetIdCache;

    public PlanetIdCacheImpl(@Qualifier("planetNameCache") HTreeMap<String, PlanetResultDom> planetNameCache,
                             @Qualifier("planetIdCache") HTreeMap<Integer, PlanetResultDom> planetIdCache) {
        this.planetNameCache = planetNameCache;
        this.planetIdCache = planetIdCache;
    }

    @Override
    public CompletableFuture<Void> populateCache() {
        return CompletableFuture.runAsync(() ->
            planetNameCache.values().forEach(value -> {
                    String url = value.getUrl();
                    if (!Strings.isNullOrEmpty(url)) {
                        String[] parts = url.split("/");
                        try {
                            Integer id = Integer.valueOf(parts[parts.length - 1]);
                            if (!planetIdCache.containsKey(id)) {
                                planetIdCache.put(id, value);
                            }
                        } catch (NumberFormatException e) {
                            log.error("Error parsing ID from URL: {} error {}", url, e.getMessage());
                        }
                    }
                })
        );
    }

    @Override
    public Optional<PlanetResultDom> getById(Integer id) {
        return planetIdCache.containsKey(id) ? Optional.of(planetIdCache.get(id)): Optional.empty();
    }

}
