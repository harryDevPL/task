package com.cdq.task.domain.person;

import com.cdq.task.domain.person.dto.PersonDto;
import com.cdq.task.domain.person.dto.UpsertPersonCommand;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Person {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String company;
    private String idNumber;

    @Version
    private long version;

    protected static Person from(UpsertPersonCommand command) {
        return new Person(
            UUID.randomUUID(),
            command.firstName(),
            command.lastName(),
            LocalDate.parse(command.birthDate()),
            command.company(),
            command.idNumber(),
            0
        );
    }

    protected void updateFrom(UpsertPersonCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.birthDate = LocalDate.parse(command.birthDate());
        this.company = command.company();
        this.idNumber = command.idNumber();
    }

    protected PersonDto toDto() {
        return new PersonDto(this.firstName, this.lastName, this.birthDate.toString(), this.company, this.getIdNumber());
    }
}
