package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.planets.PlanetResultFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetFeignToDomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PlanetSwapiFeignAdapterTest {
    @Mock
    private StarWarsFeignClient starWarsFeignClient;
    @Mock
    private PlanetFeignToDomMapper planetFeignToDomMapper;
    @InjectMocks
    private PlanetSwapiFeignAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new PlanetSwapiFeignAdapter(starWarsFeignClient, planetFeignToDomMapper);
    }

    @Test
    void findByName_ok() {
        PlanetResultFeignDto resultFeignDto = new PlanetResultFeignDto(
            "Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000",
            List.of("Luke"), List.of("A New Hope"),
            OffsetDateTime.parse("2020-01-01T00:00:00Z"), OffsetDateTime.parse("2020-01-02T00:00:00Z"), "url"
        );
        PlanetFeignDto feignDto = new PlanetFeignDto(1L, null, null, List.of(resultFeignDto));
        PlanetDom dom = new PlanetDom(1L, 1, List.of(new PlanetResultDom()));
        when(starWarsFeignClient.getPlanetByName("Tatooine")).thenReturn(feignDto);
        when(planetFeignToDomMapper.planetFeignToDom(feignDto)).thenReturn(dom);
        PlanetDom result = adapter.findByName("Tatooine");
        assertEquals(1L, result.count());
    }

    @Test
    void findByName_notFound() {
        PlanetFeignDto feignDto = new PlanetFeignDto(0L, null, null, Collections.emptyList());
        when(starWarsFeignClient.getPlanetByName("Unknown")).thenReturn(feignDto);
        assertThrows(NotFoundException.class, () -> adapter.findByName("Unknown"));
    }

    @Test
    void getPlanetByPage_ok() {
        PlanetResultFeignDto resultFeignDto = new PlanetResultFeignDto(
            "Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000",
            List.of("Luke"), List.of("A New Hope"),
            OffsetDateTime.parse("2020-01-01T00:00:00Z"), OffsetDateTime.parse("2020-01-02T00:00:00Z"), "url"
        );
        PlanetFeignDto feignDto = new PlanetFeignDto(1L, null, null, List.of(resultFeignDto));
        PlanetDom dom = new PlanetDom(1L, 1, List.of(new PlanetResultDom()));
        when(starWarsFeignClient.getPlanetsByPage(1)).thenReturn(feignDto);
        when(planetFeignToDomMapper.planetFeignToDom(feignDto)).thenReturn(dom);
        PlanetDom result = adapter.getPlanetByPage(1);
        assertEquals(1L, result.count());
    }

    @Test
    void getPlanetByPage_notFound() {
        PlanetFeignDto feignDto = new PlanetFeignDto(0L, null, null, Collections.emptyList());
        when(starWarsFeignClient.getPlanetsByPage(2)).thenReturn(feignDto);
        assertThrows(NotFoundException.class, () -> adapter.getPlanetByPage(2));
    }

    @Test
    void findById_ok() {
        PlanetResultFeignDto feignDto = new PlanetResultFeignDto(
            "Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000",
            List.of("Luke"), List.of("A New Hope"),
            OffsetDateTime.parse("2020-01-01T00:00:00Z"), OffsetDateTime.parse("2020-01-02T00:00:00Z"), "url"
        );
        PlanetResultDom dom = new PlanetResultDom();
        when(starWarsFeignClient.getPlanetById(1L)).thenReturn(feignDto);
        when(planetFeignToDomMapper.planetFeignResultToDom(feignDto)).thenReturn(dom);
        PlanetResultDom result = adapter.findById(1L);
        assertNotNull(result);
    }

    @Test
    void getPlanet_ok() {
        PlanetResultFeignDto resultFeignDto = new PlanetResultFeignDto(
            "Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000",
            List.of("Luke"), List.of("A New Hope"),
            OffsetDateTime.parse("2020-01-01T00:00:00Z"), OffsetDateTime.parse("2020-01-02T00:00:00Z"), "url"
        );
        PlanetFeignDto feignDto = new PlanetFeignDto(1L, null, null, List.of(resultFeignDto));
        PlanetDom dom = new PlanetDom(1L, 1, List.of(new PlanetResultDom()));
        when(starWarsFeignClient.getPlanets()).thenReturn(feignDto);
        when(planetFeignToDomMapper.planetFeignToDom(feignDto)).thenReturn(dom);
        PlanetDom result = adapter.getPlanet();
        assertEquals(1L, result.count());
    }
}
