--liquibase formatted sql
--changeset liquibase:001
CREATE TABLE medical_entity
(
    id                    VARCHAR(255) NOT NULL,
    fiscal_code           VARCHAR(255) NOT NULL,
    name                  VARCHAR(255) NOT NULL,
    surname               VARCHAR(255) NOT NULL,
    comma_separated_roles VARCHAR(255) NOT NULL,
    is_enabled            BIT(1)       NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE medical_entity
    ADD CONSTRAINT unique_fiscal_code UNIQUE (fiscal_code);

CREATE TRIGGER after_medical_team_entity_insert
    AFTER INSERT
    ON medical_team_entity
    FOR EACH ROW
BEGIN
    INSERT INTO medical_entity (id, fiscal_code, name, surname, comma_separated_roles, is_enabled)
    VALUES (NEW.id, NEW.fiscal_code, NEW.name, NEW.surname, NEW.comma_separated_roles, NEW.is_enabled);
END;

CREATE TRIGGER after_medical_team_entity_update
    AFTER UPDATE
    ON medical_team_entity
    FOR EACH ROW
BEGIN
    UPDATE medical_entity
    SET fiscal_code           = NEW.fiscal_code,
        name                  = NEW.name,
        surname               = NEW.surname,
        comma_separated_roles = NEW.comma_separated_roles,
        is_enabled            = NEW.is_enabled
    WHERE id = NEW.id;
END;

CREATE TRIGGER after_medical_team_entity_delete
    AFTER DELETE
    ON medical_team_entity
    FOR EACH ROW
BEGIN
    DELETE
    FROM medical_entity
    WHERE id = OLD.id;
END;