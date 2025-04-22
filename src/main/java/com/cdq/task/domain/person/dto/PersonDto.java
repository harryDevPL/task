package com.cdq.task.domain.person.dto;

public record PersonDto(
    String firstName,
    String lastName,
    String birthDate,
    String company,
    String idNumber
) {
}

