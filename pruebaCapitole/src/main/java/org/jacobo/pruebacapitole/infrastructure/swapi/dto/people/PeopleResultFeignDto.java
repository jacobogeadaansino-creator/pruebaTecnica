package org.jacobo.pruebacapitole.infrastructure.swapi.dto.people;

import java.time.OffsetDateTime;
import java.util.List;

public record PeopleResultFeignDto(String name, String height, String mass,
                                   String hair_color, String skin_color,
                                   String eye_color, String birth_year, String gender,
                                   String homeworld, List<String> films, List<String> species,
                                   List<String> vehicles, List<String> starships, OffsetDateTime created,
                                   OffsetDateTime edited, String url) {
}
