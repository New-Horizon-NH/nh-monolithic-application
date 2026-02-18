--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE drug_package_entity
(
    id                          VARCHAR(36) NOT NULL,
    drug_id                     VARCHAR(36) NOT NULL,
    package_id                  VARCHAR(36) NOT NULL,
    name                        VARCHAR(255) NOT NULL,
    aic_code                    VARCHAR(255) NOT NULL,
    forniture_class             VARCHAR(255) NOT NULL,
    forniture_class_description VARCHAR(255) NOT NULL,
    refundability_class         VARCHAR(255) NOT NULL,
    quantity                    BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (drug_id) REFERENCES drug_entity(id)
);

ALTER TABLE drug_package_entity
    ADD CONSTRAINT unique_package_id UNIQUE (package_id);