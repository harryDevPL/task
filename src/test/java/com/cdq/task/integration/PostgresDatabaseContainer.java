package com.cdq.task.integration;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class PostgresDatabaseContainer {
    private static final String IMAGE_VERSION = "postgres:15";
    private static final String DATABASE_NAME = "test-db";
    private static final String USERNAME = "test-user";
    private static final String PASSWORD = "test-password";

    private final PostgreSQLContainer<?> container;

    @SuppressWarnings("resource")
    public PostgresDatabaseContainer() {
        container = new PostgreSQLContainer<>(DockerImageName.parse(IMAGE_VERSION))
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withReuse(true);
        container.start();
        container.waitingFor(Wait.forListeningPort());
    }

    public String getUsername() {
        return container.getUsername();
    }

    public String getPassword() {
        return container.getPassword();
    }

    public String getR2dbcUrl() {
        return "r2dbc:postgresql://" + container.getHost() + ":" + container.getMappedPort(5432) + "/" + container.getDatabaseName();
    }
}
