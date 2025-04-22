package com.cdq.task.api.task;

import lombok.Builder;

@Builder
public record GetTaskResponse(
    String taskId,
    String status,
    String percentageDone,
    TaskResultResponse result
) {
    @Builder
    public record TaskResultResponse(
        String firstNameClassification,
        String lastNameClassification,
        String birthDateClassification,
        String companyClassification
    ) {
    }
}
