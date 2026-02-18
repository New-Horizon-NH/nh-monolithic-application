--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE emergency_room_document_entity
(
    id                VARCHAR(255) NOT NULL,
    medical_chart_id  VARCHAR(255) NOT NULL,
    upload_file_path  VARCHAR(255) NOT NULL,
    publish_timestamp datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id)
);