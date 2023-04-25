package com.company.controller;

import com.company.entity.Message;
import com.company.entity.Person;
import com.company.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public Flux<Message> list(@AuthenticationPrincipal Person person) {
        return messageService.list(person.getId());
    }

    @PostMapping
    public Mono<Message> add(
            @AuthenticationPrincipal Person person,
            @RequestBody Message message) {
        message.setPersonId(person.getId());
        return messageService.addOne(message);
    }
}
