package org.jacobo.pruebacapitole.application.service.commons;

import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.model.planets.PlanetResultDom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SorterConfig.class)
class SorterConfigTest {
    @Autowired
    private EntitySorter<PeopleResultDom> peopleSorter;
    @Autowired
    private EntitySorter<PlanetResultDom> planetSorter;

    @Test
    void peopleSorterBean_and_sorting() {
        assertNotNull(peopleSorter);
        List<PeopleResultDom> list = new ArrayList<>();
        list.add(new PeopleResultDom("B", null, null, null, null, null, null, null, null, null, null, null, null, OffsetDateTime.parse("2020-01-01T00:00:00Z"), null, null));
        list.add(new PeopleResultDom("A", null, null, null, null, null, null, null, null, null, null, null, null, OffsetDateTime.parse("2021-01-01T00:00:00Z"), null, null));
        peopleSorter.sort(list, "name", "asc");
        assertEquals("A", list.get(0).getName());
        peopleSorter.sort(list, "created", "desc");
        assertEquals("A", list.get(0).getName());
    }

    @Test
    void planetSorterBean_and_sorting() {
        assertNotNull(planetSorter);
        List<PlanetResultDom> list = new ArrayList<>();
        list.add(new PlanetResultDom("B", null, null, null, null, null, null, null, null, null, null, OffsetDateTime.parse("2020-01-01T00:00:00Z"), null, null));
        list.add(new PlanetResultDom("A", null, null, null, null, null, null, null, null, null, null, OffsetDateTime.parse("2021-01-01T00:00:00Z"), null, null));
        planetSorter.sort(list, "name", "asc");
        assertEquals("A", list.get(0).getName());
        planetSorter.sort(list, "created", "desc");
        assertEquals("A", list.get(0).getName());
    }
}

