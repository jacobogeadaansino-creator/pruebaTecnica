package org.jacobo.pruebacapitole.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.jacobo.pruebacapitole.infrastructure.dto.PlanetDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPlanetDto;
import org.springframework.http.ResponseEntity;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlanetControllerApiImpl implements PlanetControllerApi {

    @Override
    public ResponseEntity<PaginatedPlanetDto> findPlanetByName(String name) {
        return ResponseEntity.ok(new PaginatedPlanetDto());
    }

    @Override
    public ResponseEntity<PlanetDto> findPlanetById(Long id) {
        return ResponseEntity.ok(new PlanetDto());
    }

}
