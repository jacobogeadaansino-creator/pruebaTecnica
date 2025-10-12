package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleFeignToDomMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import lombok.val;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleSwapiFeignAdapter implements PeopleSwapiService {

    private final StarWarsFeignClient starWarsFeignClient;
    private final PeopleFeignToDomMapper peopleFeignToDomMapper;

    @Override
    public PeopleDom findById(Long id) {
        val reponse = starWarsFeignClient.getPeopleById(id);
        return peopleFeignToDomMapper.peopleFeignToDom(reponse);
    }
}
