--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE patient_entity
(
    id                  VARCHAR(36) NOT NULL,
    patient_name        VARCHAR(36) NOT NULL,
    patient_surname     VARCHAR(255) NOT NULL,
    date_of_birth       date         NOT NULL,
    patient_fiscal_code VARCHAR(255) NOT NULL,
    patient_gender      INT          NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE patient_entity
    ADD CONSTRAINT unique_cf UNIQUE (patient_fiscal_code);