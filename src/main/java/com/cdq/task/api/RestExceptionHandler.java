package com.cdq.task.api;

import com.cdq.task.domain.task.dto.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleTaskNotFound(TaskNotFoundException exception) {
        Map<String, Object> errorBody = Map.of(
            "timestamp", Instant.now().toString(),
            "status", HttpStatus.NOT_FOUND.value(),
            "error", "Not Found",
            "message", exception.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody));
    }

    // Other exceptions ...
}
