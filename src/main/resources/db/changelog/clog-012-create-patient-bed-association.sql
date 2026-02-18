--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE patient_bed_association_entity
(
    id             VARCHAR(36) NOT NULL,
    timestamp      datetime     NOT NULL,
    patient_id     VARCHAR(36) NOT NULL,
    bed_id         VARCHAR(36) NOT NULL,
    operation_type INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES patient_entity (id),
    FOREIGN KEY (bed_id) REFERENCES department_room_bed_entity (id)
);
