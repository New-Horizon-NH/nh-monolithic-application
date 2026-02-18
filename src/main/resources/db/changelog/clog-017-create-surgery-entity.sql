--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE surgery_entity
(
    id               VARCHAR(36) NOT NULL,
    surgical_room_id VARCHAR(36) NOT NULL,
    surgery_start  datetime NOT NULL,
    surgery_end    datetime NOT NULL,
    medical_chart_id VARCHAR(36) NOT NULL,
    surgery_type_id  VARCHAR(36) NOT NULL,
    surgery_status INT      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (surgical_room_id) REFERENCES surgical_room_entity (id),
    FOREIGN KEY (medical_chart_id) REFERENCES medical_chart_entity (id),
    FOREIGN KEY (surgery_type_id) REFERENCES surgery_type_entity (id)
);