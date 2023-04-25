package com.company.api.controller;

import com.company.api.dto.MessageDto;
import com.company.api.dto.PersonDto;
import com.company.persistance.entity.Person;
import com.company.service.MessageService;
import com.company.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final MessageService messageService;

    @GetMapping
    public Flux<PersonDto> getPersons(){
        return personService.findAll();
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonDto> createPerson(@Valid @RequestBody PersonDto personDto) {
        return personService.createPerson(personDto);
    }

    @GetMapping("/messages")
    public Flux<MessageDto> getMessages(@AuthenticationPrincipal Person person) {
        return messageService.getMessagesByPersonId(person.getId());
    }

    @PostMapping("/messages")
    public Mono<MessageDto> createMessage(
            @AuthenticationPrincipal Person person,
            @Valid @RequestBody MessageDto messageDto) {
        messageDto.setPersonId(person.getId());
        return messageService.createMessage(messageDto);
    }
}
