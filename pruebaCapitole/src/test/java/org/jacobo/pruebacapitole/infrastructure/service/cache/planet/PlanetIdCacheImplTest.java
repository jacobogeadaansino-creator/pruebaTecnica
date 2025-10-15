package org.jacobo.pruebacapitole.infrastructure.service.cache.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PlanetIdCacheImplTest {
    private HTreeMap<String, PlanetResultDom> planetNameCache;
    private HTreeMap<Integer, PlanetResultDom> planetIdCache;
    private PlanetIdCacheImpl cacheImpl;
    private DB db;

    @BeforeEach
    void setUp() {
        db = DBMaker.memoryDB().make();
        planetNameCache = db.hashMap("planetNameCache")
            .keySerializer(org.mapdb.Serializer.STRING)
            .valueSerializer(org.mapdb.Serializer.JAVA)
            .createOrOpen();
        planetIdCache = db.hashMap("planetIdCache")
            .keySerializer(org.mapdb.Serializer.INTEGER)
            .valueSerializer(org.mapdb.Serializer.JAVA)
            .createOrOpen();
        cacheImpl = new PlanetIdCacheImpl(planetNameCache, planetIdCache);
    }

    @Test
    void populateCache_emptyCache() throws Exception {
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(planetIdCache.isEmpty());
    }

    @Test
    void populateCache_validUrlAndNotExists() throws Exception {
        PlanetResultDom dom = new PlanetResultDom();
        dom.setUrl("https://swapi.dev/api/planets/42/");
        planetNameCache.put("tatooine", dom);
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(planetIdCache.containsKey(42));
        assertEquals(dom, planetIdCache.get(42));
    }

    @Test
    void populateCache_validUrlAndAlreadyExists() throws Exception {
        PlanetResultDom dom = new PlanetResultDom();
        dom.setUrl("https://swapi.dev/api/planets/99/");
        planetNameCache.put("alderaan", dom);
        PlanetResultDom existing = new PlanetResultDom();
        planetIdCache.put(99, existing);
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertEquals(existing, planetIdCache.get(99)); // No debe sobrescribir
    }

    @Test
    void populateCache_nullOrEmptyUrl() throws Exception {
        PlanetResultDom dom1 = new PlanetResultDom();
        dom1.setUrl(null);
        PlanetResultDom dom2 = new PlanetResultDom();
        dom2.setUrl("");
        planetNameCache.put("hoth", dom1);
        planetNameCache.put("dagobah", dom2);
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(planetIdCache.isEmpty());
    }

    @Test
    void populateCache_invalidUrlNumberFormat() throws Exception {
        PlanetResultDom dom = new PlanetResultDom();
        dom.setUrl("https://swapi.dev/api/planets/abc/");
        planetNameCache.put("bespin", dom);
        CompletableFuture<Void> future = cacheImpl.populateCache();
        future.get();
        assertTrue(planetIdCache.isEmpty());
    }

    @Test
    void getById_exists() {
        PlanetResultDom dom = new PlanetResultDom();
        planetIdCache.put(7, dom);
        Optional<PlanetResultDom> result = cacheImpl.getById(7);
        assertTrue(result.isPresent());
        assertEquals(dom, result.get());
    }

    @Test
    void getById_notExists() {
        Optional<PlanetResultDom> result = cacheImpl.getById(8);
        assertTrue(result.isEmpty());
    }
}

