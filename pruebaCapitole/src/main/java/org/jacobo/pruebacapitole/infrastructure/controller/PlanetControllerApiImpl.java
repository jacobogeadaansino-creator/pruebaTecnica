package org.jacobo.pruebacapitole.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.planet.PlanetService;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderBy;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderDirection;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPlanetDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PlanetDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PlanetDomDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlanetControllerApiImpl implements PlanetControllerApi {

    private final PlanetService planetService;
    private final PlanetDomDtoMapper planetDomDtoMapper;

    @Override
    public ResponseEntity<PaginatedPlanetDto> findPlanetByName(String name, OrderBy orderBy, OrderDirection order, Integer page) {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        val response = planetService.findByName(name,
                orderBy != null ? orderBy.getValue() : "asc",
                order != null ? order.getValue() : null,
                page);
        return ResponseEntity.ok(planetDomDtoMapper.toDto(response, page, baseUrl));
    }

    @Override
    public ResponseEntity<PlanetDto> findPlanetById(Integer id) {
        val response = planetService.findById(id);
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return ResponseEntity.ok(planetDomDtoMapper.toDtoPlanet(response, baseUrl));
    }

}
