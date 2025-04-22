package com.cdq.task.domain.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
class TaskConfiguration {

    @Bean
    TaskFacade taskFacade(TaskRepository taskRepository) {
        return new TaskFacade(taskRepository);
    }

    @Bean
    TaskEventHandler taskEventHandler(TaskRepository taskRepository) {
        return new TaskEventHandler(taskRepository, new PropertiesClassifier());
    }

    @Bean
    TaskProcessor personEventProcessor(TaskEventHandler taskEventHandler,
                                       TaskRepository taskRepository
    ) {
        return new TaskProcessor(taskEventHandler, taskRepository);
    }

    @Bean
    PersonUpsertedEventListener personUpsertedEventListener(TaskProcessor taskProcessor) {
        return new PersonUpsertedEventListener(taskProcessor);
    }
}
