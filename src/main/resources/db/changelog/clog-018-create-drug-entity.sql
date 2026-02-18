--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE drug_entity
(
    id                      VARCHAR(36)  NOT NULL,
    name                    VARCHAR(191) NOT NULL,
    code                    VARCHAR(255) NOT NULL,
    pharmaceutical_company  VARCHAR(191) NOT NULL,
    pharmaceutical_form     VARCHAR(191) NOT NULL,
    dosage_form_description VARCHAR(191) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE drug_entity
    ADD CONSTRAINT unique_drug UNIQUE (name, pharmaceutical_company, pharmaceutical_form, dosage_form_description);
