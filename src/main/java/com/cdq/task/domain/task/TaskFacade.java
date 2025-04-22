package com.cdq.task.domain.task;

import com.cdq.task.api.task.GetTaskResponse;
import com.cdq.task.domain.task.dto.TaskDto;
import com.cdq.task.domain.task.dto.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskFacade {

    private final TaskRepository taskRepository;

    public Mono<GetTaskResponse> getTask(UUID taskId) {
        return taskRepository.findById(taskId)
            .switchIfEmpty(Mono.error(new TaskNotFoundException(taskId)))
            .map(this::mapToGetTaskResponse);
    }

    public Flux<GetTaskResponse> getAllTasks(int offset, int size) {
        return taskRepository.findAllWithPaging(offset, size)
            .map(this::mapToGetTaskResponse);
    }

    private GetTaskResponse mapToGetTaskResponse(Task task) {
        TaskDto dto = task.toDto();
        return GetTaskResponse.builder()
            .taskId(task.getId().toString())
            .status(dto.status())
            .percentageDone(String.valueOf(dto.percentageDone()))
            .result(GetTaskResponse.TaskResultResponse.builder()
                .firstNameClassification(dto.result().firstNameClassification())
                .lastNameClassification(dto.result().lastNameClassification())
                .birthDateClassification(dto.result().birthDateClassification())
                .companyClassification(dto.result().companyClassification())
                .build()
            )
            .build();
    }
}
