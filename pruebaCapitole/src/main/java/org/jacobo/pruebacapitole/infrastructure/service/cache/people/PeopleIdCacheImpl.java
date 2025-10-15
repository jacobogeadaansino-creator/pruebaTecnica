package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleIdCache;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PeopleIdCacheImpl implements PeopleIdCache {

    @Qualifier("peopleCache")
    private final HTreeMap<String, List<PeopleResultDom>> peopleCache;
    @Qualifier("peopleIdCache")
    private final HTreeMap<Integer, PeopleResultDom> peopleIdCache;

    public PeopleIdCacheImpl(@Qualifier("peopleCache") HTreeMap<String, List<PeopleResultDom>> peopleCache,
                             @Qualifier("peopleIdCache") HTreeMap<Integer, PeopleResultDom> peopleIdCache) {
        this.peopleCache = peopleCache;
        this.peopleIdCache = peopleIdCache;
    }

    @Override
    public CompletableFuture<Void> populateCache() {
        return CompletableFuture.runAsync(() -> {
            peopleCache.values().forEach(value -> {
                value.forEach(people -> {
                    String url = people.getUrl();
                    if (!Strings.isNullOrEmpty(url)) {
                        String[] parts = url.split("/");
                        try {
                            Integer id = Integer.valueOf(parts[parts.length - 1]);
                            if (!peopleIdCache.containsKey(id)) {
                                peopleIdCache.put(id, people);
                            }
                        } catch (NumberFormatException e) {
                            log.error("Error parsing ID from URL: {} error {}", url, e.getMessage());
                        }
                    }
                });
            });
        });
    }

    @Override
    public Optional<PeopleResultDom> getById(Integer id) {
        return peopleIdCache.containsKey(id) ? Optional.of(peopleIdCache.get(id)): Optional.empty();
    }

    @Override
    public boolean containsId(Integer id) {
        return peopleIdCache.containsKey(id);
    }
}
