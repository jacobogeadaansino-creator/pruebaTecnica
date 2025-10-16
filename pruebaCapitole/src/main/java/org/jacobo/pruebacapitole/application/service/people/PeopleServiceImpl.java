package org.jacobo.pruebacapitole.application.service.people;


import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.commons.EntitySorter;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiPort;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleCachePort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleSwapiPort peopleSwapiPort;
    private final EntitySorter<PeopleResultDom> peopleSorter;
    private final PeopleCachePort peopleCachePort;
    private static final int PAGE_SIZE = 15;

    @Override
    public PeopleDom findByName(String name, String orderBy, String order, Integer page) {
        if(Strings.isNullOrEmpty(order)){
            order = "asc";
        }
        List<PeopleResultDom> result = peopleCachePort.findByName(name);
        if(result.isEmpty()){
            log.info("cache fails, going to api");
            val swapiResponse = peopleSwapiPort.findByName(name);
            if (swapiResponse == null || swapiResponse.results() == null || swapiResponse.results().isEmpty()) {
                throw new NotFoundException("There is not result for name {}" + name);
            }
            result = swapiResponse.results();
            peopleCachePort.save(name, swapiResponse.results().get(0));
        }
        peopleSorter.sort(result, orderBy, order);

        int startIndex = (page - 1) * PAGE_SIZE;
        if (startIndex >= result.size()) {
            return new PeopleDom((long) result.size(), page, Collections.emptyList());
        }
        int endIndex = Math.min(startIndex + PAGE_SIZE, result.size());
        List<PeopleResultDom> pagedResult = result.subList(startIndex, endIndex);

        return new PeopleDom((long) result.size(), page, pagedResult);
    }

    @Override
    public PeopleResultDom findById(Integer id) {
        val cacheResult =  peopleCachePort.findById(id);
        if(cacheResult.isEmpty()){
            log.error("Cache fails, going to api" );
            val response = peopleSwapiPort.findById(Long.valueOf(id));
            if(response == null) {
                throw new NotFoundException("There is not result for id {}" + id);
            }
            peopleCachePort.save(response.getName(), response);
            return response;
        }
        return cacheResult.get();
    }

}
