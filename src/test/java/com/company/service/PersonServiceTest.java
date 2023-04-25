package com.company.service;

import com.company.api.dto.PersonDto;
import com.company.persistance.entity.PersonRole;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonServiceTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("redis-session-store-test_db");
    @Autowired
    private PersonService personService;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.flyway.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    @Order(1)
    void shouldSaveGivenPersonIntoDatabase_andRetrieveNewlyCreatedRecordWithIdAssigned() {
        final var returnedUser = personService.createPerson(getPersonWithRoleUser());
        StepVerifier
                .create(returnedUser)
                .expectNextMatches(el -> el.getId() == 1)
                .verifyComplete();
    }

    @Test
    @Order(2)
    void shouldSaveGivenPersonIntoDatabase_andRetrieveNewlyCreatedRecordWithUsernameAssigned() {
        final var returnedUser = personService.createPerson(getPersonWithRoleAdmin());
        StepVerifier
                .create(returnedUser)
                .expectNextMatches(el -> el.getUsername().equals("Neo"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "ADMIN")
    void shouldGetPersonsFromDatabase_andRetrieveAllCreatedRecords() {
        final var personDtoFlux = personService.findAll();

        StepVerifier
                .create(personDtoFlux)
                .expectNextCount(2)
                .verifyComplete();
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


}