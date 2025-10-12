package org.jacobo.pruebacapitole.application.service;


import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiService;
import org.springframework.stereotype.Service;
import org.jacobo.pruebacapitole.application.service.PeopleSorter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleSwapiService peopleSwapiService;
    private final DataCachePort dataCachePort;
    private final PeopleSorter peopleSorter;

    @Override
    public List<PeopleDom> findByName(String name,String orderBy, String order) {
        final List<PeopleDom> result = new ArrayList<>();
        if(Strings.isNullOrEmpty(order)){
            order = "asc";
        }
        val peopleCache = dataCachePort.getPeopleCache();
        if(peopleCache.containsKey(name)){
            result.add(peopleCache.get(name));
        }
        result.addAll(peopleCache.values().stream().filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())).toList());
        peopleSorter.sort(result, orderBy, order);
        return result;
    }

    @Override
    public PeopleDom findById(Long id) {
        return peopleSwapiService.findById(id);
    }
}
