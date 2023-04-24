package com.company.controller;

import com.company.domain.Person;
import com.company.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public Flux<Person> getAll(){
        return personService.findAll();
    }

    @PostMapping("/registration")
    public Mono<Person> addNewPerson(@RequestBody Person person) {
        return personService.save(person);
    }
}
