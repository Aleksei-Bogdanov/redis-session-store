package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.persistance.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final PasswordEncoder passwordEncoder;

    public Mono<PersonDto> map(Person person) {
        PersonDto personDto = new PersonDto();
        BeanUtils.copyProperties(person, personDto);
        return Mono.just(personDto);
    }

    public Mono<Person> map(PersonDto personDto) {
        Person person = new Person();
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        BeanUtils.copyProperties(personDto, person);
        return Mono.just(person);
    }
}
