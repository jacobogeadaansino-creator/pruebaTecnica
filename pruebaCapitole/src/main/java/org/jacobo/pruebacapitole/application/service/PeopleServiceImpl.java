package org.jacobo.pruebacapitole.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleSwapiService peopleSwapiService;


    @Override
    public List<PeopleDom> findByName(String name, String order) {
        val people = peopleSwapiService.findByName(name, order);
        return null;
    }

    @Override
    public PeopleDom findById(Long id) {
        return null;
    }
}
