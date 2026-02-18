--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE therapy_administration_registry_entity
(
    id                     VARCHAR(255) NOT NULL,
    therapy_record_id      VARCHAR(255) NOT NULL,
    administration_instant datetime     NOT NULL,
    administrator_id       VARCHAR(255) NOT NULL,
    administration_status  INT          NOT NULL,
    extra_info             VARCHAR(255) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (therapy_record_id) REFERENCES therapy_record_entity(id),
    FOREIGN KEY (administrator_id) REFERENCES medical_entity (id)
);
