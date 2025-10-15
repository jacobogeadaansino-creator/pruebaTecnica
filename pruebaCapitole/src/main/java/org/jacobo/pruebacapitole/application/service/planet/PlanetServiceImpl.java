package org.jacobo.pruebacapitole.application.service.planet;


import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.commons.EntitySorter;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.PlanetSwapiService;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetCacheRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanetServiceImpl  implements PlanetService {

    private final PlanetSwapiService planetSwapiService;
    private final PlanetCacheRepository planetCacheRepository;
    private final EntitySorter<PlanetResultDom> planetSorter;
    private static final int PAGE_SIZE = 15;

    @Override
    public PlanetDom findByName(String name, String orderBy, String order, Integer page) {
        if(Strings.isNullOrEmpty(order)){
            order = "asc";
        }
        List<PlanetResultDom> result = planetCacheRepository.findByName(name);
        if(result.isEmpty()){
            log.info("cache fails, going to api");
            val swapiResponse = planetSwapiService.findByName(name);
            if (swapiResponse == null || swapiResponse.results() == null || swapiResponse.results().isEmpty()) {
                throw new NotFoundException(String.format("There is not result for name %s", name));
            }
            result = swapiResponse.results();
            planetCacheRepository.save(name, swapiResponse.results().get(0));
        }

        planetSorter.sort(result, orderBy, order);

        int startIndex = (page - 1) * PAGE_SIZE;
        if (startIndex >= result.size()) {
            return new PlanetDom((long) result.size(), page, Collections.emptyList());
        }
        int endIndex = Math.min(startIndex + PAGE_SIZE, result.size());
        List<PlanetResultDom> pagedResult = result.subList(startIndex, endIndex);

        return new PlanetDom((long) result.size(), page, pagedResult);
    }

    @Override
    public PlanetResultDom findById(Integer id) {
        val cacheResult =  planetCacheRepository.findById(id);
        if(cacheResult.isEmpty()){
            log.error("Cache fails, going to api" );
            val response = planetSwapiService.findById(Long.valueOf(id));
            if(response == null) {
                throw new NotFoundException(String.format("There is not result for id %d", id));
            }
            planetCacheRepository.save(response.getName(), response);
            return response;
        }
        return cacheResult.get();
    }
}
