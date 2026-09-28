-- ============================================================
-- FINVEST - DATA TEST
-- MariaDB
-- ============================================================



INSERT INTO personal_informations (first_name, last_name, email)
VALUES ('Romain', 'Rousval', 'romain@example.com'),
       ('Test', 'User', 'test@example.com'),
       ('John', 'Doe', 'john.doe@example.com'),
       ('Jane', 'Smith', 'jane.smith@example.com');


-- ============================================================
-- USERS
-- ============================================================

-- test-password-hash: $2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.
INSERT INTO users (personal_information_id, password)
VALUES ((SELECT id FROM personal_informations WHERE email = 'romain@example.com'),
        '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.'),
       ((SELECT id FROM personal_informations WHERE email = 'test@example.com'),
        '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.');



INSERT INTO users_external (personal_information_id)
VALUES ((SELECT id FROM personal_informations WHERE email = 'john.doe@example.com')),
       ((SELECT id FROM personal_informations WHERE email = 'jane.smith@example.com'));


-- ============================================================
-- CURRENCIES
-- ============================================================

INSERT INTO currencies (code)
VALUES ('EUR'),
       ('USD'),
       ('GBP'),
       ('JPY'),
       ('CHF');


-- ============================================================
-- COUNTRIES
-- ============================================================

INSERT INTO countries (code)
VALUES ('FR'),
       ('US'),
       ('GB'),
       ('DE'),
       ('JP'),
       ('CH'),
       ('NL'),
       ('LU'),
       ('IE'),
       ('CA');


-- ============================================================
-- BANKS
-- ============================================================

INSERT INTO banks (name, bic, logo)
VALUES ('Crédit Mutuel Arkéa', 'CMBRFR2B', NULL),
       ('Fortuneo', 'FTNOFRP1', NULL),
       ('Boursobank', 'BOUSFRPP', NULL),
       ('AXA', 'AXABFRPP', NULL);


-- ============================================================
-- ACCOUNT STATUSES
-- ============================================================

INSERT INTO account_statuses (code)
VALUES ('ACTIVE'),
       ('CLOSED'),
       ('BLOCKED');


-- ============================================================
-- MANAGEMENT TYPES
-- ============================================================

INSERT INTO management_types (code)
VALUES ('FREE_MANAGEMENT'),
       ('MANAGED'),
       ('ADVISED');


-- ============================================================
-- ASSET TYPES
-- ============================================================

INSERT INTO asset_types (code)
VALUES ('STOCK'),
       ('ETF'),
       ('BOND'),
       ('FUND'),
       ('SCPI'),
       ('OPCI'),
       ('CRYPTO'),
       ('CASH');


-- ============================================================
-- TRANSACTION TYPES
-- ============================================================

INSERT INTO transaction_types (code)
VALUES ('DEPOSIT'),
       ('WITHDRAWAL'),
       ('TRANSFER'),
       ('DIVIDEND'),
       ('INTEREST'),
       ('FEE'),
       ('TAX');


-- ============================================================
-- TRADE TYPES
-- ============================================================

INSERT INTO trade_types (code)
VALUES ('BUY'),
       ('SELL');


-- ============================================================
-- ACCOUNT TYPES
-- ============================================================

INSERT INTO account_types (code)
VALUES ('COMPTE_COURANT'),
       ('LIVRET'),
       ('PEA'),
       ('COMPTE_TITRES'),
       ('ASSURANCE_VIE'),
       ('PER'),
       ('PEE');


-- ============================================================
-- ACCOUNTS - ROMAIN
-- ============================================================

-- ------------------------------------------------------------
-- Compte courant Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'Compte courant Romain',
       2500.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Compte courant principal',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'COMPTE_COURANT';


-- ------------------------------------------------------------
-- Livret A Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'Livret A Romain',
       10000.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Épargne de précaution',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'LIVRET';


-- ------------------------------------------------------------
-- PEA Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'PEA Romain',
       25000.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Plan d''épargne en actions',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Fortuneo'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'PEA';


-- ------------------------------------------------------------
-- PER Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'PER Romain',
       8000.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Plan épargne retraite',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'AXA'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'PER';


-- ------------------------------------------------------------
-- PEE Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'PEE Romain',
       15000.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Plan épargne entreprise',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'PEE';


-- ------------------------------------------------------------
-- Assurance vie Romain
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'Assurance vie Romain',
       12000.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Contrat d''assurance vie',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'AXA'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'ASSURANCE_VIE';


-- ============================================================
-- ACCOUNTS - TEST
-- ============================================================

-- ------------------------------------------------------------
-- Compte courant Test
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'Compte courant Test',
       1800.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Compte courant principal',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Boursobank'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'COMPTE_COURANT';


-- ------------------------------------------------------------
-- Livret A Test
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'Livret A Test',
       7500.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Épargne',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Boursobank'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'LIVRET';


-- ------------------------------------------------------------
-- PEA Test
-- ------------------------------------------------------------

INSERT INTO accounts
(bank_bic, name, balance, currency_code, created_at, account_status_code, description, account_type_code)
SELECT b.bic,
       'PEA Test',
       18500.00,
       c.code,
       CURRENT_TIMESTAMP,
       s.code,
       'Plan d''épargne en actions',
       at.code
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
         CROSS JOIN account_types at
WHERE b.name = 'Fortuneo'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE'
  AND at.code = 'PEA';


-- ============================================================
-- ACCOUNT OWNERS
-- ============================================================

-- ------------------------------------------------------------
-- Comptes de Romain
-- ------------------------------------------------------------

INSERT INTO account_owners
(account_id, user_id, user_external_id, ownership_percentage)
SELECT a.id,
       u.id,
       null,
       100.00
FROM accounts a
         CROSS JOIN users u
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE pi.email = 'romain@example.com'
  AND a.name IN (
                 'Compte courant Romain',
                 'Livret A Romain',
                 'PEA Romain',
                 'PER Romain',
                 'PEE Romain',
                 'Assurance vie Romain'
    );


-- ------------------------------------------------------------
-- Comptes du deuxième utilisateur
-- ------------------------------------------------------------

INSERT INTO account_owners
(account_id, user_id, user_external_id, ownership_percentage)
SELECT a.id,
       u.id,
       null,
       100.00
FROM accounts a
         CROSS JOIN users u
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE pi.email = 'test@example.com'
  AND a.name IN (
                 'Compte courant Test',
                 'Livret A Test',
                 'PEA Test'
    );


-- ============================================================
-- COMPTE COURANT
-- ============================================================

-- ------------------------------------------------------------
-- Compte courant Romain
-- ------------------------------------------------------------

INSERT INTO compte_courants
(account_id, iban, bic, account_number, overdraft_limit, holder_name)
SELECT a.id,
       'FR7630006000011234567890189',
       b.bic,
       '12345678901',
       500.00,
       'Romain Rousval'
FROM accounts a
         JOIN banks b
              ON b.bic = a.bank_bic
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'Compte courant Romain'
  AND pi.email = 'romain@example.com';


-- ------------------------------------------------------------
-- Compte courant Test
-- ------------------------------------------------------------

INSERT INTO compte_courants
(account_id, iban, bic, account_number, overdraft_limit, holder_name)
SELECT a.id,
       'FR7630006000098765432109876',
       b.bic,
       '98765432109',
       300.00,
       'Utilisateur test'
FROM accounts a
         JOIN banks b
              ON b.bic = a.bank_bic
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'Compte courant Test'
  AND pi.email = 'test@example.com';


-- ============================================================
-- LIVRETS
-- ============================================================

INSERT INTO livrets
(account_id, interest_rate, ceiling)
SELECT a.id,
       1.70,
       22950.00
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'Livret A Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO livrets
(account_id, interest_rate, ceiling)
SELECT a.id,
       1.70,
       22950.00
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'Livret A Test'
  AND pi.email = 'test@example.com';


-- ============================================================
-- PEAs
-- ============================================================

INSERT INTO peas
(account_id, opening_date, deposit_limit)
SELECT a.id,
       '2022-03-15',
       150000.00
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO peas
(account_id, opening_date, deposit_limit)
SELECT a.id,
       '2024-06-10',
       150000.00
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Test'
  AND pi.email = 'test@example.com';


-- ============================================================
-- PER
-- ============================================================

INSERT INTO pers
(account_id, contract_number, opening_date, management_type_code)
SELECT a.id,
       'PER-2025-000001',
       '2025-09-01',
       m.code
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN management_types m
WHERE a.name = 'PER Romain'
  AND pi.email = 'romain@example.com'
  AND m.code = 'MANAGED';


-- ============================================================
-- PEE
-- ============================================================

INSERT INTO pees
(account_id, opening_date, employer)
SELECT a.id,
       '2020-06-01',
       'Crédit Mutuel Arkéa'
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEE Romain'
  AND pi.email = 'romain@example.com';


-- ============================================================
-- ASSURANCE VIE
-- ============================================================

INSERT INTO assurances_vie
(account_id, contract_number, opening_date, management_type_code)
SELECT a.id,
       'AV-2021-000001',
       '2021-05-10',
       m.code
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN management_types m
WHERE a.name = 'Assurance vie Romain'
  AND pi.email = 'romain@example.com'
  AND m.code = 'FREE_MANAGEMENT';


-- ============================================================
-- ASSETS
-- ============================================================

INSERT INTO assets
(name, asset_type_code, isin, ticker, issuer_country_code, currency_code, description)
SELECT 'Apple Inc.',
       at.code,
       'US0378331005',
       'AAPL',
       co.code,
       cu.code,
       'Action Apple'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'STOCK'
  AND co.code = 'US'
  AND cu.code = 'USD';


INSERT INTO assets
(name, asset_type_code, isin, ticker, issuer_country_code, currency_code, description)
SELECT 'Microsoft Corporation',
       at.code,
       'US5949181045',
       'MSFT',
       co.code,
       cu.code,
       'Action Microsoft'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'STOCK'
  AND co.code = 'US'
  AND cu.code = 'USD';


INSERT INTO assets
(name, asset_type_code, isin, ticker, issuer_country_code, currency_code, description)
SELECT 'Amundi MSCI World',
       at.code,
       'LU1681043599',
       'CW8',
       co.code,
       cu.code,
       'ETF MSCI World'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'ETF'
  AND co.code = 'LU'
  AND cu.code = 'EUR';


INSERT INTO assets
(name, asset_type_code, isin, ticker, issuer_country_code, currency_code, description)
SELECT 'iShares Core S&P 500',
       at.code,
       'IE00B5BMR087',
       'CSPX',
       co.code,
       cu.code,
       'ETF S&P 500'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'ETF'
  AND co.code = 'IE'
  AND cu.code = 'USD';


-- ------------------------------------------------------------
-- SCPI
-- ------------------------------------------------------------
-- ISIN et ticker sont obligatoires dans le schéma.
-- Valeurs synthétiques uniquement pour les données de test.

INSERT INTO assets
(name, asset_type_code, isin, ticker, issuer_country_code, currency_code, description)
SELECT 'SCPI Immorente',
       at.code,
       'TEST-SCPI-IMMOR-001',
       'IMMOR',
       co.code,
       cu.code,
       'SCPI immobilière'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'SCPI'
  AND co.code = 'FR'
  AND cu.code = 'EUR';


-- ============================================================
-- ASSET COUNTRIES
-- ============================================================

INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       70.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'LU1681043599'
  AND c.code = 'US';


INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       6.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'LU1681043599'
  AND c.code = 'JP';


INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       4.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'LU1681043599'
  AND c.code = 'GB';


INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       3.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'LU1681043599'
  AND c.code = 'FR';


INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       17.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'LU1681043599'
  AND c.code = 'DE';


INSERT INTO asset_countries
(asset_id, country_code, weight)
SELECT a.id,
       c.code,
       100.00
FROM assets a
         CROSS JOIN countries c
WHERE a.isin = 'IE00B5BMR087'
  AND c.code = 'US';


-- ============================================================
-- ASSET PRICES
-- ============================================================

INSERT INTO asset_prices
(asset_id, price, currency_code, date)
SELECT a.id,
       230.50,
       c.code,
       CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c
              ON c.code = 'USD'
WHERE a.ticker = 'AAPL';


INSERT INTO asset_prices
(asset_id, price, currency_code, date)
SELECT a.id,
       510.25,
       c.code,
       CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c
              ON c.code = 'USD'
WHERE a.ticker = 'MSFT';


INSERT INTO asset_prices
(asset_id, price, currency_code, date)
SELECT a.id,
       520.80,
       c.code,
       CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c
              ON c.code = 'EUR'
WHERE a.ticker = 'CW8';


INSERT INTO asset_prices
(asset_id, price, currency_code, date)
SELECT a.id,
       650.40,
       c.code,
       CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c
              ON c.code = 'USD'
WHERE a.ticker = 'CSPX';


INSERT INTO asset_prices
(asset_id, price, currency_code, date)
SELECT a.id,
       350.00,
       c.code,
       CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c
              ON c.code = 'EUR'
WHERE a.ticker = 'IMMOR';


-- ============================================================
-- POSITIONS - PEA ROMAIN
-- ============================================================

INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_code)
SELECT a.id,
       asset.id,
       20,
       180.00,
       c.code
FROM accounts a
         CROSS JOIN assets asset
         JOIN currencies c
              ON c.code = 'USD'
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'AAPL';


INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_code)
SELECT a.id,
       asset.id,
       10,
       390.00,
       c.code
FROM accounts a
         CROSS JOIN assets asset
         JOIN currencies c
              ON c.code = 'USD'
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'MSFT';


INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_code)
SELECT a.id,
       asset.id,
       15,
       450.00,
       c.code
FROM accounts a
         CROSS JOIN assets asset
         JOIN currencies c
              ON c.code = 'EUR'
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'CW8';


-- ============================================================
-- TRANSACTIONS
-- ============================================================

INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       2500.00,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 30 DAY,
       'Versement initial'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Compte courant Romain'
  AND tt.code = 'DEPOSIT'
  AND c.code = 'EUR';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       1800.00,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 25 DAY,
       'Versement initial'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Compte courant Test'
  AND tt.code = 'DEPOSIT'
  AND c.code = 'EUR';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       125.50,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 10 DAY,
       'Intérêts annuels'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Livret A Romain'
  AND tt.code = 'INTEREST'
  AND c.code = 'EUR';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       85.25,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 12 DAY,
       'Intérêts annuels'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Livret A Test'
  AND tt.code = 'INTEREST'
  AND c.code = 'EUR';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       24.50,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 5 DAY,
       'Dividende Apple'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND tt.code = 'DIVIDEND'
  AND c.code = 'USD';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       1.99,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 3 DAY,
       'Frais de courtage'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND tt.code = 'FEE'
  AND c.code = 'EUR';


INSERT INTO transactions
(account_id, transaction_type_code, amount, currency_code, date, description)
SELECT a.id,
       tt.code,
       18.75,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 8 DAY,
       'Dividende'
FROM accounts a
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Test'
  AND pi.email = 'test@example.com'
  AND tt.code = 'DIVIDEND'
  AND c.code = 'EUR';


-- ============================================================
-- TRADES - PEA ROMAIN
-- ============================================================

INSERT INTO trades
(account_id, asset_id, trade_type_code, quantity, unit_price, fees, currency_code, date)
SELECT a.id,
       asset.id,
       tt.code,
       20,
       180.00,
       2.00,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 90 DAY
FROM accounts a
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'AAPL'
  AND tt.code = 'BUY'
  AND c.code = 'USD';


INSERT INTO trades
(account_id, asset_id, trade_type_code, quantity, unit_price, fees, currency_code, date)
SELECT a.id,
       asset.id,
       tt.code,
       10,
       390.00,
       2.00,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 60 DAY
FROM accounts a
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'MSFT'
  AND tt.code = 'BUY'
  AND c.code = 'USD';


INSERT INTO trades
(account_id, asset_id, trade_type_code, quantity, unit_price, fees, currency_code, date)
SELECT a.id,
       asset.id,
       tt.code,
       15,
       450.00,
       1.50,
       c.code,
       CURRENT_TIMESTAMP - INTERVAL 45 DAY
FROM accounts a
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'CW8'
  AND tt.code = 'BUY'
  AND c.code = 'EUR';


-- ============================================================
-- EXCHANGE RATES
-- ============================================================
-- Taux vers EUR (devise de référence par défaut), sur 4 dates
-- (J-90, J-60, J-30, aujourd'hui) pour permettre de tester
-- des conversions historiques.

-- ------------------------------------------------------------
-- USD -> EUR
-- ------------------------------------------------------------

INSERT INTO exchange_rates
(from_currency_code, to_currency_code, rate, rate_date)
SELECT cf.code,
       ct.code,
       v.rate,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM currencies cf
         JOIN currencies ct
              ON ct.code = 'EUR'
         CROSS JOIN (VALUES (90, 0.9280),
                            (60, 0.9195),
                            (30, 0.9240),
                            (0, 0.9265)) AS v (days_ago, rate)
WHERE cf.code = 'USD';


-- ------------------------------------------------------------
-- GBP -> EUR
-- ------------------------------------------------------------

INSERT INTO exchange_rates
(from_currency_code, to_currency_code, rate, rate_date)
SELECT cf.code,
       ct.code,
       v.rate,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM currencies cf
         JOIN currencies ct
              ON ct.code = 'EUR'
         CROSS JOIN (VALUES (90, 1.1650),
                            (60, 1.1720),
                            (30, 1.1695),
                            (0, 1.1710)) AS v (days_ago, rate)
WHERE cf.code = 'GBP';


-- ------------------------------------------------------------
-- JPY -> EUR
-- ------------------------------------------------------------

INSERT INTO exchange_rates
(from_currency_code, to_currency_code, rate, rate_date)
SELECT cf.code,
       ct.code,
       v.rate,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM currencies cf
         JOIN currencies ct
              ON ct.code = 'EUR'
         CROSS JOIN (VALUES (90, 0.00615),
                            (60, 0.00608),
                            (30, 0.00612),
                            (0, 0.00619)) AS v (days_ago, rate)
WHERE cf.code = 'JPY';


-- ------------------------------------------------------------
-- CHF -> EUR
-- ------------------------------------------------------------

INSERT INTO exchange_rates
(from_currency_code, to_currency_code, rate, rate_date)
SELECT cf.code,
       ct.code,
       v.rate,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM currencies cf
         JOIN currencies ct
              ON ct.code = 'EUR'
         CROSS JOIN (VALUES (90, 1.0420),
                            (60, 1.0455),
                            (30, 1.0480),
                            (0, 1.0460)) AS v (days_ago, rate)
WHERE cf.code = 'CHF';


-- ============================================================
-- ACCOUNT BALANCE SNAPSHOTS
-- ============================================================
-- 4 dates (J-90, J-60, J-30, aujourd'hui) par compte, avec une
-- progression réaliste vers le solde actuel.

INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 2200.00),
                            (60, 2350.00),
                            (30, 2420.00),
                            (0, 2500.00)) AS v (days_ago, balance)
WHERE a.name = 'Compte courant Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 9700.00),
                            (60, 9820.00),
                            (30, 9910.00),
                            (0, 10000.00)) AS v (days_ago, balance)
WHERE a.name = 'Livret A Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 23100.00),
                            (60, 23800.00),
                            (30, 24350.00),
                            (0, 25000.00)) AS v (days_ago, balance)
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 7600.00),
                            (60, 7750.00),
                            (30, 7880.00),
                            (0, 8000.00)) AS v (days_ago, balance)
WHERE a.name = 'PER Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 14200.00),
                            (60, 14550.00),
                            (30, 14800.00),
                            (0, 15000.00)) AS v (days_ago, balance)
WHERE a.name = 'PEE Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 11400.00),
                            (60, 11650.00),
                            (30, 11820.00),
                            (0, 12000.00)) AS v (days_ago, balance)
WHERE a.name = 'Assurance vie Romain'
  AND pi.email = 'romain@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 1600.00),
                            (60, 1680.00),
                            (30, 1740.00),
                            (0, 1800.00)) AS v (days_ago, balance)
WHERE a.name = 'Compte courant Test'
  AND pi.email = 'test@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 7250.00),
                            (60, 7350.00),
                            (30, 7420.00),
                            (0, 7500.00)) AS v (days_ago, balance)
WHERE a.name = 'Livret A Test'
  AND pi.email = 'test@example.com';


INSERT INTO account_balance_snapshots
(account_id, balance, currency_code, snapshot_date)
SELECT a.id,
       v.balance,
       a.currency_code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM accounts a
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         CROSS JOIN (VALUES (90, 17100.00),
                            (60, 17650.00),
                            (30, 18100.00),
                            (0, 18500.00)) AS v (days_ago, balance)
WHERE a.name = 'PEA Test'
  AND pi.email = 'test@example.com';


-- ============================================================
-- POSITION SNAPSHOTS
-- ============================================================
-- Quantité et prix de revient figés à la date du snapshot,
-- valorisation de marché progressant vers le prix actuel de
-- l'actif (asset_prices).

-- ------------------------------------------------------------
-- AAPL (PEA Romain) - 20 titres, PRU 180.00 USD
-- ------------------------------------------------------------

INSERT INTO position_snapshots
(position_id, quantity, market_value, average_price, currency_code, snapshot_date)
SELECT p.id,
       20,
       v.market_value,
       180.00,
       c.code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM positions p
         JOIN accounts a
              ON a.id = p.account_id
         JOIN assets asset
              ON asset.id = p.asset_id
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         JOIN currencies c
              ON c.code = 'USD'
         CROSS JOIN (VALUES (90, 3980.00),
                            (60, 4260.00),
                            (30, 4450.00),
                            (0, 4610.00)) AS v (days_ago, market_value)
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'AAPL';


-- ------------------------------------------------------------
-- MSFT (PEA Romain) - 10 titres, PRU 390.00 USD
-- ------------------------------------------------------------

INSERT INTO position_snapshots
(position_id, quantity, market_value, average_price, currency_code, snapshot_date)
SELECT p.id,
       10,
       v.market_value,
       390.00,
       c.code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM positions p
         JOIN accounts a
              ON a.id = p.account_id
         JOIN assets asset
              ON asset.id = p.asset_id
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         JOIN currencies c
              ON c.code = 'USD'
         CROSS JOIN (VALUES (90, 4650.00),
                            (60, 4850.00),
                            (30, 4980.00),
                            (0, 5102.50)) AS v (days_ago, market_value)
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'MSFT';


-- ------------------------------------------------------------
-- CW8 (PEA Romain) - 15 titres, PRU 450.00 EUR
-- ------------------------------------------------------------

INSERT INTO position_snapshots
(position_id, quantity, market_value, average_price, currency_code, snapshot_date)
SELECT p.id,
       15,
       v.market_value,
       450.00,
       c.code,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM positions p
         JOIN accounts a
              ON a.id = p.account_id
         JOIN assets asset
              ON asset.id = p.asset_id
         JOIN account_owners ao
              ON ao.account_id = a.id
         JOIN users u
              ON u.id = ao.user_id
         JOIN personal_informations pi
              ON pi.id = u.personal_information_id
         JOIN currencies c
              ON c.code = 'EUR'
         CROSS JOIN (VALUES (90, 7100.00),
                            (60, 7420.00),
                            (30, 7650.00),
                            (0, 7812.00)) AS v (days_ago, market_value)
WHERE a.name = 'PEA Romain'
  AND pi.email = 'romain@example.com'
  AND asset.ticker = 'CW8';


-- ============================================================
-- ACCOUNT OWNER SNAPSHOTS
-- ============================================================
-- Répartition de propriété inchangée dans ce jeu de test
-- (100% sur les 4 mêmes dates) - la table est prévue pour le
-- jour où la répartition d'un compte évoluerait.

INSERT INTO account_owner_snapshots
(account_owner_id, ownership_percentage, snapshot_date)
SELECT ao.id,
       100.00,
       CURRENT_DATE - INTERVAL v.days_ago DAY
FROM account_owners ao
         CROSS JOIN (VALUES (90), (60), (30), (0)) AS v (days_ago);


-- ============================================================
-- FIN DES DONNÉES DE TEST
-- ============================================================