package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.persistance.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final PasswordEncoder passwordEncoder;

    public PersonDto entityToDto(Person person) {
        PersonDto personDto = new PersonDto();
        BeanUtils.copyProperties(person, personDto);
        return personDto;
    }

    public Person dtoToEntity(PersonDto personDto) {
        Person person = new Person();
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        BeanUtils.copyProperties(personDto, person);
        return person;
    }
}
