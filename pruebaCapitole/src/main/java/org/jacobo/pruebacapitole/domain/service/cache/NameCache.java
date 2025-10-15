package org.jacobo.pruebacapitole.domain.service.cache;

import java.util.List;
import java.util.Optional;

public interface NameCache<T>{
    Optional<T> find(String name);
    void put(String name, T json);
    int size();
    List<String> getAllKeys();
}
