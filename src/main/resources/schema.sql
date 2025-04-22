-- This should be done via migration tool like Flyway / Liquibase for SQL or if DB will be NoSQL then other tool (for Mongo e.g. mongock)

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    company VARCHAR(255),
    id_number VARCHAR(255) UNIQUE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE task (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status VARCHAR(255) NOT NULL,
    percentage_done INT NOT NULL,
    first_name_classification_type VARCHAR(255),
    last_name_classification_type VARCHAR(255),
    birth_date_classification_type VARCHAR(255),
    company_classification_type VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0
);
