package org.jacobo.pruebacapitole.infrastructure.controller;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.application.service.PeopleService;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderBy;
import org.jacobo.pruebacapitole.infrastructure.dto.OrderDirection;
import org.jacobo.pruebacapitole.infrastructure.dto.PaginatedPeopleDto;
import org.jacobo.pruebacapitole.infrastructure.dto.PeopleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PeopleControllerApiImpl implements PeopleControllerApi {

    private final PeopleService peopleService;

    @Override
    public ResponseEntity<PeopleDto> findById(Long id){
        return  ResponseEntity.ok(new PeopleDto());
    }

    @Override
    public ResponseEntity<PaginatedPeopleDto> findPeopleByName(String name, OrderBy orderBy, OrderDirection order){
        //TODO meter un dom de request
        val people = peopleService.findByName(name, Strings.nullToEmpty(orderBy.getValue()), Strings.nullToEmpty(order.getValue()));

        return  ResponseEntity.ok(new PaginatedPeopleDto());
    }


}
