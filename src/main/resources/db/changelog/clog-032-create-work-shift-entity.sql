--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE work_shift_entity
(
    id         VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    shift_code INT          NOT NULL,
    start_time time         NOT NULL,
    end_time   time         NOT NULL,
    PRIMARY KEY (id)
);