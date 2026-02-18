--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE medical_chart_entity
(
    id           VARCHAR(36) NOT NULL,
    patient_id   VARCHAR(36) NOT NULL,
    opening_date datetime     NOT NULL,
    closing_date datetime NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES patient_entity (id)
);