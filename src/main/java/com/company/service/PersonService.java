package com.company.service;

import com.company.dto.PersonDto;
import com.company.repository.PersonRepository;
import com.company.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return personRepository.findByUsername(username).cast(UserDetails.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Flux<PersonDto> findAll() {
        return personRepository.findAll().map(personMapper::entityToDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<PersonDto> createPerson(Mono<PersonDto> personDtoMono){
        return  personDtoMono.map(personMapper::dtoToEntity)
                .flatMap(personRepository::save)
                .map(personMapper::entityToDto);
    }
}
