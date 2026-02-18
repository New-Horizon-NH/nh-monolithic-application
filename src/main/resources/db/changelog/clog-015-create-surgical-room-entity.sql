--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE surgical_room_entity
(
    id          VARCHAR(36) NOT NULL,
    room_number INT          NOT NULL,
    room_type   INT          NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE surgical_room_entity
    ADD CONSTRAINT unique_room_number UNIQUE (room_number);