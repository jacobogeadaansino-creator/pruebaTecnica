package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;


import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleFeignDto;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PeopleFeignToDomMapper {

    PeopleDom peopleFeignToDom(PeopleFeignDto peopleFeignDto);

    @Mapping(source = "eye_color", target = "eyeColor")
    @Mapping(source = "hair_color", target = "hairColor")
    @Mapping(source = "skin_color", target = "skinColor")
    @Mapping(source = "birth_year", target = "birthYear")
    @Mapping(source = "homeworld", target = "homeWorld")
    @Mapping(source = "starships", target = "starShips")
    PeopleResultDom peopleFeignToDom(PeopleResultFeignDto peopleFeignDto);

    List<PeopleResultDom> peopleFeignListToDom(List<PeopleResultFeignDto> peopleFeignDto);

}
