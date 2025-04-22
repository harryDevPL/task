package com.cdq.task.domain.person.dto;

import com.cdq.task.api.person.UpsertPersonRequest;

public record UpsertPersonCommand(
    String firstName,
    String lastName,
    String birthDate,
    String company,
    String idNumber
) {
    public static UpsertPersonCommand from(UpsertPersonRequest request) {
        return new UpsertPersonCommand(
            request.firstName(),
            request.lastName(),
            request.birthDate(),
            request.company(),
            request.idNumber()
        );
    }
}
