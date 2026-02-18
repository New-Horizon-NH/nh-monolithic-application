--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE exam_result_entity
(
    id                VARCHAR(36) NOT NULL,
    exam_id           VARCHAR(36) NOT NULL,
    upload_file_path   VARCHAR(255) NOT NULL,
    publish_timestamp datetime     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (exam_id) REFERENCES exam_entity (id)
);