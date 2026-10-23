-- ============================================================
-- FINVEST - Minimal reference data for integration tests
-- ============================================================

INSERT INTO currencies (code)
VALUES ('EUR'),
       ('USD');

INSERT INTO banks (bic, name, logo)
VALUES ('CMBRFR2B', 'Crédit Mutuel Arkéa', NULL),
       ('FTNOFRP1', 'Fortuneo', 'fortuneo.png');

INSERT INTO account_statuses (code)
VALUES ('ACTIVE'),
       ('CLOSED');

INSERT INTO management_types (code)
VALUES ('FREE_MANAGEMENT'),
       ('MANAGED');

INSERT INTO account_types (code)
VALUES ('COMPTE_COURANT'),
       ('LIVRET'),
       ('PEA'),
       ('COMPTE_TITRES'),
       ('ASSURANCE_VIE'),
       ('PER'),
       ('PEE');

-- Internal users: romain@example.com, test@example.com
-- External user: john.doe@example.com
INSERT INTO personal_informations (first_name, last_name, email)
VALUES ('Romain', 'Rousval', 'romain@example.com'),
       ('Test', 'User', 'test@example.com'),
       ('John', 'Doe', 'john.doe@example.com');

INSERT INTO users (personal_information_id, password)
SELECT id, 'hash'
FROM personal_informations
WHERE email IN ('romain@example.com', 'test@example.com')
ORDER BY id;

INSERT INTO users_external (personal_information_id)
SELECT id
FROM personal_informations
WHERE email = 'john.doe@example.com';
