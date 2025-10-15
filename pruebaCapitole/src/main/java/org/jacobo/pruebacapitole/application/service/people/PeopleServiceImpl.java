package org.jacobo.pruebacapitole.application.service.people;


import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.commons.EntitySorter;
import org.jacobo.pruebacapitole.application.service.commons.GenericEntitySorter;
import org.jacobo.pruebacapitole.domain.exception.NotFoundException;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleCacheRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleSwapiService peopleSwapiService;
    private final EntitySorter<PeopleResultDom> peopleSorter;
    private final PeopleCacheRepository peopleCacheRepository;
    private static final int PAGE_SIZE = 15;

    @Override
    public PeopleDom findByName(String name, String orderBy, String order, Integer page) {
        if(Strings.isNullOrEmpty(order)){
            order = "asc";
        }
        List<PeopleResultDom> result = peopleCacheRepository.findByName(name);
        if(result.isEmpty()){
            log.info("cache fails, going to api");
            val swapiResponse = peopleSwapiService.findByName(name);
            if (swapiResponse == null || swapiResponse.results() == null || swapiResponse.results().isEmpty()) {
                throw new NotFoundException("There is not result for name {}" + name);
            }
            result = swapiResponse.results();
            peopleCacheRepository.save(name, swapiResponse.results().get(0));
        }
        Map<String, Function<PeopleResultDom, Comparable>> peopleFields = Map.of(
                "created", PeopleResultDom::getCreated,
                "name", PeopleResultDom::getName
        );
        EntitySorter<PeopleResultDom> peopleSorter = new GenericEntitySorter<>(peopleFields);

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
        val cacheResult =  peopleCacheRepository.findById(id);
        if(cacheResult.isEmpty()){
            log.error("Cache fails, going to api" );
            val response = peopleSwapiService.findById(Long.valueOf(id));
            if(response == null) {
                throw new NotFoundException("There is not result for id {}" + id);
            }
            peopleCacheRepository.save(response.getName(), response);
            return response;
        }
        return cacheResult.get();
    }

}
