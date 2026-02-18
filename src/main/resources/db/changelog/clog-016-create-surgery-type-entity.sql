--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE surgery_type_entity
(
    id                  VARCHAR(36) NOT NULL,
    surgery_code        VARCHAR(255) NOT NULL,
    surgery_name        VARCHAR(255) NOT NULL,
    surgery_description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE surgery_type_entity
    ADD CONSTRAINT unique_code UNIQUE (surgery_code);