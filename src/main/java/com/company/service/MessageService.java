package com.company.service;

import com.company.api.dto.MessageDto;
import com.company.persistance.entity.Message;
import com.company.persistance.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Flux<MessageDto> getMessages() {
        return messageRepository.findAll()
                .flatMap(messageMapper::map);
    }

    public Flux<MessageDto> getMessagesByPersonId(long personId) {
        return messageRepository.findByPersonId(personId)
                .flatMap(messageMapper::map);
    }

    public Mono<MessageDto> createMessage(MessageDto messageDto) {
        return messageMapper.map(messageDto)
                .flatMap(messageRepository::save)
                .flatMap(messageMapper::map);

    }
}
