--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE exam_type_entity
(
    id               VARCHAR(36) NOT NULL,
    exam_code        VARCHAR(255) NOT NULL,
    exam_name        VARCHAR(255) NOT NULL,
    exam_description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE exam_type_entity
    ADD CONSTRAINT unique_code UNIQUE (exam_code);