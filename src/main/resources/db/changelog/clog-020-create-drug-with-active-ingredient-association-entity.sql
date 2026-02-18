--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE drug_with_drug_active_ingredient_association_entity
(
    id                   VARCHAR(36) NOT NULL,
    drug_id              VARCHAR(36) NOT NULL,
    active_ingredient_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (drug_id) REFERENCES drug_entity (id),
    FOREIGN KEY (active_ingredient_id) REFERENCES drug_active_ingredient_entity (id)
);

ALTER TABLE drug_with_drug_active_ingredient_association_entity
    ADD CONSTRAINT unique_association UNIQUE (drug_id, active_ingredient_id);