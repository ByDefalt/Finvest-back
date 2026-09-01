-- Users
INSERT INTO users (email, password)
VALUES
    ('romain@example.com', '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.'),
    ('test@example.com', '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.');
//test-password-hash: $2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.
-- Accounts
INSERT INTO accounts (name, type, balance, user_id)
SELECT 'Compte courant', 'COMPTE_COURANT', 2500.00, id
FROM users
WHERE email = 'romain@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'Livret A', 'LIVRET', 10000.00, id
FROM users
WHERE email = 'romain@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'PEA', 'PEA', 25000.00, id
FROM users
WHERE email = 'romain@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'PER', 'PER', 8000.00, id
FROM users
WHERE email = 'romain@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'PEE', 'PEE', 15000.00, id
FROM users
WHERE email = 'romain@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'Assurance vie', 'ASSURANCE_VIE', 12000.00, id
FROM users
WHERE email = 'romain@example.com';


-- Accounts du deuxième utilisateur
INSERT INTO accounts (name, type, balance, user_id)
SELECT 'Compte courant', 'COMPTE_COURANT', 1800.00, id
FROM users
WHERE email = 'test@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'Livret A', 'LIVRET', 7500.00, id
FROM users
WHERE email = 'test@example.com';

INSERT INTO accounts (name, type, balance, user_id)
SELECT 'PEA', 'PEA', 18500.00, id
FROM users
WHERE email = 'test@example.com';