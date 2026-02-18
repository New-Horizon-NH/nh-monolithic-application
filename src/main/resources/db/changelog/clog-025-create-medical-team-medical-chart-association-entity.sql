--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE medical_chart_medical_team_association_entity
(
    id               VARCHAR(255) NOT NULL,
    medical_chart_id VARCHAR(255) NOT NULL,
    medical_team_id  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id),
    FOREIGN KEY (medical_team_id) REFERENCES medical_team_entity (id)
);