package org.jacobo.pruebacapitole.infrastructure.controller;

import org.jacobo.pruebacapitole.application.service.planet.PlanetService;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPlanetDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PlanetDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetDomDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PlanetControllerApiImpl.class)
class PlanetControllerApiImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlanetService planetService;
    @MockBean
    private PlanetDomDtoMapper planetDomDtoMapper;

    @Test
    void findPlanetByName_ok() throws Exception {
        PlanetDom dom = new PlanetDom(1L, 1, List.of(new PlanetResultDom()));
        PaginatedPlanetDto dto = new PaginatedPlanetDto();
        Mockito.when(planetService.findByName(anyString(), anyString(), anyString(), anyInt())).thenReturn(dom);
        Mockito.when(planetDomDtoMapper.toDto(any(), anyInt(), anyString())).thenReturn(dto);
        mockMvc.perform(get("/api/planets")
                .param("name", "Tatooine")
                .param("orderBy", "name")
                .param("order", "asc")
                .param("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findPlanetById_ok() throws Exception {
        PlanetResultDom dom = new PlanetResultDom();
        PlanetDto dto = new PlanetDto();
        Mockito.when(planetService.findById(anyInt())).thenReturn(dom);
        Mockito.when(planetDomDtoMapper.toDtoPlanet(any(), anyString())).thenReturn(dto);
        mockMvc.perform(get("/api/planets/1/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findPlanetByName_notFound() throws Exception {
        Mockito.when(planetService.findByName(anyString(), anyString(), anyString(), anyInt()))
                .thenThrow(new org.jacobo.pruebacapitole.domain.exception.NotFoundException("No planet found"));
        mockMvc.perform(get("/api/planets")
                .param("name", "Unknown")
                .param("orderBy", "name")
                .param("order", "asc")
                .param("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findPlanetById_notFound() throws Exception {
        Mockito.when(planetService.findById(anyInt()))
                .thenThrow(new org.jacobo.pruebacapitole.domain.exception.NotFoundException("No planet found"));
        mockMvc.perform(get("/api/planets/999/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
