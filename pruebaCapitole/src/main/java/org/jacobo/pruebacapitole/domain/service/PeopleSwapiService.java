package org.jacobo.pruebacapitole.domain.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

public interface PeopleSwapiService {

    PeopleDom findByName(String name);

    PeopleDom getPeopleByPage(int page);

    PeopleResultDom findById(Long id);

    PeopleDom getPeople();
}
