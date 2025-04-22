package com.cdq.task.domain.task;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

interface TaskRepository extends ReactiveCrudRepository<Task, UUID> {

    @Query("INSERT INTO task (id, status, percentage_done, version) VALUES ($1, $2, $3, $4)")
    Mono<Void> insert(UUID id, String status, int percentageDone, long version);

    @Query("SELECT * FROM task OFFSET :offset LIMIT :limit")
    Flux<Task> findAllWithPaging(int offset, int limit);
}
