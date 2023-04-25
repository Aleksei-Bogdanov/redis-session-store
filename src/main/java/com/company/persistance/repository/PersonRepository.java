package com.company.persistance.repository;

import com.company.persistance.entity.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends R2dbcRepository<Person, Long> {
    Mono<Person> findByUsername(String username);
}
