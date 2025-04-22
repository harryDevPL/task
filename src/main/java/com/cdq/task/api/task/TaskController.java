package com.cdq.task.api.task;

import com.cdq.task.domain.task.TaskFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
class TaskController {

    private final TaskFacade taskFacade;

    @GetMapping("/{taskId}")
    // Cache can be added for tasks with status COMPLETED
    // with desired configuration (like ttl)
    // For caching Redis/Hazelcast can be used (or even in memory cache)
    public Mono<GetTaskResponse> getSingleTask(@PathVariable UUID taskId) {
        log.info("Received request to fetch task data with ID: [{}]", taskId.toString());
        return taskFacade.getTask(taskId);
    }

    @GetMapping("/")
    // Cache can be added for tasks with status COMPLETED
    // with desired configuration (like ttl)
    // For caching Redis/Hazelcast can be used (or even in memory cache)
    public Flux<GetTaskResponse> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size
    ) {
        int offset = page * size;
        log.info("Fetching tasks with offset {} and size {}", offset, size);
        return taskFacade.getAllTasks(offset, size);
    }
}
