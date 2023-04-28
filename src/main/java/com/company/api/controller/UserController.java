package com.company.api.controller;

import com.company.api.dto.MessageDto;
import com.company.api.dto.PersonDto;
import com.company.api.dto.UserDto;
import com.company.persistance.entity.User;
import com.company.service.MessageService;
import com.company.service.UserService;
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
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping
    public Flux<PersonDto> getPersons(){
        return userService.findAll();
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonDto> createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/messages")
    public Flux<MessageDto> getMessages(@AuthenticationPrincipal User user) {
        return messageService.getMessagesByPersonId(user.getId());
    }

    @PostMapping("/messages")
    public Mono<MessageDto> createMessage(
            @AuthenticationPrincipal User person,
            @Valid @RequestBody MessageDto messageDto) {
        messageDto.setPersonId(person.getId());
        return messageService.createMessage(messageDto);
    }
}
