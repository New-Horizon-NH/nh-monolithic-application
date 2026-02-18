--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE department_room_entity
(
    id            VARCHAR(36) NOT NULL,
    department_id VARCHAR(36) NOT NULL,
    room_number   INT          NOT NULL,
    bed_count     INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (department_id) REFERENCES department_entity (id)
);