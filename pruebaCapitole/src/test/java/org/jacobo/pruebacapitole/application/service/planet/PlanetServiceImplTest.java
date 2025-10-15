package org.jacobo.pruebacapitole.application.service.planet;

import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.domain.service.PlanetSwapiService;
import org.jacobo.pruebacapitole.domain.service.cache.planet.PlanetCacheRepository;
import org.jacobo.pruebacapitole.application.service.commons.EntitySorter;
import org.jacobo.pruebacapitole.application.service.commons.GenericEntitySorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanetServiceImplTest {

    @Mock
    private PlanetSwapiService planetSwapiService;
    @Mock
    private PlanetCacheRepository planetCacheRepository;

    private PlanetServiceImpl planetService;
    private PlanetResultDom tatooine;
    private PlanetResultDom alderaan;
    private PlanetResultDom hoth;
    private EntitySorter<PlanetResultDom> planetSorter;

    @BeforeEach
    void setUp() {
        // Crear sorter real con los campos de ordenación
        planetSorter = new GenericEntitySorter<>(Map.of(
                "created", PlanetResultDom::getCreated,
                "name", PlanetResultDom::getName
        ));
        planetService = new PlanetServiceImpl(planetSwapiService, planetCacheRepository, planetSorter);

        tatooine = new PlanetResultDom(
                "Tatooine", null, null, null, null, null, null, null, null,
                null, null,
                OffsetDateTime.parse("2020-01-01T00:00:00Z"), null, null
        );
        alderaan = new PlanetResultDom(
                "Alderaan", null, null, null, null, null, null, null, null,
                null, null,
                OffsetDateTime.parse("2021-01-01T00:00:00Z"), null, null
        );
        hoth = new PlanetResultDom(
                "Hoth", null, null, null, null, null, null, null, null,
                null, null,
                OffsetDateTime.parse("2019-01-01T00:00:00Z"), null, null
        );
    }

    @Test
    void findByName_cacheHit_orderByNameAsc() {
        List<PlanetResultDom> planets = Arrays.asList(alderaan, tatooine, hoth);
        when(planetCacheRepository.findByName("all")).thenReturn(planets);
        PlanetDom result = planetService.findByName("all", "name", "asc", 1);
        assertEquals(3L, result.count());
        assertEquals("Alderaan", result.results().get(0).getName());
        assertEquals("Hoth", result.results().get(1).getName());
        assertEquals("Tatooine", result.results().get(2).getName());
    }

    @Test
    void findByName_cacheHit_orderByCreatedDesc() {
        List<PlanetResultDom> planets = Arrays.asList(alderaan, tatooine, hoth);
        when(planetCacheRepository.findByName("all")).thenReturn(planets);
        PlanetDom result = planetService.findByName("all", "created", "desc", 1);
        assertEquals(3L, result.count());
        assertEquals("Alderaan", result.results().get(0).getName());
        assertEquals("Tatooine", result.results().get(1).getName());
        assertEquals("Hoth", result.results().get(2).getName());
    }

    @Test
    void findByName_cacheHit_orderByNull() {
        List<PlanetResultDom> planets = Arrays.asList(alderaan, tatooine, hoth);
        when(planetCacheRepository.findByName("all")).thenReturn(planets);
        PlanetDom result = planetService.findByName("all", null, null, 1);
        assertEquals(3L, result.count());
        assertNotNull(result.results());
    }

    @Test
    void findByName_cacheHit_paginationOutOfRange() {
        List<PlanetResultDom> planets = Arrays.asList(alderaan, tatooine, hoth);
        when(planetCacheRepository.findByName("all")).thenReturn(planets);
        PlanetDom result = planetService.findByName("all", "name", "asc", 2);
        assertTrue(result.results().isEmpty());
    }

    @Test
    void findByName_cacheMiss_apiHit() {
        when(planetCacheRepository.findByName("Tatooine")).thenReturn(Collections.emptyList());
        PlanetDom swapiResponse = new PlanetDom(1L, 1, List.of(tatooine));
        when(planetSwapiService.findByName("Tatooine")).thenReturn(swapiResponse);

        PlanetDom result = planetService.findByName("Tatooine", "name", "asc", 1);
        assertEquals(1L, result.count());
        verify(planetCacheRepository).save(eq("Tatooine"), any());
    }

    @Test
    void findByName_cacheMiss_apiNoResults() {
        when(planetCacheRepository.findByName("Dagobah")).thenReturn(Collections.emptyList());
        PlanetDom swapiResponse = new PlanetDom(0L, 1, Collections.emptyList());
        when(planetSwapiService.findByName("Dagobah")).thenReturn(swapiResponse);

        assertThrows(NotFoundException.class, () -> planetService.findByName("Dagobah", "name", "asc", 1));
    }

    @Test
    void findById_cacheHit() {
        when(planetCacheRepository.findById(1)).thenReturn(Optional.of(tatooine));
        PlanetResultDom result = planetService.findById(1);
        assertEquals("Tatooine", result.getName());
    }

    @Test
    void findById_cacheMiss_apiHit() {
        when(planetCacheRepository.findById(2)).thenReturn(Optional.empty());
        when(planetSwapiService.findById(2L)).thenReturn(alderaan);

        PlanetResultDom result = planetService.findById(2);
        assertEquals("Alderaan", result.getName());
        verify(planetCacheRepository).save(eq("Alderaan"), any());
    }

    @Test
    void findById_cacheMiss_apiNoResult() {
        when(planetCacheRepository.findById(3)).thenReturn(Optional.empty());
        when(planetSwapiService.findById(3L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> planetService.findById(3));
    }
}