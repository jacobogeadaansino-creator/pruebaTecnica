package org.jacobo.pruebacapitole.domain.model.people;

import java.util.List;

public record PeopleDom(
        Long count,
        Integer page,
        List<PeopleResultDom> results
) {
}