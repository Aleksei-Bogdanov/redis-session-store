package com.company.service;

import com.company.RedisSessionStoreApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RedisSessionStoreApplication.class)
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    public void personsWhenNotAuthenticatedThenDenied() {
        StepVerifier.create(this.personService.findAll())
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @WithMockUser
    public void personsWhenUserThenDenied() {
        StepVerifier.create(this.personService.findAll())
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void messagesWhenAdminThenOk() {
        StepVerifier.create(this.personService.findAll())
                .expectNext()
                .verifyComplete();
    }
}