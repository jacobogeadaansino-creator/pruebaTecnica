package org.jacobo.pruebacapitole.domain.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;

public interface PeopleSwapiService {

    PeopleDom findById(Long id);
}
