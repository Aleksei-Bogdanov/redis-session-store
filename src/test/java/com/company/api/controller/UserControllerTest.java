package com.company.api.controller;

import com.company.api.dto.PersonDto;
import com.company.api.dto.UserDto;
import com.company.persistance.entity.UserRole;
import com.company.service.MessageService;
import com.company.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(UserController.class)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserService userService;
    @MockBean
    private MessageService messageService;

    private final String baseUrl = "/api/users";
    private final String registrationUrl = "/registration";
    private static final long FIRST_ELEMENT_ID = 1;
    private static final long SECOND_ELEMENT_ID = 2;
    private static final String FIRST_USER_NAME = "TestName";
    private static final String SECOND_USER_NAME = "TestName2";
    private static final String PASSWORD = "TestPassword";
    private final String jsonId = "$.id";
    private final String jsonUsername = "$.username";
    private final String jsonUserRole = "$.userRole";
    private final String jsonStatus = "$.status";
    private final String methodSourcePath = "com.company.api.controller.UserControllerTest#";

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/users возвращает список всех пользователей")
    void shouldReturnAllPersons_soResponseIs200() {
        PersonDto personDtoUser = getPersonWithRoleUser();
        PersonDto personDtoAdmin = getPersonWithRoleAdmin();

        Flux<PersonDto> personDtoFlux = Flux.just(personDtoUser, personDtoAdmin);

        when(userService.findAll()).thenReturn(personDtoFlux);

        Flux<PersonDto> responseBody = this.webTestClient
                .get()
                .uri(baseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(PersonDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(personDtoUser)
                .expectNext(personDtoAdmin)
                .verifyComplete();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/users/registration создает нового пользователя")
    public void shouldCreateUser_thenReturnSavedUser_soResponseIs201(){
        //given
        UserDto userDto = getUserWithRoleUser();
        PersonDto personDto = getPersonWithRoleUser();

        when(userService.createUser(userDto)).thenReturn(Mono.just(personDto));

        //then
        final WebTestClient.ResponseSpec response = this.webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(baseUrl + registrationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(getUserWithRoleUser())
                .exchange();

        response.expectStatus()
                .isCreated()
                .expectHeader().doesNotExist("Location")
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(jsonId).isEqualTo(1)
                .jsonPath(jsonUsername).isEqualTo(userDto.getUsername())
                .jsonPath(jsonUserRole).isEqualTo(userDto.getUserRole().toString());
    }

    @ParameterizedTest
    @MethodSource(methodSourcePath + "provideInvalidPersonDto")
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/users/registration возвращает код 400 если введены некорректные данные")
    void shouldNotCreateUser_becauseRequestValidationFailed_soResponseIs400(UserDto invalidUserDto) {
        final WebTestClient.ResponseSpec response = this.webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(baseUrl + registrationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserDto)
                .exchange();

        response.expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(jsonStatus).exists();
    }

    private UserDto getUserWithRoleUser(){
        return new UserDto(
                FIRST_ELEMENT_ID,
                FIRST_USER_NAME,
                PASSWORD,
                UserRole.ROLE_USER);
    }

    private PersonDto getPersonWithRoleUser(){
        return new PersonDto(
                FIRST_ELEMENT_ID,
                FIRST_USER_NAME,
                UserRole.ROLE_USER);
    }
    private PersonDto getPersonWithRoleAdmin(){
        return new PersonDto(
                SECOND_ELEMENT_ID,
                SECOND_USER_NAME,
                UserRole.ROLE_ADMIN);
    }

    static Stream<Arguments> provideInvalidPersonDto() {
        return Stream.of(
                Arguments.of(new UserDto(FIRST_ELEMENT_ID, "", PASSWORD, UserRole.ROLE_USER)),
                Arguments.of(new UserDto(FIRST_ELEMENT_ID, FIRST_USER_NAME, "", UserRole.ROLE_USER)),
                Arguments.of(new UserDto(SECOND_ELEMENT_ID, SECOND_USER_NAME, PASSWORD, null))
        );
    }
}
