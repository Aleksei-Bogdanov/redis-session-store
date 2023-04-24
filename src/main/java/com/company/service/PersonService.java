package com.company.service;

import com.company.domain.Person;
import com.company.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return personRepository.findByUsername(username).cast(UserDetails.class);
    }

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    public Mono<Person> save(Person user){
        Person person = new Person();
        person.setUsername(user.getUsername());
        person.setPassword(passwordEncoder.encode(user.getPassword()));
        person.setPersonRole(user.getPersonRole());

        return personRepository.save(person);
    }
}
