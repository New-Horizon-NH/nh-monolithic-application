--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE medical_work_shift_association_entity
(
    id            VARCHAR(255) NOT NULL,
    work_shift_id VARCHAR(255) NOT NULL,
    medical_id    VARCHAR(255) NOT NULL,
    shift_date    date         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (work_shift_id) REFERENCES work_shift_entity(id),
    FOREIGN KEY (medical_id) REFERENCES medical_entity(id)
);