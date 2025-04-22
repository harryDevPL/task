package com.cdq.task.domain.person.dto;

import java.util.UUID;

public record PersonUpsertedEvent(
    UUID taskId,
    PersonDto oldPerson,
    PersonDto newPerson
) {
}
