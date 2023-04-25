package com.company.service;

import com.company.api.dto.MessageDto;
import com.company.persistance.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    public Mono<MessageDto> map(Message message) {
        MessageDto messageDto = new MessageDto();
        BeanUtils.copyProperties(message, messageDto);
        return Mono.just(messageDto);
    }

    public Mono<Message> map(MessageDto messageDto) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDto, message);
        return Mono.just(message);
    }
}
