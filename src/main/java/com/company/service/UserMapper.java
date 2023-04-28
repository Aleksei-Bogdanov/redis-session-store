package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.api.dto.UserDto;
import com.company.persistance.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserMapper {

    public Mono<PersonDto> map(User user) {
        PersonDto personDto = new PersonDto();
        personDto.setId(user.getId());
        personDto.setUsername(user.getUsername());
        personDto.setUserRole(user.getUserRole());
        return Mono.just(personDto);
    }

    public Mono<User> map(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return Mono.just(user);
    }
}
