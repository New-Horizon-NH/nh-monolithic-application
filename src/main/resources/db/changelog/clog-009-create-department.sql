--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE department_entity
(
    id                     VARCHAR(36) NOT NULL,
    department_name        VARCHAR(255) NOT NULL,
    code                   VARCHAR(255) NOT NULL,
    department_description VARCHAR(255) NULL,
    location               VARCHAR(255) NOT NULL,
    director               VARCHAR(255) NOT NULL,
    phone                  VARCHAR(255) NOT NULL,
    email                  VARCHAR(255) NOT NULL,
    coordinator            VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE department_entity
    ADD CONSTRAINT unique_code UNIQUE (code);