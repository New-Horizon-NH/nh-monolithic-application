--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE exam_entity
(
    id                VARCHAR(36) NOT NULL,
    medical_chart_id  VARCHAR(36) NOT NULL,
    exam_type_id      VARCHAR(36) NOT NULL,
    request_timestamp datetime     NOT NULL,
    exam_date_time    datetime NULL,
    notes             VARCHAR(255) NULL,
    exam_status       INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id),
    FOREIGN KEY (exam_type_id) REFERENCES exam_type_entity (id)
);