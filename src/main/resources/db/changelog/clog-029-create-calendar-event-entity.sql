--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE calendar_event_entity
(
    id                VARCHAR(255) NOT NULL,
    calendar_id       VARCHAR(255) NOT NULL,
    title             VARCHAR(255) NOT NULL,
    event_description VARCHAR(255) NULL,
    start_date        datetime     NOT NULL,
    end_date          datetime     NULL,
    is_entire_day     BIT(1)       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (calendar_id) REFERENCES calendar_entity(id)
);