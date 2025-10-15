package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleIdCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PeopleCacheLoaderTest {

    @Mock
    private PeopleSwapiService peopleSwapiService;
    @Mock
    private NameCache<PeopleResultDom> peopleCache;
    @Mock
    private ExecutorService executor;
    @Mock
    private PeopleIdCache peopleIdCache;

    private PeopleCacheLoader loader;

    @BeforeEach
    void setUp() {
        loader = new PeopleCacheLoader(peopleSwapiService, peopleCache, executor, peopleIdCache);
    }

    @Test
    void loadCache_firstPageNullOrEmpty() {
        when(peopleSwapiService.getPeople()).thenReturn(null);
        loader.loadCache();
        verify(peopleCache, never()).put(anyString(), any());
        verify(peopleIdCache, never()).populateCache();
    }

    @Test
    void loadCache_firstPageEmptyResults() {
        PeopleDom dom = new PeopleDom(0L, 1, List.of());
        when(peopleSwapiService.getPeople()).thenReturn(dom);
        loader.loadCache();
        verify(peopleCache, never()).put(anyString(), any());
        verify(peopleIdCache, never()).populateCache();
    }

    @Test
    void loadCache_fullFlow() throws InterruptedException {
        // Primera página con 2 resultados, total 4
        PeopleResultDom p1 = new PeopleResultDom("Luke", "172", "77", "blond", "fair", "blue", "19BBY", "male", "Tatooine", List.of("film1"), List.of("species1"), List.of("vehicle1"), List.of("starship1"), null, null, "url1");
        PeopleResultDom p2 = new PeopleResultDom("Leia", "150", "49", "brown", "light", "brown", "19BBY", "female", "Alderaan", List.of("film2"), List.of("species2"), List.of("vehicle2"), List.of("starship2"), null, null, "url2");
        PeopleDom firstPage = new PeopleDom(4L, 1, List.of(p1, p2));
        when(peopleSwapiService.getPeople()).thenReturn(firstPage);
        // Segunda página con 2 resultados
        PeopleResultDom p3 = new PeopleResultDom("Han", "180", "80", "brown", "light", "brown", "29BBY", "male", "Corellia", List.of("film3"), List.of("species3"), List.of("vehicle3"), List.of("starship3"), null, null, "url3");
        PeopleResultDom p4 = new PeopleResultDom("Yoda", "66", "17", "white", "green", "green", "896BBY", "male", "Dagobah", List.of("film4"), List.of("species4"), List.of("vehicle4"), List.of("starship4"), null, null, "url4");
        PeopleDom secondPage = new PeopleDom(4L, 2, List.of(p3, p4));
        when(peopleSwapiService.getPeopleByPage(anyInt())).thenReturn(secondPage);
        // Executor real
        java.util.concurrent.ExecutorService realExecutor = java.util.concurrent.Executors.newFixedThreadPool(2);
        loader = new PeopleCacheLoader(peopleSwapiService, peopleCache, realExecutor, peopleIdCache);
        loader.loadCache();
        realExecutor.shutdown();
        boolean finished = realExecutor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(finished, "Executor did not finish in time");
        // Verifica que se guardan todos los nombres
        verify(peopleCache, atLeast(4)).put(any(), any());
        verify(peopleIdCache).populateCache();
    }

}
