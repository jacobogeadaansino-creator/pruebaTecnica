package org.jacobo.pruebacapitole.domain.model.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleResultDom implements Serializable {
    private String name;
    private String height;
    private String mass;
    private String hairColor;
    private String skinColor;
    private String eyeColor;
    private String birthYear;
    private String gender;
    private String homeWorld;
    private List<String> films;
    private List<String> species;
    private List<String> vehicles;
    private List<String> starShips;
    private OffsetDateTime created;
    private OffsetDateTime edited;
    private String url;

}
