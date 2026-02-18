--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE patient_address_association_entity
(
    id           VARCHAR(36) NOT NULL,
    patient_id   VARCHAR(36) NOT NULL,
    address_id   VARCHAR(36) NOT NULL,
    address_type INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES patient_entity (id),
    FOREIGN KEY (address_id) REFERENCES address_entity (id)
);