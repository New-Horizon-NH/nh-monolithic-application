--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE nursing_rating_scale_registry_entity
(
    id                  VARCHAR(255) NOT NULL,
    medical_chart_id    VARCHAR(255) NOT NULL,
    nursing_scale_code  INT          NOT NULL,
    nursing_scale_value INT          NOT NULL,
    timestamp           datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id)
);