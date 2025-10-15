package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleFeignToDomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleSwapiFeignAdapterTest {
    @Mock
    private StarWarsFeignClient starWarsFeignClient;
    @Mock
    private PeopleFeignToDomMapper peopleFeignToDomMapper;
    @InjectMocks
    private PeopleSwapiFeignAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PeopleSwapiFeignAdapter(starWarsFeignClient, peopleFeignToDomMapper);
    }

    @Test
    void findByName_ok() {
        PeopleFeignDto feignDto = new PeopleFeignDto(1L, null, null, List.of(new PeopleResultFeignDto("Luke", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)));
        PeopleDom dom = new PeopleDom(1L, 1, List.of(new PeopleResultDom()));
        when(starWarsFeignClient.getPeopleByName("Luke")).thenReturn(feignDto);
        when(peopleFeignToDomMapper.peopleFeignToDom(feignDto)).thenReturn(dom);
        PeopleDom result = adapter.findByName("Luke");
        assertEquals(1L, result.count());
    }

    @Test
    void findByName_notFound() {
        PeopleFeignDto feignDto = new PeopleFeignDto(0L, null, null, Collections.emptyList());
        when(starWarsFeignClient.getPeopleByName("Unknown")).thenReturn(feignDto);
        assertThrows(NotFoundException.class, () -> adapter.findByName("Unknown"));
    }

    @Test
    void getPeopleByPage_ok() {
        PeopleFeignDto feignDto = new PeopleFeignDto(1L, null, null, List.of(new PeopleResultFeignDto("Luke", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)));
        PeopleDom dom = new PeopleDom(1L, 1, List.of(new PeopleResultDom()));
        when(starWarsFeignClient.getPeopleByPage(1)).thenReturn(feignDto);
        when(peopleFeignToDomMapper.peopleFeignToDom(feignDto)).thenReturn(dom);
        PeopleDom result = adapter.getPeopleByPage(1);
        assertEquals(1L, result.count());
    }

    @Test
    void getPeopleByPage_notFound() {
        PeopleFeignDto feignDto = new PeopleFeignDto(0L, null, null, Collections.emptyList());
        when(starWarsFeignClient.getPeopleByPage(2)).thenReturn(feignDto);
        assertThrows(NotFoundException.class, () -> adapter.getPeopleByPage(2));
    }

    @Test
    void findById_ok() {
        PeopleResultFeignDto feignDto = new PeopleResultFeignDto("Luke", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        PeopleResultDom dom = new PeopleResultDom();
        when(starWarsFeignClient.getPeopleById(1L)).thenReturn(feignDto);
        when(peopleFeignToDomMapper.peopleFeignToDom(feignDto)).thenReturn(dom);
        PeopleResultDom result = adapter.findById(1L);
        assertNotNull(result);
    }

    @Test
    void getPeople_ok() {
        PeopleFeignDto feignDto = new PeopleFeignDto(1L, null, null, List.of(new PeopleResultFeignDto("Luke", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)));
        PeopleDom dom = new PeopleDom(1L, 1, List.of(new PeopleResultDom()));
        when(starWarsFeignClient.getPeople()).thenReturn(feignDto);
        when(peopleFeignToDomMapper.peopleFeignToDom(feignDto)).thenReturn(dom);
        PeopleDom result = adapter.getPeople();
        assertEquals(1L, result.count());
    }
}
