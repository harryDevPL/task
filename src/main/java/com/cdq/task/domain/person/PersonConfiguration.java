package com.cdq.task.domain.person;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PersonConfiguration {

    @Bean
    PersonFacade personFacade(ApplicationEventPublisher applicationEventPublisher,
                              PersonRepository personRepository
    ) {
        return new PersonFacade(applicationEventPublisher, personRepository);
    }
}
