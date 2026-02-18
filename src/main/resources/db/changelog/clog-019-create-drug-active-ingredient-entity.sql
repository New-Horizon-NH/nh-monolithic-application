--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE drug_active_ingredient_entity
(
    id   VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE drug_active_ingredient_entity
    ADD CONSTRAINT unique_drug_name UNIQUE (name);