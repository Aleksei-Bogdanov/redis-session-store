package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.api.dto.UserDto;
import com.company.persistance.entity.User;
import com.company.persistance.entity.UserRole;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private static final String USER_NAME = "TestName";
    private static final String PASSWORD = "TestPassword";
    private static final long FIRST_ELEMENT_ID = 1;
    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldReturnValidPersonDTO_WhenMapsUser() {
        final var user = getUserWithRoleUser();
        final var expectedUserDto = getPersonWithRoleUser();
        final var actualPersonDto = userMapper.map(user);

        StepVerifier
                .create(actualPersonDto)
                .expectNext(expectedUserDto)
                .verifyComplete();
    }

    @Test
    void shouldReturnValidUser_WhenMapsUserDTO() {
        final var expectedUser = getUserWithRoleUser();
        final var userDto = getUserDtoWithRoleUser();
        final var actualUser = userMapper.map(expectedUser);

        StepVerifier
                .create(actualUser)
                .assertNext(user -> assertThat(userDto)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedUser))
                .verifyComplete();
    }

    private User getUserWithRoleUser(){
        return new User(
                FIRST_ELEMENT_ID,
                USER_NAME,
                PASSWORD,
                UserRole.ROLE_USER
        );
    }

    private UserDto getUserDtoWithRoleUser(){
        return new UserDto(
                FIRST_ELEMENT_ID,
                USER_NAME,
                PASSWORD,
                UserRole.ROLE_USER
        );
    }

    private PersonDto getPersonWithRoleUser(){
        return new PersonDto(
                FIRST_ELEMENT_ID,
                USER_NAME,
                UserRole.ROLE_USER);
    }

}