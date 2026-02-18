--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE medical_team_entity
(
    id                    VARCHAR(255) NOT NULL,
    fiscal_code           VARCHAR(255) NOT NULL,
    name                  VARCHAR(255) NOT NULL,
    surname               VARCHAR(255) NOT NULL,
    comma_separated_roles VARCHAR(255) NOT NULL,
    is_enabled BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE medical_team_entity
    ADD CONSTRAINT unique_fiscal_code UNIQUE (fiscal_code);