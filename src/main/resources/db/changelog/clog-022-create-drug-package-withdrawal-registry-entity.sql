--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE drug_package_withdrawal_registry_entity
(
    id         VARCHAR(36)  NOT NULL,
    package_id VARCHAR(36)  NOT NULL,
    quantity   BIGINT       NOT NULL,
    user_id    VARCHAR(255) NOT NULL,
    timestamp  datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (package_id) REFERENCES drug_package_entity (id)
);