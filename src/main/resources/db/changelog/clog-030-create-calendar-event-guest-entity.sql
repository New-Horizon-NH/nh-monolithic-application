--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE calendar_event_guest_entity
(
    id       VARCHAR(255) NOT NULL,
    event_id VARCHAR(255) NOT NULL,
    guest_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES calendar_event_entity(id)
);