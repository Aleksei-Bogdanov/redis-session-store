package com.company.api.controller;

import com.company.api.dto.PersonDto;
import com.company.persistance.entity.PersonRole;
import com.company.service.PersonService;
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
@WebFluxTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private WebTestClient rest;
    @MockBean
    private PersonService personService;
    private final String baseUrl = "/api/persons";
    private final String registrationUrl = "/registration";
    private final String jsonId = "$.id";
    private final String jsonUsername = "$.username";
    private final String jsonPassword = "$.password";
    private final String jsonPersonRole = "$.personRole";
    private final String jsonStatus = "$.status";
    private final String methodSourcePath = "com.company.api.controller.PersonControllerTest#";

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/persons возвращает список всех пользователей")
    void shouldReturnAllPersons_soResponseIs200() {
        PersonDto personDtoUser = getPersonWithRoleUser();
        PersonDto personDtoAdmin = getPersonWithRoleAdmin();

        Flux<PersonDto> personDtoFlux = Flux.just(personDtoUser, personDtoAdmin);

        when(personService.findAll()).thenReturn(personDtoFlux);

        Flux<PersonDto> responseBody = this.rest
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
    @DisplayName("POST /api/persons/registration создает нового пользователя")
    public void shouldCreatePerson_thenReturnSavedPerson_soResponseIs201(){
        //given
        PersonDto personDto = getPersonWithRoleUser();

        when(personService.createPerson(personDto)).thenReturn(Mono.just(personDto));

        //then
        final WebTestClient.ResponseSpec response = this.rest
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(baseUrl + registrationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(getPersonWithRoleUser())
                .exchange();

        response.expectStatus()
                .isCreated()
                .expectHeader().doesNotExist("Location")
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(jsonId).isEqualTo(1)
                .jsonPath(jsonUsername).isEqualTo(personDto.getUsername())
                .jsonPath(jsonPassword).isEqualTo(personDto.getPassword())
                .jsonPath(jsonPersonRole).isEqualTo(personDto.getPersonRole().toString());
    }

    @ParameterizedTest
    @MethodSource(methodSourcePath + "provideInvalidPersonDto")
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/persons/registration возвращает код 400 если введены некорректные данные")
    void shouldNotCreatePerson_becauseRequestValidationFailed_soResponseIs400(PersonDto invalidPersonDto) {
        final WebTestClient.ResponseSpec response = this.rest
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(baseUrl + registrationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPersonDto)
                .exchange();

        response.expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(jsonStatus).exists();
    }

    private PersonDto getPersonWithRoleUser(){
        return new PersonDto(
                1,
                "Leo",
                "password",
                PersonRole.ROLE_USER,
                true,
                true,
                true,
                true);
    }
    private PersonDto getPersonWithRoleAdmin(){
        return new PersonDto(
                2,
                "Neo",
                "password",
                PersonRole.ROLE_ADMIN,
                true,
                true,
                true,
                true);
    }

    static Stream<Arguments> provideInvalidPersonDto() {
        return Stream.of(
                Arguments.of(new PersonDto(1, "", "password", PersonRole.ROLE_USER, true, true, true, true)),
                Arguments.of(new PersonDto(1, "Leo", "", PersonRole.ROLE_USER, true, true, true, true)),
                Arguments.of(new PersonDto(2, "Neo", "password", null, true, true, true, true))
        );
    }
}
