--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE address_entity
(
    id       VARCHAR(36) NOT NULL,
    street   VARCHAR(255) NOT NULL,
    number   VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    city     VARCHAR(255) NOT NULL,
    province VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);