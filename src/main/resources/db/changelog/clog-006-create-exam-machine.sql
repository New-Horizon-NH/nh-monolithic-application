--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE exam_machine_entity
(
    id                    VARCHAR(36) NOT NULL,
    exam_type_id          VARCHAR(36) NOT NULL,
    machine_serial_number VARCHAR(255) NOT NULL,
    machine_name          VARCHAR(255) NOT NULL,
    is_enabled            BIT(1)       NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (exam_type_id) REFERENCES exam_type_entity (id)
);

ALTER TABLE exam_machine_entity
    ADD CONSTRAINT unique_serial UNIQUE (machine_serial_number);