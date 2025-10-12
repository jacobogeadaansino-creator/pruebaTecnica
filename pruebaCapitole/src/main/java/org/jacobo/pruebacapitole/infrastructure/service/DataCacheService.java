package org.jacobo.pruebacapitole.infrastructure.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.feign.StarWarsFeignClient;
import org.jacobo.pruebacapitole.application.service.DataCachePort;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleFeignToDomMapper;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetFeignToDomMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataCacheService implements DataCachePort {
    private final StarWarsFeignClient starWarsFeignClient;
    private final PeopleFeignToDomMapper peopleFeignToDomMapper;
    private final PlanetFeignToDomMapper planetFeignToDomMapper;
    private final List<PeopleDom> peopleCache = new ArrayList<>();
    private final List<PlanetDom> planetCache = new ArrayList<>();

    @PostConstruct
    public void init() {
        peopleCache.clear();
        planetCache.clear();
        // Cargar todas las páginas de people
        int page = 1;
        while (true) {
            PeopleFeignDto peoplePage = starWarsFeignClient.getPeopleByPage(page);
            if (peoplePage == null || peoplePage.results() == null || peoplePage.results().isEmpty()) {
                break;
            }
            peopleCache.addAll(peopleFeignToDomMapper.peopleFeignListToDom(peoplePage.results()));
            if (peoplePage.next() == null) {
                break;
            }
            page++;
        }
        // Cargar todas las páginas de planets
        page = 1;
        while (true) {
            PlanetFeignDto planetPage = starWarsFeignClient.getPlanetsByPage(page);
            if (planetPage == null || planetPage.results() == null || planetPage.results().isEmpty()) {
                break;
            }
            planetCache.addAll(planetFeignToDomMapper.planetFeignListToDom(planetPage.results()));
            if (planetPage.next() == null) {
                break;
            }
            page++;
        }
    }

}
