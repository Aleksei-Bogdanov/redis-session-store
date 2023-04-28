package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.api.dto.UserDto;
import com.company.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .cast(UserDetails.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Flux<PersonDto> findAll() {
        return userRepository
                .findAll()
                .flatMap(userMapper::map);
    }

    public Mono<PersonDto> createUser(UserDto userDto){
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return  userMapper.map(userDto)
                .flatMap(userRepository::save)
                .flatMap(userMapper::map);
    }
}
