package org.jacobo.pruebacapitole.domain.service.cache.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PeopleIdCache {

    CompletableFuture<Void> populateCache();
    Optional<PeopleResultDom> getById(Integer id);
}
