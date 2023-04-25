package com.company.persistance.repository;

import com.company.persistance.entity.Message;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends R2dbcRepository<Message, Long> {
    Flux<Message> findByPersonId(long personId);
}
