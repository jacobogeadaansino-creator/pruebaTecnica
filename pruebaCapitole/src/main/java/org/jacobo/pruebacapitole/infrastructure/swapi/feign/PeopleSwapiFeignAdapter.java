package org.jacobo.pruebacapitole.infrastructure.swapi.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.jacobo.pruebacapitole.infrastructure.swapi.mappers.PeopleFeignToDomMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleSwapiFeignAdapter implements PeopleSwapiService {

    private final StarWarsFeignClient starWarsFeignClient;
    private final PeopleFeignToDomMapper peopleFeignToDomMapper;

    @Override
    public PeopleDom findByName(String name) {
        val response = starWarsFeignClient.getPeopleByName(name);
        if (response.count() == 0 || response.results().isEmpty()) {
            throw new NotFoundException("There is not result for name {}" + name);
        }
        return peopleFeignToDomMapper.peopleFeignToDom(response);
    }

    @Override
    public PeopleDom getPeopleByPage(int page) {
        val response = starWarsFeignClient.getPeopleByPage(page);
        if (response.count() == 0 || response.results().isEmpty()) {
            throw new NotFoundException("There is not result for page {}" + page);
        }
        return peopleFeignToDomMapper.peopleFeignToDom(response);
    }


    @Override
    public PeopleResultDom findById(Long id) {
        val response = starWarsFeignClient.getPeopleById(id);
        return peopleFeignToDomMapper.peopleFeignToDom(response);
    }

    @Override
    public PeopleDom getPeople() {
        return peopleFeignToDomMapper.peopleFeignToDom(starWarsFeignClient.getPeople());
    }
}
