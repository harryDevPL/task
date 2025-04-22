package com.cdq.task.domain.person;

import com.cdq.task.domain.person.dto.PersonDto;
import com.cdq.task.domain.person.dto.PersonUpsertedEvent;
import com.cdq.task.domain.person.dto.UpsertPersonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class PersonFacade {

    private final ApplicationEventPublisher eventPublisher;
    private final PersonRepository personRepository;

    @Transactional
    public Mono<UUID> upsertPerson(UpsertPersonCommand command) {
        return personRepository.findByIdNumber(command.idNumber())
            .flatMap(existingPerson -> {
                log.info("Person will be updated");

                PersonDto oldSnapshotDto = existingPerson.toDto();
                existingPerson.updateFrom(command);
                return personRepository.save(existingPerson)
                    .map(savedPerson -> publishAndReturnTaskId(oldSnapshotDto, savedPerson));
            })
            .switchIfEmpty(Mono.defer(() -> {
                log.info("A new person will be created");
                Person newPerson = Person.from(command);
                newPerson.setId(null);
                PersonDto oldSnapshotDto = newPerson.toDto();
                return personRepository.save(newPerson)
                    .map(savedPerson -> publishAndReturnTaskId(oldSnapshotDto, savedPerson));
            }));
    }

    private UUID publishAndReturnTaskId(PersonDto oldSnapshot, Person savedPerson) {
        UUID taskId = UUID.randomUUID();
        eventPublisher.publishEvent(new PersonUpsertedEvent(
            taskId,
            oldSnapshot,
            savedPerson.toDto()
        ));
        log.info("Published event for task with ID: [{}]", taskId);
        return taskId;
    }
}
