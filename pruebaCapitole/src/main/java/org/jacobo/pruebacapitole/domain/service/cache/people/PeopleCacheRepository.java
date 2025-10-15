package org.jacobo.pruebacapitole.domain.service.cache.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

import java.util.List;
import java.util.Optional;

public interface PeopleCacheRepository {
    List<PeopleResultDom> findByName(String name);
    Optional<PeopleResultDom> findById(Integer id);
    void save(String name, PeopleResultDom json);
}
