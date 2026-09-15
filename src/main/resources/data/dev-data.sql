-- ============================================================
-- FINVEST - TEST DATA
-- ============================================================


-- ============================================================
-- USERS
-- ============================================================

INSERT INTO users (email, password)
VALUES
    ('romain@example.com',
     '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.'),

    ('test@example.com',
     '$2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.');

-- test-password-hash:
-- $2a$10$EO3AlQsEUvetnxuLcnAp6OKnd2TVkcT992RAxRMiGzD02y93o26W.


-- ============================================================
-- CURRENCIES
-- ============================================================

INSERT INTO currencies (code, name, symbol)
VALUES
    ('EUR', 'Euro', '€'),
    ('USD', 'Dollar américain', '$'),
    ('GBP', 'Livre sterling', '£'),
    ('JPY', 'Yen japonais', '¥'),
    ('CHF', 'Franc suisse', 'CHF');


-- ============================================================
-- COUNTRIES
-- ============================================================

INSERT INTO countries (code, name)
VALUES
    ('FR', 'France'),
    ('US', 'États-Unis'),
    ('GB', 'Royaume-Uni'),
    ('DE', 'Allemagne'),
    ('JP', 'Japon'),
    ('CH', 'Suisse'),
    ('NL', 'Pays-Bas'),
    ('LU', 'Luxembourg'),
    ('IE', 'Irlande'),
    ('CA', 'Canada');


-- ============================================================
-- BANKS
-- ============================================================

INSERT INTO banks (name, bic, logo)
VALUES
    ('Crédit Mutuel Arkéa', 'CMBRFR2B', NULL),
    ('Fortuneo', 'FTNOFRP1', NULL),
    ('Boursobank', 'BOUSFRPP', NULL),
    ('AXA', 'AXABFRPP', NULL);


-- ============================================================
-- ACCOUNT STATUSES
-- ============================================================

INSERT INTO account_statuses (code, name)
VALUES
    ('ACTIVE', 'Actif'),
    ('CLOSED', 'Clôturé'),
    ('BLOCKED', 'Bloqué');


-- ============================================================
-- MANAGEMENT TYPES
-- ============================================================

INSERT INTO management_types (code, name)
VALUES
    ('FREE_MANAGEMENT', 'Gestion libre'),
    ('MANAGED', 'Gestion pilotée'),
    ('ADVISED', 'Gestion conseillée');


-- ============================================================
-- ASSET TYPES
-- ============================================================

INSERT INTO asset_types (code, name)
VALUES
    ('STOCK', 'Action'),
    ('ETF', 'ETF'),
    ('BOND', 'Obligation'),
    ('FUND', 'Fonds'),
    ('SCPI', 'SCPI'),
    ('OPCI', 'OPCI'),
    ('CRYPTO', 'Cryptomonnaie'),
    ('CASH', 'Liquidités');


-- ============================================================
-- TRANSACTION TYPES
-- ============================================================

INSERT INTO transaction_types (code, name)
VALUES
    ('DEPOSIT', 'Dépôt'),
    ('WITHDRAWAL', 'Retrait'),
    ('TRANSFER', 'Virement'),
    ('DIVIDEND', 'Dividende'),
    ('INTEREST', 'Intérêts'),
    ('FEE', 'Frais'),
    ('TAX', 'Impôt');


-- ============================================================
-- TRADE TYPES
-- ============================================================

INSERT INTO trade_types (code, name)
VALUES
    ('BUY', 'Achat'),
    ('SELL', 'Vente');


-- ============================================================
-- ACCOUNTS - ROMAIN
-- ============================================================

-- Compte courant
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'Compte courant',
    2500.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Compte courant principal'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

-- Livret A
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'Livret A',
    10000.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Épargne de précaution'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

-- PEA
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'PEA',
    25000.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Plan d''épargne en actions'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Fortuneo'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

-- PER
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'PER',
    8000.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Plan épargne retraite'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'AXA'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

-- PEE
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'PEE',
    15000.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Plan épargne entreprise'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Crédit Mutuel Arkéa'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

-- Assurance vie
INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'Assurance vie',
    12000.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Contrat d''assurance vie'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'AXA'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';


-- ============================================================
-- ACCOUNTS - TEST
-- ============================================================

INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'Compte courant',
    1800.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Compte courant principal'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Boursobank'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'Livret A',
    7500.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Épargne'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Boursobank'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';

INSERT INTO accounts
(bank_id, name, balance, currency_id, created_at, account_status_id, description)
SELECT
    b.id,
    'PEA',
    18500.00,
    c.id,
    CURRENT_TIMESTAMP,
    s.id,
    'Plan d''épargne en actions'
FROM banks b
         CROSS JOIN currencies c
         CROSS JOIN account_statuses s
WHERE b.name = 'Fortuneo'
  AND c.code = 'EUR'
  AND s.code = 'ACTIVE';


-- ============================================================
-- ACCOUNT OWNERS
-- ============================================================

-- Comptes de Romain
INSERT INTO account_owners
(account_id, user_id, name, ownership_percentage)
SELECT
    a.id,
    u.id,
    'Romain Rousval',
    100.00
FROM accounts a
         CROSS JOIN users u
WHERE u.email = 'romain@example.com'
  AND a.name IN (
                 'Compte courant',
                 'Livret A',
                 'PEA',
                 'PER',
                 'PEE',
                 'Assurance vie'
    );

-- Comptes du deuxième utilisateur
INSERT INTO account_owners
(account_id, user_id, name, ownership_percentage)
SELECT
    a.id,
    u.id,
    'Utilisateur test',
    100.00
FROM accounts a
         CROSS JOIN users u
WHERE u.email = 'test@example.com'
  AND a.name IN (
                 'Compte courant',
                 'Livret A',
                 'PEA'
    );


-- ============================================================
-- COMPTE COURANT
-- ============================================================

INSERT INTO compte_courants
(account_id, iban, bic, account_number, overdraft_limit, holder_name)
SELECT
    a.id,
    'FR7630006000011234567890189',
    b.bic,
    '12345678901',
    500.00,
    'Romain Rousval'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
WHERE a.name = 'Compte courant'
  AND b.name = 'Crédit Mutuel Arkéa'
  AND EXISTS (
    SELECT 1
    FROM account_owners ao
             JOIN users u ON u.id = ao.user_id
    WHERE ao.account_id = a.id
      AND u.email = 'romain@example.com'
);


-- ============================================================
-- LIVRET
-- ============================================================

INSERT INTO livrets
(account_id, interest_rate, ceiling)
SELECT
    a.id,
    1.70,
    22950.00
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
WHERE a.name = 'Livret A'
  AND b.name = 'Crédit Mutuel Arkéa';


INSERT INTO livrets
(account_id, interest_rate, ceiling)
SELECT
    a.id,
    1.70,
    22950.00
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
WHERE a.name = 'Livret A'
  AND b.name = 'Boursobank';


-- ============================================================
-- PEA
-- ============================================================

INSERT INTO peas
(account_id, opening_date, deposit_limit)
SELECT
    a.id,
    DATE '2022-03-15',
    150000.00
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo';


-- ============================================================
-- PER
-- ============================================================

INSERT INTO pers
(account_id, contract_number, opening_date, management_type_id)
SELECT
    a.id,
    'PER-2025-000001',
    DATE '2025-09-01',
    m.id
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN management_types m
WHERE a.name = 'PER'
  AND b.name = 'AXA'
  AND m.code = 'MANAGED';


-- ============================================================
-- PEE
-- ============================================================

INSERT INTO pees
(account_id, opening_date, employer)
SELECT
    a.id,
    DATE '2020-06-01',
    'Crédit Mutuel Arkéa'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
WHERE a.name = 'PEE'
  AND b.name = 'Crédit Mutuel Arkéa';


-- ============================================================
-- ASSURANCE VIE
-- ============================================================

INSERT INTO assurances_vie
(account_id, contract_number, opening_date, management_type_id)
SELECT
    a.id,
    'AV-2021-000001',
    DATE '2021-05-10',
    m.id
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN management_types m
WHERE a.name = 'Assurance vie'
  AND b.name = 'AXA'
  AND m.code = 'FREE_MANAGEMENT';


-- ============================================================
-- ASSETS
-- ============================================================

-- Apple
INSERT INTO assets
(name, asset_type_id, isin, ticker, issuer_country_id, currency_id, description)
SELECT
    'Apple Inc.',
    at.id,
    'US0378331005',
    'AAPL',
    co.id,
    cu.id,
    'Action Apple'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'STOCK'
  AND co.code = 'US'
  AND cu.code = 'USD';


-- Microsoft
INSERT INTO assets
(name, asset_type_id, isin, ticker, issuer_country_id, currency_id, description)
SELECT
    'Microsoft Corporation',
    at.id,
    'US5949181045',
    'MSFT',
    co.id,
    cu.id,
    'Action Microsoft'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'STOCK'
  AND co.code = 'US'
  AND cu.code = 'USD';


-- ETF MSCI World
INSERT INTO assets
(name, asset_type_id, isin, ticker, issuer_country_id, currency_id, description)
SELECT
    'Amundi MSCI World',
    at.id,
    'LU1681043599',
    'CW8',
    co.id,
    cu.id,
    'ETF MSCI World'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'ETF'
  AND co.code = 'LU'
  AND cu.code = 'EUR';


-- ETF S&P 500
INSERT INTO assets
(name, asset_type_id, isin, ticker, issuer_country_id, currency_id, description)
SELECT
    'iShares Core S&P 500',
    at.id,
    'IE00B5BMR087',
    'CSPX',
    co.id,
    cu.id,
    'ETF S&P 500'
FROM asset_types at
         CROSS JOIN countries co
         CROSS JOIN currencies cu
WHERE at.code = 'ETF'
  AND co.code = 'IE'
  AND cu.code = 'USD';


-- SCPI
INSERT INTO assets
(name, asset_type_id, isin, ticker, issuer_country_id, currency_id, description)
SELECT
    'SCPI Immorente',
    at.id,
    NULL,
    NULL,
    co.id,
    cu.id,
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

-- MSCI World
INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 70.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'LU1681043599' AND c.code = 'US';

INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 6.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'LU1681043599' AND c.code = 'JP';

INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 4.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'LU1681043599' AND c.code = 'GB';

INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 3.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'LU1681043599' AND c.code = 'FR';

INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 17.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'LU1681043599' AND c.code = 'DE';


-- S&P 500
INSERT INTO asset_countries (asset_id, country_id, weight)
SELECT a.id, c.id, 100.00
FROM assets a CROSS JOIN countries c
WHERE a.isin = 'IE00B5BMR087' AND c.code = 'US';


-- ============================================================
-- ASSET PRICES
-- ============================================================

INSERT INTO asset_prices
(asset_id, price, currency_id, date)
SELECT a.id, 230.50, c.id, CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c ON c.code = 'USD'
WHERE a.ticker = 'AAPL';

INSERT INTO asset_prices
(asset_id, price, currency_id, date)
SELECT a.id, 510.25, c.id, CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c ON c.code = 'USD'
WHERE a.ticker = 'MSFT';

INSERT INTO asset_prices
(asset_id, price, currency_id, date)
SELECT a.id, 520.80, c.id, CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c ON c.code = 'EUR'
WHERE a.ticker = 'CW8';

INSERT INTO asset_prices
(asset_id, price, currency_id, date)
SELECT a.id, 650.40, c.id, CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c ON c.code = 'USD'
WHERE a.ticker = 'CSPX';

INSERT INTO asset_prices
(asset_id, price, currency_id, date)
SELECT a.id, 350.00, c.id, CURRENT_TIMESTAMP
FROM assets a
         JOIN currencies c ON c.code = 'EUR'
WHERE a.name = 'SCPI Immorente';


-- ============================================================
-- POSITIONS - PEA ROMAIN
-- ============================================================

-- 20 Apple
INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_id)
SELECT
    a.id,
    asset.id,
    20,
    180.00,
    c.id
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         JOIN currencies c ON c.code = 'USD'
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'AAPL';


-- 10 Microsoft
INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_id)
SELECT
    a.id,
    asset.id,
    10,
    390.00,
    c.id
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         JOIN currencies c ON c.code = 'USD'
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'MSFT';


-- 15 MSCI World
INSERT INTO positions
(account_id, asset_id, quantity, average_price, currency_id)
SELECT
    a.id,
    asset.id,
    15,
    450.00,
    c.id
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         JOIN currencies c ON c.code = 'EUR'
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'CW8';


-- ============================================================
-- TRANSACTIONS
-- ============================================================

-- Dépôt sur compte courant Romain
INSERT INTO transactions
(account_id, transaction_type_id, amount, currency_id, date, description)
SELECT
    a.id,
    tt.id,
    2500.00,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '30' DAY,
    'Versement initial'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Compte courant'
  AND b.name = 'Crédit Mutuel Arkéa'
  AND tt.code = 'DEPOSIT'
  AND c.code = 'EUR';


-- Intérêts Livret A
INSERT INTO transactions
(account_id, transaction_type_id, amount, currency_id, date, description)
SELECT
    a.id,
    tt.id,
    125.50,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '10' DAY,
    'Intérêts annuels'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'Livret A'
  AND b.name = 'Crédit Mutuel Arkéa'
  AND tt.code = 'INTEREST'
  AND c.code = 'EUR';


-- Dividende Apple
INSERT INTO transactions
(account_id, transaction_type_id, amount, currency_id, date, description)
SELECT
    a.id,
    tt.id,
    24.50,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '5' DAY,
    'Dividende Apple'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND tt.code = 'DIVIDEND'
  AND c.code = 'USD';


-- Frais
INSERT INTO transactions
(account_id, transaction_type_id, amount, currency_id, date, description)
SELECT
    a.id,
    tt.id,
    1.99,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '3' DAY,
    'Frais de courtage'
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN transaction_types tt
         CROSS JOIN currencies c
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND tt.code = 'FEE'
  AND c.code = 'EUR';


-- ============================================================
-- TRADES
-- ============================================================

-- Achat Apple
INSERT INTO trades
(account_id, asset_id, trade_type_id, quantity, unit_price, fees, currency_id, date)
SELECT
    a.id,
    asset.id,
    tt.id,
    20,
    180.00,
    2.00,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '90' DAY
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'AAPL'
  AND tt.code = 'BUY'
  AND c.code = 'USD';


-- Achat Microsoft
INSERT INTO trades
(account_id, asset_id, trade_type_id, quantity, unit_price, fees, currency_id, date)
SELECT
    a.id,
    asset.id,
    tt.id,
    10,
    390.00,
    2.00,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '60' DAY
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'MSFT'
  AND tt.code = 'BUY'
  AND c.code = 'USD';


-- Achat MSCI World
INSERT INTO trades
(account_id, asset_id, trade_type_id, quantity, unit_price, fees, currency_id, date)
SELECT
    a.id,
    asset.id,
    tt.id,
    15,
    450.00,
    1.50,
    c.id,
    CURRENT_TIMESTAMP - INTERVAL '45' DAY
FROM accounts a
         JOIN banks b ON b.id = a.bank_id
         CROSS JOIN assets asset
         CROSS JOIN trade_types tt
         CROSS JOIN currencies c
WHERE a.name = 'PEA'
  AND b.name = 'Fortuneo'
  AND asset.ticker = 'CW8'
  AND tt.code = 'BUY'
  AND c.code = 'EUR';