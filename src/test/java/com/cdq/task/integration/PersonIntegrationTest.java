package com.cdq.task.integration;

import com.cdq.task.api.person.UpsertPersonRequest;
import org.junit.jupiter.api.Test;

class PersonIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreatePersonAndGetTask() {
        // Given
        UpsertPersonRequest request = new UpsertPersonRequest(
            "John",
            "Doe",
            "1990-01-01",
            "Acme Inc.",
            "12345678901"
        );

        // When
        String taskId = createPerson(request);

        // Then
        assertIsValidUUID(taskId);
    }

    // Other tests, unhappy path, etc.
}
