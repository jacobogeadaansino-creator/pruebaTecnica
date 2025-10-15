package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PeopleIdCacheImplTest {
    private HTreeMap<String, List<PeopleResultDom>> peopleCache;
    private HTreeMap<Integer, PeopleResultDom> peopleIdCache;
    private PeopleIdCacheImpl cacheImpl;
    private DB db;

    @BeforeEach
    void setUp() {
        db = DBMaker.memoryDB().make();
        peopleCache = db.hashMap("peopleCache")
            .keySerializer(org.mapdb.Serializer.STRING)
            .valueSerializer(org.mapdb.Serializer.JAVA)
            .createOrOpen();
        peopleIdCache = db.hashMap("peopleIdCache")
            .keySerializer(org.mapdb.Serializer.INTEGER)
            .valueSerializer(org.mapdb.Serializer.JAVA)
            .createOrOpen();
        cacheImpl = new PeopleIdCacheImpl(peopleCache, peopleIdCache);
    }

    @Test
    void populateCache_emptyCache() throws Exception {
        // No agregamos nada a peopleCache
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(peopleIdCache.isEmpty());
    }

    @Test
    void populateCache_validUrlAndNotExists() throws Exception {
        PeopleResultDom dom = new PeopleResultDom();
        dom.setUrl("https://swapi.dev/api/people/42/");
        peopleCache.put("key1", List.of(dom));
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(peopleIdCache.containsKey(42));
        assertEquals(dom, peopleIdCache.get(42));
    }

    @Test
    void populateCache_validUrlAndAlreadyExists() throws Exception {
        PeopleResultDom dom = new PeopleResultDom();
        dom.setUrl("https://swapi.dev/api/people/99/");
        peopleCache.put("key2", List.of(dom));
        // Pre-cargar el id en la cache
        PeopleResultDom existing = new PeopleResultDom();
        peopleIdCache.put(99, existing);
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertEquals(existing, peopleIdCache.get(99)); // No debe sobrescribir
    }

    @Test
    void populateCache_nullOrEmptyUrl() throws Exception {
        PeopleResultDom dom1 = new PeopleResultDom();
        dom1.setUrl(null);
        PeopleResultDom dom2 = new PeopleResultDom();
        dom2.setUrl("");
        peopleCache.put("key3", List.of(dom1, dom2));
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(peopleIdCache.isEmpty());
    }

    @Test
    void populateCache_invalidUrlNumberFormat() throws Exception {
        PeopleResultDom dom = new PeopleResultDom();
        dom.setUrl("https://swapi.dev/api/people/abc/");
        peopleCache.put("key4", List.of(dom));
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(peopleIdCache.isEmpty());
    }

    @Test
    void getById_exists() {
        PeopleResultDom dom = new PeopleResultDom();
        peopleIdCache.put(7, dom);
        Optional<PeopleResultDom> result = cacheImpl.getById(7);
        assertTrue(result.isPresent());
        assertEquals(dom, result.get());
    }

    @Test
    void getById_notExists() {
        Optional<PeopleResultDom> result = cacheImpl.getById(8);
        assertTrue(result.isEmpty());
    }
}
