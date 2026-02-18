--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE therapy_record_entity
(
    id                    VARCHAR(255) NOT NULL,
    therapy_id            VARCHAR(255) NOT NULL,
    package_id            VARCHAR(255) NOT NULL,
    administration_number INT          NOT NULL,
    administration_type   INT          NOT NULL,
    medical_assignee_id   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (therapy_id) REFERENCES therapy_entity (id),
    FOREIGN KEY (package_id) REFERENCES drug_package_entity (package_id),
    FOREIGN KEY (medical_assignee_id) REFERENCES medical_entity (id)
);