--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE device_entity
(
    id                     VARCHAR(36) NOT NULL,
    device_serial_number   VARCHAR(255) NOT NULL,
    device_tls_certificate VARCHAR(255) NOT NULL,
    device_type            INT          NOT NULL,
    is_enabled             BIT(1)       NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE device_entity
    ADD CONSTRAINT unique_serial UNIQUE (device_serial_number);

ALTER TABLE device_entity
    ADD CONSTRAINT unique_certificate UNIQUE (device_tls_certificate);