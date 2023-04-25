package com.company.api.controller;

import com.company.api.dto.PersonDto;
import com.company.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public Flux<PersonDto> getAll(){
        return personService.findAll();
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonDto> createPerson(@Valid @RequestBody PersonDto personDtoMono) {
        return personService.createPerson(personDtoMono);
    }
}
