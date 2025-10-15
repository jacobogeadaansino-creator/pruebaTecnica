package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;

import org.apache.logging.log4j.util.Strings;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPeopleDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PeopleDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PeopleDomDtoMapper {

    @Mapping(source = ".", target = "totalPages", qualifiedByName = "countTotalPages")
    @Mapping(source = ".", target = "totalItems", qualifiedByName = "countPeople")
    @Mapping(source = "peopleDom.results", target = "results")
    @Mapping(target = "pageSize", constant = "15")
    @Mapping(target = "currentPage", source = "page")
    PaginatedPeopleDto toDto(PeopleDom peopleDom, @Context Integer page, @Context String baseUrl);

    @Named("countPeople")
    default Integer countPeople(PeopleDom peopleDom){
        return peopleDom.results().size();
    }

    @Named("countTotalPages")
    default Integer countTotalPages(PeopleDom peopleDom) {
        return (int) Math.ceil((double) peopleDom.count() / 15);
    }


    @Mapping(source = ".", target = "url", qualifiedByName = "replaceUrl")
    PeopleDto toDtoPeople(PeopleResultDom peopleDom, @Context String baseUrl);

    @Named("replaceUrl")
    default String replaceUrl(PeopleResultDom peopleResultDom, @Context String baseUrl){
        if (Strings.isNotEmpty(baseUrl)) {
            return peopleResultDom.getUrl().replace("https://swapi.dev/api", baseUrl);
        }
        return peopleResultDom.getUrl();
    }
}
