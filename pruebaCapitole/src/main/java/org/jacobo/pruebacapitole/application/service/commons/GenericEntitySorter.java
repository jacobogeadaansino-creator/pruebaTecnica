package org.jacobo.pruebacapitole.application.service.commons;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class GenericEntitySorter<T> implements EntitySorter<T> {
    private final Map<String, Function<T, Comparable>> fieldExtractors;

    public GenericEntitySorter(Map<String, Function<T, Comparable>> fieldExtractors) {
        this.fieldExtractors = fieldExtractors;
    }

    @Override
    public void sort(List<T> list, String orderBy, String order) {
        if (list == null || list.isEmpty() || orderBy == null || list.size() == 1) return;
        Function<T, Comparable> extractor = fieldExtractors.get(orderBy.toLowerCase());
        if (extractor == null) return;
        Comparator<T> comparator = Comparator.comparing(extractor, Comparator.nullsLast(Comparator.naturalOrder()));
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        list.sort(comparator);
    }
}
