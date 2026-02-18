--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE therapy_entity
(
    id                 VARCHAR(255) NOT NULL,
    medical_chart_id   VARCHAR(255) NOT NULL,
    medical_creator_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id),
    FOREIGN KEY (medical_creator_id) REFERENCES medical_entity (id)
);