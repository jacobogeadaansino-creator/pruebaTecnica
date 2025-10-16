package org.jacobo.pruebacapitole.infrastructure.service.cache.planet;

import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.PlanetSwapiPort;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetIdCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanetCacheLoaderTest {
    @Mock
    private PlanetSwapiPort planetSwapiPort;
    @Mock
    private NameCache<PlanetResultDom> planetCache;
    @Mock
    private PlanetIdCache planetIdCache;
    private PlanetCacheLoader loader;

    @BeforeEach
    void setUp() {
        // El executor real se crea en cada test que lo necesita
    }

    @Test
    void loadCache_firstPageNullOrEmpty() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        loader = new PlanetCacheLoader(planetSwapiPort, planetCache, executor, planetIdCache);
        when(planetSwapiPort.getPlanet()).thenReturn(null);
        loader.loadCache();
        verify(planetCache, never()).put(anyString(), any());
        verify(planetIdCache, never()).populateCache();
        executor.shutdownNow();
    }

    @Test
    void loadCache_firstPageEmptyResults() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        loader = new PlanetCacheLoader(planetSwapiPort, planetCache, executor, planetIdCache);
        PlanetDom dom = new PlanetDom(0L, 1, List.of());
        when(planetSwapiPort.getPlanet()).thenReturn(dom);
        loader.loadCache();
        verify(planetCache, never()).put(anyString(), any());
        verify(planetIdCache, never()).populateCache();
        executor.shutdownNow();
    }

    @Test
    void loadCache_fullFlow() throws InterruptedException {
        // Primera página con 2 resultados, total 4
        PlanetResultDom p1 = new PlanetResultDom("Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000", List.of("res1"), List.of("film1"), null, null, "url1");
        PlanetResultDom p2 = new PlanetResultDom("Alderaan", "24", "364", "12500", "temperate", "1 standard", "grasslands", "40", "2000000000", List.of("res2"), List.of("film2"), null, null, "url2");
        PlanetDom firstPage = new PlanetDom(4L, 1, List.of(p1, p2));
        when(planetSwapiPort.getPlanet()).thenReturn(firstPage);
        // Segunda página con 2 resultados
        PlanetResultDom p3 = new PlanetResultDom("Hoth", "23", "304", "7200", "frozen", "1.1 standard", "tundra", "100", "unknown", List.of("res3"), List.of("film3"), null, null, "url3");
        PlanetResultDom p4 = new PlanetResultDom("Dagobah", "23", "304", "8900", "murky", "N/A", "swamp", "8", "unknown", List.of("res4"), List.of("film4"), null, null, "url4");
        PlanetDom secondPage = new PlanetDom(4L, 2, List.of(p3, p4));
        when(planetSwapiPort.getPlanetByPage(anyInt())).thenReturn(secondPage);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        loader = new PlanetCacheLoader(planetSwapiPort, planetCache, executor, planetIdCache);
        loader.loadCache();
        executor.shutdown();
        boolean finished = executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(finished, "Executor did not finish in time");
        verify(planetCache, atLeast(4)).put(any(), any());
        verify(planetIdCache).populateCache();
    }



}

