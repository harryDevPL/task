package com.cdq.task.domain.task;

import com.cdq.task.domain.person.dto.PersonUpsertedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class PersonUpsertedEventListener {

    private final TaskProcessor taskProcessor;

    @EventListener
    public void onPersonUpserted(PersonUpsertedEvent event) {
        log.info("Received task to process with ID: {}", event.taskId());
        taskProcessor.handlePersonUpsertedEvent(event)
            .subscribe(
                null,
                error -> log.error("Error handling PersonUpsertedEvent", error)
            );
    }
}
