package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import org.jacobo.pruebacapitole.infrastructure.config.FeignConfig;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "StarWarsFeignClient", url = "${base.url:https://swapi.dev/api}", configuration = FeignConfig.class)
public interface StarWarsFeignClient {

    @GetMapping(path = "/people/", produces = "application/json")
    PeopleFeignDto getPeople();

    @GetMapping(path = "/people/", produces = "application/json")
    PeopleFeignDto getPeopleByPage(@RequestParam(name = "page") int page);

    @GetMapping(path = "/planets/", produces = "application/json")
    PlanetFeignDto getPlanets();

    @GetMapping(path = "/planets/", produces = "application/json")
    PlanetFeignDto getPlanetsByPage(@RequestParam(name = "page") int page);

    @GetMapping(path = "/planets/{id}", produces = "application/json")
    PlanetResultFeignDto getPlanetById(@PathVariable(name = "id") Long id);

    @GetMapping(path = "/people/{id}", produces = "application/json")
    PeopleResultFeignDto getPeopleById(@PathVariable(name = "id") Long id);



}
