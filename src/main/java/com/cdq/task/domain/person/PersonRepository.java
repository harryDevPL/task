package com.cdq.task.domain.person;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {

    Mono<Person> findByIdNumber(String idNumber);
}
