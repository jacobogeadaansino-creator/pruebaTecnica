package org.jacobo.pruebacapitole.application.service.commons;

import java.util.List;

public interface EntitySorter<T> {
    void sort(List<T> list, String orderBy, String order);
}