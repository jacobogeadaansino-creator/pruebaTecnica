package org.jacobo.pruebacapitole.infrastructure.swapi.mappers;

import org.apache.logging.log4j.util.Strings;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPlanetDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PlanetDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PlanetDomDtoMapper {

    @Mapping(source = ".", target = "totalPages", qualifiedByName = "countTotalPages")
    @Mapping(source = ".", target = "totalItems", qualifiedByName = "count")
    @Mapping(source = "peopleDom.results", target = "results")
    @Mapping(target = "pageSize", constant = "15")
    @Mapping(target = "currentPage", source = "page")
    PaginatedPlanetDto toDto(PlanetDom peopleDom, @Context Integer page, @Context String baseUrl);

    @Named("count")
    default Integer count(PlanetDom planetDom){
        return planetDom.results().size();
    }

    @Named("countTotalPages")
    default Integer countTotalPages(PlanetDom planetDom) {
        return (int) Math.ceil((double) planetDom.count() / 15);
    }


    @Mapping(source = ".", target = "url", qualifiedByName = "replaceUrl")
    PlanetDto toDtoPlanet(PlanetResultDom planetDom, @Context String baseUrl);

    @Named("replaceUrl")
    default String replaceUrl(PlanetResultDom peopleResultDom, @Context String baseUrl){
        if (Strings.isNotEmpty(baseUrl)) {
            return peopleResultDom.getUrl().replace("https://swapi.dev/api", baseUrl);
        }
        return peopleResultDom.getUrl();
    }
}
