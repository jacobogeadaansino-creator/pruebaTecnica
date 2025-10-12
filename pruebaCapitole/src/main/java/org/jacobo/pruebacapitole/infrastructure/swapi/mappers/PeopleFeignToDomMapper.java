package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;


import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.infrastructure.swapi.dto.people.PeopleResultFeignDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PeopleFeignToDomMapper {

    PeopleDom peopleFeignToDom(PeopleResultFeignDto peopleFeignDto);

    List<PeopleDom> peopleFeignListToDom(List<PeopleResultFeignDto> peopleFeignDto);

}
