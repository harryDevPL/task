package com.cdq.task.domain.task;

import com.cdq.task.domain.task.dto.TaskDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private int percentageDone;

    @Enumerated(EnumType.STRING)
    private ClassificationType firstNameClassificationType;

    @Enumerated(EnumType.STRING)
    private ClassificationType lastNameClassificationType;

    @Enumerated(EnumType.STRING)
    private ClassificationType birthDateClassificationType;

    @Enumerated(EnumType.STRING)
    private ClassificationType companyClassificationType;

    @Version
    private long version;

    protected TaskDto toDto() {
        return new TaskDto(
            this.status.name(),
            this.percentageDone,
            new TaskDto.ResultDto(
                safeName(firstNameClassificationType),
                safeName(lastNameClassificationType),
                safeName(birthDateClassificationType),
                safeName(companyClassificationType)
            )
        );
    }

    private String safeName(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }
}
