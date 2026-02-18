--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE department_room_bed_entity
(
    id                VARCHAR(36) NOT NULL,
    room_id           VARCHAR(36) NOT NULL,
    bed_number        INT          NOT NULL,
    is_motorized BIT(1) NOT NULL DEFAULT 0,
    bed_serial_number VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES department_room_entity (id)
);

ALTER TABLE department_room_bed_entity
    ADD CONSTRAINT unique_serial UNIQUE (bed_serial_number);