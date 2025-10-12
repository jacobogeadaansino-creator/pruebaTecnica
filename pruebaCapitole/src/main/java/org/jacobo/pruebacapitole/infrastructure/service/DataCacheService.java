package org.jacobo.pruebacapitole.infrastructure.service;

import com.google.common.base.Strings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.DataCachePort;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.feign.StarWarsFeignClient;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleFeignToDomMapper;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetFeignToDomMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataCacheService implements DataCachePort {
    private final StarWarsFeignClient starWarsFeignClient;
    private final PeopleFeignToDomMapper peopleFeignToDomMapper;
    private final PlanetFeignToDomMapper planetFeignToDomMapper;
    private final Map<String, PeopleDom> peopleCache = new HashMap<>();
    private final Map<String, PlanetDom> planetCache = new HashMap<>();

    @PostConstruct
    public void init() {
        peopleCache.clear();
        planetCache.clear();
        log.info("Loading people and planets data into cache...");
        int page = 1;
        boolean continueLoading = true;
        addToPeopleCache(peopleCache, peopleFeignToDomMapper.peopleFeignListToDom(starWarsFeignClient.getPeople().results()));
        while (continueLoading) {
            val peoplePage = starWarsFeignClient.getPeopleByPage(++page);
            if (Objects.isNull(peoplePage) || Objects.isNull(peoplePage.results()) || peoplePage.results().isEmpty()) {
                continueLoading = false;
            }
            addToPeopleCache(peopleCache, peopleFeignToDomMapper.peopleFeignListToDom(peoplePage.results()));
            if (Strings.isNullOrEmpty(peoplePage.next())) {
                continueLoading = false;
            }
        }
        // Cargar todas las páginas de planets
        page = 1;
        continueLoading = true;
        addToPlanetCache(planetCache, planetFeignToDomMapper.planetFeignListToDom(starWarsFeignClient.getPlanets().results()));
        while (continueLoading) {
            val planetPage = starWarsFeignClient.getPlanetsByPage(++page);
            if (Objects.isNull(planetPage) || Objects.isNull(planetPage.results()) || planetPage.results().isEmpty()) {
                continueLoading = false;
            }
            addToPlanetCache(planetCache, planetFeignToDomMapper.planetFeignListToDom(planetPage.results()));
            if (Strings.isNullOrEmpty(planetPage.next())) {
                continueLoading = false;
            }
        }
    }

    private void addToPeopleCache(Map<String, PeopleDom> peopleCache, List<PeopleDom> peopleDoms) {
        peopleDoms.forEach(peopleDom ->
                peopleCache.putIfAbsent(peopleDom.getName().toLowerCase(), peopleDom)
        );
    }

    private void addToPlanetCache(Map<String, PlanetDom> planetCache, List<PlanetDom> planetDoms) {
        planetDoms.forEach(planetDom ->
                planetCache.putIfAbsent(planetDom.getName().toLowerCase(), planetDom)
        );
    }

    @Override
    public Map<String, PeopleDom> getPeopleCache() {
        return peopleCache;
    }

    @Override
    public Map<String, PlanetDom> getPlanetCache() {
        return planetCache;
    }
}
