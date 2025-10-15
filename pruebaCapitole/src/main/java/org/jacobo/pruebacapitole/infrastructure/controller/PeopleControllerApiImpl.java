package org.jacobo.pruebacapitole.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.people.PeopleService;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderBy;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderDirection;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPeopleDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PeopleDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleDomDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PeopleControllerApiImpl implements PeopleControllerApi {

    private final PeopleService peopleService;
    private final PeopleDomDtoMapper peopleDomDtoMapper;

    @Override
    public ResponseEntity<PeopleDto> findById(Integer id){
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api";
        val response = peopleService.findById(id);
        return  ResponseEntity.ok(peopleDomDtoMapper.toDtoPeople(response, baseUrl));
    }

    @Override
    public ResponseEntity<PaginatedPeopleDto> findPeopleByName( String name, OrderBy orderBy, OrderDirection order, Integer page){
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api";
        val people = peopleService.findByName(name,
                orderBy != null ? orderBy.getValue() : "asc",
                order != null ? order.getValue() : null,
                page);
        return  ResponseEntity.ok(peopleDomDtoMapper.toDto(people, page, baseUrl));
    }


}
