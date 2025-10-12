package org.jacobo.pruebacapitole.application.service;

import org.jacobo.pruebacapitole.domain.model.people.PeopleDom;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DefaultPeopleSorter implements PeopleSorter {
    @Override
    public void sort(List<PeopleDom> list, String orderBy, String order) {
        if (list == null || list.isEmpty()) return;
        boolean desc = order != null && order.equalsIgnoreCase("desc");
        if (orderBy == null) return;
        if (orderBy.equalsIgnoreCase("created")) {
            list.sort((p1, p2) -> {
                if (p1.getCreated() == null || p2.getCreated() == null) return 0;
                int cmp = p1.getCreated().compareTo(p2.getCreated());
                return desc ? -cmp : cmp;
            });
        } else if (orderBy.equalsIgnoreCase("name")) {
            list.sort((p1, p2) -> {
                int cmp = p1.getName().compareTo(p2.getName());
                return desc ? -cmp : cmp;
            });
        }
    }
}


