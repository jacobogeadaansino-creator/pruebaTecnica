package org.jacobo.pruebacapitole.application.service.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

import java.util.Optional;

public interface PeopleService {

    PeopleDom findByName(String name, String orderBy, String order, Integer page);

    PeopleResultDom findById(Integer id);

}
