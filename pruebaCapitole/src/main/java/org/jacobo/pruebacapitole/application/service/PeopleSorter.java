package org.jacobo.pruebacapitole.application.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import java.util.List;

public interface PeopleSorter {
    void sort(List<PeopleDom> list, String orderBy, String order);
}