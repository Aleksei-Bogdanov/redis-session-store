package com.company.controller;

import com.company.domain.Person;
import com.company.repository.PersonRepository;
import com.company.service.PersonService;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class PersonControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private PersonService personService;

    @BeforeEach
    public void setup() {
        initializeDatabase();
        insertData();
    }

    private void initializeDatabase() {
        String dbUrl = "r2dbc:postgresql://localhost:5432/test";
        ConnectionFactory connectionFactory = ConnectionFactories.get(dbUrl);
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
        String query = "CREATE TABLE IF NOT EXISTS tbl_persons (id bigserial PRIMARY KEY, username varchar(64));";
        template.getDatabaseClient().sql(query).fetch().rowsUpdated().block();
    }

    private void insertData() {
        Person person1 = new Person();
        Person person2 = new Person();
        Person person3 = new Person();
        Person person4 = new Person();
        person1.setUsername("Leonardo");
        person2.setUsername("Raphael");
        person3.setUsername("Donatello");
        person4.setUsername("Michelangelo");
        Flux<Person> personFlux = Flux.just(person1, person2, person3, person4);

        personRepository.deleteAll()
                .thenMany(personFlux)
                .flatMap(personRepository::save)
                .doOnNext(person -> log.info("inserted {}", person))
                .blockLast();
    }

    @Test
    public void getAll() {
        webTestClient.get()
                .uri("api/persons")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$")
                .isArray()
                .jsonPath("$[0].name")
                .isEqualTo("Leonardo")
                .jsonPath("$[1].name")
                .isEqualTo("Raphael")
                .jsonPath("$[2].name")
                .isEqualTo("Donatello")
                .jsonPath("$[3].name")
                .isEqualTo("Michelangelo");
    }
}
