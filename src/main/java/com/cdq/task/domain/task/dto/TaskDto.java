package com.cdq.task.domain.task.dto;

public record TaskDto(
    String status,
    int percentageDone,
    ResultDto result
) {
    public record ResultDto(
        String firstNameClassification,
        String lastNameClassification,
        String birthDateClassification,
        String companyClassification
    ) {
    }
}
