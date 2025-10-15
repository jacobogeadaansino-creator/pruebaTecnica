package org.jacobo.pruebacapitole.infrastructure.config;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MapDbConfig {

    @Bean
    public DB mapDb() {
        return DBMaker.memoryDB().make();
    }

    @Bean("peopleCache")
    public HTreeMap<String, List<PeopleResultDom>> peopleCache(DB mapDb) {
        return mapDb.hashMap("peopleCache")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }

    @Bean("peopleNameCache")
    public HTreeMap<String, PeopleResultDom> peopleNameCache(DB mapDb) {
        return mapDb.hashMap("peopleCache")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }

    @Bean("peopleIdCache")
    public HTreeMap<Integer, PeopleResultDom> peopleIdCache(DB mapDb) {
        return mapDb.hashMap("peopleIdCache")
                .keySerializer(Serializer.INTEGER)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }

    @Bean("planetNameCache")
    public HTreeMap<String, PlanetResultDom> planetNameCache(DB mapDb) {
        return mapDb.hashMap("planetNameCache")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }

    @Bean("planetIdCache")
    public HTreeMap<Integer, PlanetResultDom> planetIdCache(DB mapDb) {
        return mapDb.hashMap("planetIdCache")
                .keySerializer(Serializer.INTEGER)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }

    @Bean("planetCache")
    public HTreeMap<String, List<PlanetResultDom>> planetCache(DB mapDb) {
        return mapDb.hashMap("planetCache")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
    }
}
