package org.jacobo.pruebacapitole.application.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;

import java.util.List;
import java.util.Optional;

public interface PeopleService {

    List<PeopleDom> findByName(String name, String order);

    PeopleDom findById(Long id);
}
