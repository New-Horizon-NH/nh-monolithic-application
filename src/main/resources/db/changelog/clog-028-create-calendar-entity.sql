--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE calendar_entity
(
    id                   VARCHAR(255) NOT NULL,
    medical_id           VARCHAR(255) NOT NULL,
    title                VARCHAR(255) NOT NULL,
    calendar_description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_id) REFERENCES medical_entity (id)
);