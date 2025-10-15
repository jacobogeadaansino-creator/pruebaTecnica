package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import lombok.RequiredArgsConstructor;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleCacheRepository;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleIdCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleCacheRepositoryImpl implements PeopleCacheRepository {

    private final NameCache<PeopleResultDom> peopleNameCache;
    private final PeopleIdCache peopleIdCache;


    @Override
    public List<PeopleResultDom> findByName(String name) {
        String search = name == null ? "" : name.toLowerCase();
        return  peopleNameCache.getAllKeys().stream()
                .filter(key -> key != null && key.toLowerCase().contains(search))
                .flatMap(key -> peopleNameCache.find(key).stream()).collect(Collectors.toList());
    }


    @Override
    public Optional<PeopleResultDom> findById(Integer id) {
        return peopleIdCache.getById(id);
    }

    @Override
    public void save(String name, PeopleResultDom json) {
        peopleNameCache.put(name, json);
    }


}
