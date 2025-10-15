package org.jacobo.pruebacapitole.infrastructure.controller;

import org.jacobo.pruebacapitole.application.service.people.PeopleService;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPeopleDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PeopleDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleDomDtoMapper;
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

@WebMvcTest(PeopleControllerApiImpl.class)
class PeopleControllerApiImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PeopleService peopleService;
    @MockBean
    private PeopleDomDtoMapper peopleDomDtoMapper;

    @Test
    void findPeopleByName_ok() throws Exception {
        PeopleDom dom = new PeopleDom(1L, 1, List.of(new PeopleResultDom()));
        PaginatedPeopleDto dto = new PaginatedPeopleDto();
        Mockito.when(peopleService.findByName(anyString(), anyString(), anyString(), anyInt())).thenReturn(dom);
        Mockito.when(peopleDomDtoMapper.toDto(any(), anyInt(), anyString())).thenReturn(dto);
        mockMvc.perform(get("/api/people")
                .param("name", "Luke")
                .param("orderBy", "name")
                .param("order", "asc")
                .param("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ok() throws Exception {
        PeopleResultDom dom = new PeopleResultDom();
        PeopleDto dto = new PeopleDto();
        Mockito.when(peopleService.findById(anyInt())).thenReturn(dom);
        Mockito.when(peopleDomDtoMapper.toDtoPeople(any(), anyString())).thenReturn(dto);
        mockMvc.perform(get("/api/people/1/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findPeopleByName_notFound() throws Exception {
        Mockito.when(peopleService.findByName(anyString(), anyString(), anyString(), anyInt()))
                .thenThrow(new org.jacobo.pruebacapitole.domain.exception.NotFoundException("No people found"));
        mockMvc.perform(get("/api/people")
                .param("name", "Unknown")
                .param("orderBy", "name")
                .param("order", "asc")
                .param("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_notFound() throws Exception {
        Mockito.when(peopleService.findById(anyInt()))
                .thenThrow(new org.jacobo.pruebacapitole.domain.exception.NotFoundException("No people found"));
        mockMvc.perform(get("/api/people/999/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
