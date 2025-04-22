package com.cdq.task.domain.task;

import com.cdq.task.domain.person.dto.PersonDto;
import com.cdq.task.domain.person.dto.PersonUpsertedEvent;
import com.cdq.task.domain.task.dto.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Slf4j
class TaskEventHandler {

    private final TaskRepository taskRepository;
    private final PropertiesClassifier propertiesClassifier;

    protected Mono<Void> processEvent(PersonUpsertedEvent event) {
        UUID taskId = event.taskId();
        PersonDto oldPerson = event.oldPerson();
        PersonDto newPerson = event.newPerson();

        log.info("Starting processing task with ID: {}", taskId);

        return taskRepository.findById(taskId) // <<< you forgot this "return"
            .switchIfEmpty(Mono.error(new TaskNotFoundException(taskId)))
            .flatMap(task -> {
                task.setStatus(TaskStatus.IN_PROGRESS);
                task.setPercentageDone(0);
                return taskRepository.save(task)
                    .then(classifyAndUpdate(task, () -> task.setFirstNameClassificationType(
                        propertiesClassifier.classifyField(oldPerson.firstName(), newPerson.firstName())
                    ), 20))
                    .then(classifyAndUpdate(task, () -> task.setLastNameClassificationType(
                        propertiesClassifier.classifyField(oldPerson.lastName(), newPerson.lastName())
                    ), 40))
                    .then(classifyAndUpdate(task, () -> task.setBirthDateClassificationType(
                        propertiesClassifier.classifyField(oldPerson.birthDate(), newPerson.birthDate())
                    ), 60))
                    .then(classifyAndUpdate(task, () -> task.setCompanyClassificationType(
                        propertiesClassifier.classifyField(oldPerson.company(), newPerson.company())
                    ), 80))
                    .then(Mono.defer(() -> {
                        task.setStatus(TaskStatus.COMPLETED);
                        task.setPercentageDone(100);
                        return taskRepository.save(task);
                    }))
                    .then(Mono.fromRunnable(() ->
                        log.info("Finished processing task with ID: {}", taskId)
                    ));
            });
    }

    private Mono<Void> classifyAndUpdate(Task task, Runnable classificationLogic, int percentage) {
        return Mono.fromRunnable(classificationLogic)
            .then(Mono.defer(() -> {
                task.setPercentageDone(percentage);
                log.info("For task ID: [{}], the progress is: [{}]", task.getId().toString(), percentage);
                return taskRepository.save(task);
            }))
            .then(Mono.delay(Duration.ofMillis(ThreadLocalRandom.current().nextLong(1, 1000))))
            .then();
    }
}
