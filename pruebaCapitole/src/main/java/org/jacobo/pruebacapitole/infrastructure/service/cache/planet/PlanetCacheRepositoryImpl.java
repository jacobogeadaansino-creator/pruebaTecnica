package org.jacobo.pruebacapitole.infrastructure.service.cache.planet;

import lombok.RequiredArgsConstructor;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetCacheRepository;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetIdCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanetCacheRepositoryImpl implements PlanetCacheRepository {

    private final NameCache<PlanetResultDom> planetNameCache;
    private final PlanetIdCache planetIdCache;
    @Override
    public Optional<PlanetResultDom> findByNameExact(String name) {
        return Optional.empty();
    }

    @Override
    public List<PlanetResultDom> findByName(String name) {
        String search = name == null ? "" : name.toLowerCase();
        return  planetNameCache.getAllKeys().stream()
                .filter(key -> key != null && key.toLowerCase().contains(search))
                .flatMap(key -> planetNameCache.find(key).stream()).collect(Collectors.toList());
    }

    @Override
    public List<PlanetResultDom> findAll() {
        return  planetNameCache.getAllKeys().stream()
                .flatMap(key ->  planetNameCache.find(key).stream())
                .collect(Collectors.toList());
    }
    @Override
    public Optional<PlanetResultDom> findById(Integer id) {
        return planetIdCache.getById(id);
    }

    @Override
    public void save(String name, PlanetResultDom json) {
        planetNameCache.put(name, json);
    }

    }




