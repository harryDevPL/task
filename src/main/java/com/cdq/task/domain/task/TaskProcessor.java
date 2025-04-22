package com.cdq.task.domain.task;

import com.cdq.task.domain.person.dto.PersonUpsertedEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
@Slf4j
class TaskProcessor {

    private final BlockingQueue<PersonUpsertedEvent> personUpsertedEvents = new LinkedBlockingQueue<>();
    private final TaskEventHandler taskEventHandler;
    private final TaskRepository taskRepository;

    protected Mono<Void> handlePersonUpsertedEvent(PersonUpsertedEvent event) {
        return taskRepository.insert(
                event.taskId(),
                TaskStatus.PENDING.name(),
                0,
                0
            )
            .doOnSuccess(v -> {
                log.info("Task [{}] successfully inserted in DB", event.taskId());
                personUpsertedEvents.offer(event);
                log.info("Task with ID [{}] has been added to the queue", event.taskId());
            })
            .doOnError(error -> log.error("Error while inserting task to DB", error));
    }

    @PostConstruct
    public void startProcessing() {
        Thread.ofVirtual().start(() -> {
            while (true) {
                try {
                    PersonUpsertedEvent event = personUpsertedEvents.take();
                    taskEventHandler.processEvent(event)
                        .doOnError(error -> log.error("Error processing event", error))
                        .subscribe();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Event processor interrupted", e);
                    break;
                }
            }
        });
    }
}
