package org.jacobo.pruebacapitole.application.service.people;

import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleCacheRepository;
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
class PeopleServiceImplTest {

    @Mock
    private PeopleSwapiService peopleSwapiService;
    @Mock
    private PeopleCacheRepository peopleCacheRepository;

    private EntitySorter<PeopleResultDom> peopleSorter;
    private PeopleServiceImpl peopleService;
    private PeopleResultDom luke;
    private PeopleResultDom leia;
    private PeopleResultDom han;

    @BeforeEach
    void setUp() {
        peopleSorter = new GenericEntitySorter<>(Map.of(
                "created", PeopleResultDom::getCreated,
                "name", PeopleResultDom::getName
        ));
        peopleService = new PeopleServiceImpl(peopleSwapiService, peopleSorter, peopleCacheRepository);

        luke = new PeopleResultDom(
                "Luke", null, null, null, null, null, null, null, null,
                null, null, null, null,
                OffsetDateTime.parse("2020-01-01T00:00:00Z"), null, null
        );
        leia = new PeopleResultDom(
                "Leia", null, null, null, null, null, null, null, null,
                null, null, null, null,
                OffsetDateTime.parse("2021-01-01T00:00:00Z"), null, null
        );
        han = new PeopleResultDom(
                "Han", null, null, null, null, null, null, null, null,
                null, null, null, null,
                OffsetDateTime.parse("2019-01-01T00:00:00Z"), null, null
        );
    }

    @Test
    void findByName_cacheHit_orderByNameAsc() {
        List<PeopleResultDom> people = Arrays.asList(leia, luke, han);
        when(peopleCacheRepository.findByName("all")).thenReturn(people);
        PeopleDom result = peopleService.findByName("all", "name", "asc", 1);
        assertEquals(3L, result.count());
        assertEquals("Han", result.results().get(0).getName());
        assertEquals("Leia", result.results().get(1).getName());
        assertEquals("Luke", result.results().get(2).getName());
    }

    @Test
    void findByName_cacheHit_orderByCreatedDesc() {
        List<PeopleResultDom> people = Arrays.asList(leia, luke, han);
        when(peopleCacheRepository.findByName("all")).thenReturn(people);
        PeopleDom result = peopleService.findByName("all", "created", "desc", 1);
        assertEquals(3L, result.count());
        assertEquals("Leia", result.results().get(0).getName());
        assertEquals("Luke", result.results().get(1).getName());
        assertEquals("Han", result.results().get(2).getName());
    }

    @Test
    void findByName_cacheHit_orderByNull() {
        List<PeopleResultDom> people = Arrays.asList(leia, luke, han);
        when(peopleCacheRepository.findByName("all")).thenReturn(people);
        PeopleDom result = peopleService.findByName("all", null, null, 1);
        assertEquals(3L, result.count());
        assertNotNull(result.results());
    }

    @Test
    void findByName_cacheHit_paginationOutOfRange() {
        List<PeopleResultDom> people = Arrays.asList(leia, luke, han);
        when(peopleCacheRepository.findByName("all")).thenReturn(people);
        PeopleDom result = peopleService.findByName("all", "name", "asc", 2);
        assertTrue(result.results().isEmpty());
    }

    @Test
    void findByName_cacheMiss_apiHit() {
        when(peopleCacheRepository.findByName("Luke")).thenReturn(Collections.emptyList());
        PeopleDom swapiResponse = new PeopleDom(1L, 1, List.of(luke));
        when(peopleSwapiService.findByName("Luke")).thenReturn(swapiResponse);

        PeopleDom result = peopleService.findByName("Luke", "name", "asc", 1);
        assertEquals(1L, result.count());
        verify(peopleCacheRepository).save(eq("Luke"), any());
    }

    @Test
    void findByName_cacheMiss_apiNoResults() {
        when(peopleCacheRepository.findByName("HanSolo")).thenReturn(Collections.emptyList());
        PeopleDom swapiResponse = new PeopleDom(0L, 1, Collections.emptyList());
        when(peopleSwapiService.findByName("HanSolo")).thenReturn(swapiResponse);

        assertThrows(NotFoundException.class, () -> peopleService.findByName("HanSolo", "name", "asc", 1));
    }

    @Test
    void findById_cacheHit() {
        when(peopleCacheRepository.findById(1)).thenReturn(Optional.of(luke));
        PeopleResultDom result = peopleService.findById(1);
        assertEquals("Luke", result.getName());
    }

    @Test
    void findById_cacheMiss_apiHit() {
        when(peopleCacheRepository.findById(2)).thenReturn(Optional.empty());
        when(peopleSwapiService.findById(2L)).thenReturn(leia);

        PeopleResultDom result = peopleService.findById(2);
        assertEquals("Leia", result.getName());
        verify(peopleCacheRepository).save(eq("Leia"), any());
    }

    @Test
    void findById_cacheMiss_apiNoResult() {
        when(peopleCacheRepository.findById(3)).thenReturn(Optional.empty());
        when(peopleSwapiService.findById(3L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> peopleService.findById(3));
    }
}
