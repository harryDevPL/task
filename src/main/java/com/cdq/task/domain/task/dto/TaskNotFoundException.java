package com.cdq.task.domain.task.dto;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID taskId) {
        super("Task with ID [%s] not found".formatted(taskId));
    }
}
