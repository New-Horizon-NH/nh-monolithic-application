--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE patient_event_association_entity
(
    id         VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    event_id   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES calendar_event_entity(id),
    FOREIGN KEY (patient_id) REFERENCES patient_entity(id)
);