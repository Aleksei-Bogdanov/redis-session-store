package com.company.repository;

import com.company.domain.Message;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends R2dbcRepository<Message, Long> {
    Flux<Message> findByPersonId(long personId);
}
