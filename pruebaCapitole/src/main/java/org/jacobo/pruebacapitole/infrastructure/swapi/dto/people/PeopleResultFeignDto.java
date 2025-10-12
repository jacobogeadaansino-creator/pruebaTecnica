package org.jacobo.pruebacapitole.infrastructure.swapi.dto.people;

import java.time.LocalDateTime;
import java.util.List;

public record PeopleResultFeignDto(String name, String height, String mass, String hairColor, String skinColor,
                                   String eyeColor, String birthYear, String gender,
                                   String homeworld, List<String> films, List<String> species,
                                   List<String> vehicles, List<String> starships, LocalDateTime created,
                                   LocalDateTime edited, LocalDateTime url) {
}
