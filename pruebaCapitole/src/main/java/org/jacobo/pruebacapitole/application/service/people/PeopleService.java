package org.jacobo.pruebacapitole.application.service.people;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;

public interface PeopleService {

        PeopleDom findByName(String name, String orderBy, String order, Integer page);

        PeopleResultDom findById(Integer id);

}
