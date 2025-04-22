package com.cdq.task.integration;

import com.cdq.task.api.person.UpsertPersonRequest;
import com.cdq.task.api.person.UpsertPersonResponse;
import com.cdq.task.api.task.GetTaskResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    static final PostgresDatabaseContainer postgresContainer;

    static {
        try {
            postgresContainer = new PostgresDatabaseContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @LocalServerPort
    int serverPort;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", postgresContainer::getR2dbcUrl);
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }

    @BeforeAll
    void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = serverPort;
    }

    protected String createPerson(UpsertPersonRequest request) {
        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/persons")
            .then()
            .statusCode(200)
            .extract()
            .as(UpsertPersonResponse.class)
            .taskId();
    }

    protected GetTaskResponse getTask(String taskId) {
        return given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/tasks/{taskId}", taskId)
            .then()
            .statusCode(200)
            .extract()
            .as(GetTaskResponse.class);
    }

    protected String createRandomUpsertPersonRequest() {
        String firstName = "John-" + UUID.randomUUID().toString().substring(0, 5);
        String lastName = "Doe-" + UUID.randomUUID().toString().substring(0, 5);
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2000, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        String birthDate = LocalDate.ofEpochDay(randomDay).toString();
        String companyName = "Company-" + UUID.randomUUID().toString().substring(0, 5);
        String pesel = String.valueOf(10000000000L + ThreadLocalRandom.current().nextLong(0, 8999999999L));

        UpsertPersonRequest upsertPersonRequest = new UpsertPersonRequest(
            firstName,
            lastName,
            birthDate,
            companyName,
            pesel
        );

        return createPerson(upsertPersonRequest);
    }

    protected void assertIsValidUUID(String value) {
        assertThat(value).isNotBlank();
        assertThatCode(() -> UUID.fromString(value)).doesNotThrowAnyException();
    }
}
