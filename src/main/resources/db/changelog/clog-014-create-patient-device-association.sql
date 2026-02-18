--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE patient_device_association_entity
(
    id             VARCHAR(36) NOT NULL,
    timestamp      datetime     NOT NULL,
    patient_id     VARCHAR(36) NOT NULL,
    device_id      VARCHAR(36) NOT NULL,
    operation_type INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES patient_entity (id),
    FOREIGN KEY (device_id) REFERENCES device_entity (id)
);