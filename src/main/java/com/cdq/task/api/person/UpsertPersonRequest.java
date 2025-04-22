package com.cdq.task.api.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpsertPersonRequest(
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "birthDate must be in format YYYY-MM-DD")
    String birthDate,
    String company,
    @Pattern(regexp = "\\d{11}", message = "idNumber must be a valid Polish PESEL (11 digits)")
    String idNumber
) {
}
