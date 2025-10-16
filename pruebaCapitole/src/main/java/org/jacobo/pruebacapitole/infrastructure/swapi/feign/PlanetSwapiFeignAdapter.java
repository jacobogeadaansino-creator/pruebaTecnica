package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.PlanetSwapiPort;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetFeignToDomMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanetSwapiFeignAdapter implements PlanetSwapiPort {

    private final StarWarsFeignClient starWarsFeignClient;
    private final PlanetFeignToDomMapper planetFeignToDomMapper;

    @Override
    public PlanetDom findByName(String name) {
        val response = starWarsFeignClient.getPlanetByName(name);
        if (response.count() == 0 || response.results().isEmpty()) {
            throw new NotFoundException("There is not result for name {}" + name);
        }
        return planetFeignToDomMapper.planetFeignToDom(response);
    }

    @Override
    public PlanetDom getPlanetByPage(int page) {
        val response = starWarsFeignClient.getPlanetsByPage(page);
        if (response.count() == 0 || response.results().isEmpty()) {
            throw new NotFoundException("There is not result for page {}" + page);
        }
        return planetFeignToDomMapper.planetFeignToDom(response);
    }

    @Override
    public PlanetResultDom findById(Long id) {
        val response = starWarsFeignClient.getPlanetById(id);
        return planetFeignToDomMapper.planetFeignResultToDom(response);
    }

    @Override
    public PlanetDom getPlanet() {
        return planetFeignToDomMapper.planetFeignToDom(starWarsFeignClient.getPlanets());
    }

}
