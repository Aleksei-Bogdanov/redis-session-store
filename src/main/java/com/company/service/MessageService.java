package com.company.service;

import com.company.domain.Message;
import com.company.domain.Person;
import com.company.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Flux<Message> list() {
        return messageRepository.findAll();
    }
    public Flux<Message> list(long personId) {
        return messageRepository.findByPersonId(personId);
    }

    public Mono<Message> addOne(Message message) {
        return messageRepository.save(message);
    }
}
