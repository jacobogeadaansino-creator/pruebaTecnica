package org.jacobo.pruebacapitole.infrastructure.swapi.dto.people;


import java.util.List;

public record PeopleFeignDto(Long count, String next, String previous, List<PeopleResultFeignDto> results){}


