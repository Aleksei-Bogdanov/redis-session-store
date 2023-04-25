package com.company.controller;

import com.company.dto.PersonDto;
import com.company.entity.PersonRole;
import com.company.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/persons возвращает список всех пользователей")
    void shouldReturnAllPersons_soResponseIs200() {
        PersonDto personDtoUser = getPersonUser();
        PersonDto personDtoAdmin = getPersonAdmin();

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
        Mono<PersonDto> personDtoMono = Mono.just(getPersonUser());

        when(personService.createPerson(personDtoMono)).thenReturn(personDtoMono);

        //then
        this.rest.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(baseUrl + registrationUrl)
                .body(Mono.just(personDtoMono),PersonDto.class)
                .exchange()
                .expectStatus().isCreated();
    }

    private PersonDto getPersonUser(){
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
    private PersonDto getPersonAdmin(){
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
}
