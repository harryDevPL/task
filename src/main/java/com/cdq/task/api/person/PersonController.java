package com.cdq.task.api.person;

import com.cdq.task.domain.person.PersonFacade;
import com.cdq.task.domain.person.dto.UpsertPersonCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
@Slf4j
class PersonController {

    private final PersonFacade personFacade;

    @PostMapping
    public Mono<UpsertPersonResponse> upsertPerson(@Valid @RequestBody UpsertPersonRequest request) {
        log.info("Received request: {}", request.toString()); // Careful with logging all requests, especially with PII data
        return personFacade.upsertPerson(UpsertPersonCommand.from(request))
            .map(taskId -> new UpsertPersonResponse(taskId.toString()));
    }
}
