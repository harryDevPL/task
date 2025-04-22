package com.cdq.task.integration;

import com.cdq.task.api.task.GetTaskResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskIntegrationTest extends BaseIntegrationTest {

    @Test
    void whenPersonIsCreatedThenShouldGetTaskByTaskId() {
        // Given
        String taskId = createRandomUpsertPersonRequest();

        // and
        assertIsValidUUID(taskId);

        // When
        GetTaskResponse taskResponse = getTask(taskId);

        // Then
        assertThat(taskResponse).isNotNull();
        assertThat(taskResponse.taskId()).isEqualTo(taskId);
        assertThat(taskResponse.status()).isNotNull();
        assertThat(taskResponse.status()).isIn("PENDING", "IN_PROGRESS", "COMPLETED");
    }


    // Other tests, unhappy path, etc.
}
