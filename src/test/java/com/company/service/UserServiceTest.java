package com.company.service;

import com.company.api.dto.UserDto;
import com.company.persistance.entity.UserRole;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
@TestMethodOrder(OrderAnnotation.class)
class UserServiceTest {
    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("redis-session-store-test_db");
    @Autowired
    private UserService userService;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.flyway.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.r2dbc.url", () -> POSTGRESQL_CONTAINER.getJdbcUrl().replace("jdbc", "r2dbc"));
        registry.add("spring.r2dbc.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    @Order(1)
    void shouldSaveGivenPersonIntoDatabase_andRetrieveNewlyCreatedRecordWithIdAssigned() {
//        final var returnedPerson = userService.createUser(getUserDtoWithRoleUser());
//        StepVerifier
//                .create(returnedPerson)
//                .expectNextMatches(personDto -> personDto.getId() == 1)
//                .verifyComplete();
    }

    @Test
    @Order(2)
    void shouldSaveGivenPersonIntoDatabase_andRetrieveNewlyCreatedRecordWithUsernameAssigned() {
//        final var returnedPerson = userService.createUser(getUserDtoWithRoleAdmin());
//        StepVerifier
//                .create(returnedPerson)
//                .expectNextMatches(personDto -> personDto.getUsername().equals("Neo"))
//                .verifyComplete();
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "ADMIN")
    void shouldGetPersonsFromDatabase_andRetrieveAllCreatedRecords() {
//        final var personDtoFlux = userService.findAll();
//
//        StepVerifier
//                .create(personDtoFlux)
//                .expectNextCount(2)
//                .verifyComplete();
    }

    private UserDto getUserDtoWithRoleUser(){
        return new UserDto(
                1,
                "Leo",
                "password",
                UserRole.ROLE_USER);
    }

    private UserDto getUserDtoWithRoleAdmin(){
        return new UserDto(
                2,
                "Neo",
                "password",
                UserRole.ROLE_ADMIN);
    }
}
