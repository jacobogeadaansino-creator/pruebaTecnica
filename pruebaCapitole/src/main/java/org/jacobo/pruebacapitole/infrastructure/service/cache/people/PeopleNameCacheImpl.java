package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleNameCacheImpl implements NameCache<PeopleResultDom> {
    @Qualifier("peopleNameCache")
    private final HTreeMap<String, PeopleResultDom> peopleNameCache;

    public PeopleNameCacheImpl(@Qualifier("peopleNameCache") HTreeMap<String, PeopleResultDom> peopleNameCache) {
        this.peopleNameCache = peopleNameCache;
    }

    @Override
    public Optional<PeopleResultDom> find(String name) {
        return peopleNameCache.containsKey(name) ? Optional.of(peopleNameCache.get(name)) : Optional.empty();
    }

    @Override
    public void put(String name, PeopleResultDom json) {
        peopleNameCache.put(name, json);
    }

    public boolean containsKey(String name) {
        return peopleNameCache.containsKey(name);
    }

    @Override
    public int size() {
        return peopleNameCache.size();
    }

    @Override
    public List<String> getAllKeys() {
        return peopleNameCache.keySet().stream().toList();
    }
}
