package org.jacobo.pruebacapitole.application.service.commons;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class SorterConfig {
    @Bean
    public EntitySorter<PeopleResultDom> peopleSorter() {
        Map<String, Function<PeopleResultDom, Comparable<?>>> peopleFields = Map.of(
                "created", PeopleResultDom::getCreated,
                "name", PeopleResultDom::getName
        );
        return new GenericEntitySorter<>(peopleFields);
    }

    @Bean
    public EntitySorter<PlanetResultDom> planetSorter() {
        Map<String, Function<PlanetResultDom, Comparable<?>>> planetFields = Map.of(
                "created", PlanetResultDom::getCreated,
                "name", PlanetResultDom::getName
        );
        return new GenericEntitySorter<>(planetFields);
    }
}
