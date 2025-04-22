package com.cdq.task.domain.task;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
class TaskResult {

    @Enumerated(EnumType.STRING)
    ClassificationType firstNameClassificationType;

    @Enumerated(EnumType.STRING)
    ClassificationType lastNameClassificationType;

    @Enumerated(EnumType.STRING)
    ClassificationType birthDateClassificationType;

    @Enumerated(EnumType.STRING)
    ClassificationType companyClassificationType;
}
