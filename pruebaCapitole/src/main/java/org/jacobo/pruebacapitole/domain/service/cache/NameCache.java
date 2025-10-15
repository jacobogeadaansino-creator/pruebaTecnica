package org.jacobo.pruebacapitole.domain.service.cache;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

import java.util.List;
import java.util.Optional;

public interface NameCache<T>{
    Optional<T> find(String name);
    void put(String name, T json);
    void replace(String name, T json);
    boolean containsKey(String name);
    int size();
    List<String> getAllKeys();
}
