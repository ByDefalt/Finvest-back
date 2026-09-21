-- Finvest - Database schema
-- PostgreSQL / H2-compatible style

DROP TABLE IF EXISTS trades CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS positions CASCADE;
DROP TABLE IF EXISTS asset_countries CASCADE;
DROP TABLE IF EXISTS asset_prices CASCADE;
DROP TABLE IF EXISTS assets CASCADE;

DROP TABLE IF EXISTS compte_courants CASCADE;
DROP TABLE IF EXISTS livrets CASCADE;
DROP TABLE IF EXISTS peas CASCADE;
DROP TABLE IF EXISTS comptes_titres CASCADE;
DROP TABLE IF EXISTS assurances_vie CASCADE;
DROP TABLE IF EXISTS pers CASCADE;
DROP TABLE IF EXISTS pees CASCADE;

DROP TABLE IF EXISTS account_owners CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS account_types CASCADE;

DROP TABLE IF EXISTS trade_types CASCADE;
DROP TABLE IF EXISTS transaction_types CASCADE;
DROP TABLE IF EXISTS asset_types CASCADE;
DROP TABLE IF EXISTS management_types CASCADE;
DROP TABLE IF EXISTS account_statuses CASCADE;

DROP TABLE IF EXISTS banks CASCADE;
DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS currencies CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL
);

CREATE TABLE banks
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bic  VARCHAR(50),
    logo VARCHAR(500)
);

CREATE TABLE currencies
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code   VARCHAR(10)  NOT NULL UNIQUE
);

CREATE TABLE countries
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(10)  NOT NULL UNIQUE
);

CREATE TABLE account_statuses
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE management_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE asset_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE transaction_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE trade_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE account_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE accounts
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bank_id           BIGINT NOT NULL,
    name              VARCHAR(255) NOT NULL,
    balance           DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_id       BIGINT NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at         TIMESTAMP,
    account_status_id BIGINT NOT NULL,
    description       VARCHAR(1000),
    account_type_id BIGINT NOT NULL,

    CONSTRAINT fk_accounts_account_type
        FOREIGN KEY (account_type_id) REFERENCES account_types (id),

    CONSTRAINT fk_accounts_bank
        FOREIGN KEY (bank_id) REFERENCES banks (id),

    CONSTRAINT fk_accounts_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id),

    CONSTRAINT fk_accounts_status
        FOREIGN KEY (account_status_id) REFERENCES account_statuses (id)
);

CREATE TABLE account_owners
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id            BIGINT NOT NULL,
    user_id               BIGINT,
    name                  VARCHAR(255) NOT NULL,
    ownership_percentage  DECIMAL(5, 2) NOT NULL,

    CONSTRAINT fk_account_owners_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_account_owners_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT chk_account_owners_percentage
        CHECK (ownership_percentage >= 0 AND ownership_percentage <= 100)
);

CREATE TABLE compte_courants
(
    account_id      BIGINT PRIMARY KEY,
    iban            VARCHAR(50),
    bic             VARCHAR(50),
    account_number  VARCHAR(100),
    overdraft_limit DECIMAL(19, 4),
    holder_name     VARCHAR(255),

    CONSTRAINT fk_compte_courants_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE livrets
(
    account_id    BIGINT PRIMARY KEY,
    interest_rate DECIMAL(8, 4),
    ceiling       DECIMAL(19, 4),

    CONSTRAINT fk_livrets_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE peas
(
    account_id    BIGINT PRIMARY KEY,
    opening_date  DATE,
    deposit_limit DECIMAL(19, 4),

    CONSTRAINT fk_peas_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE comptes_titres
(
    account_id     BIGINT PRIMARY KEY,
    account_number VARCHAR(100),

    CONSTRAINT fk_comptes_titres_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE assurances_vie
(
    account_id         BIGINT PRIMARY KEY,
    contract_number    VARCHAR(100),
    opening_date       DATE,
    management_type_id BIGINT,

    CONSTRAINT fk_assurances_vie_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_assurances_vie_management_type
        FOREIGN KEY (management_type_id) REFERENCES management_types (id)
);

CREATE TABLE pers
(
    account_id         BIGINT PRIMARY KEY,
    contract_number    VARCHAR(100),
    opening_date       DATE,
    management_type_id BIGINT,

    CONSTRAINT fk_pers_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_pers_management_type
        FOREIGN KEY (management_type_id) REFERENCES management_types (id)
);

CREATE TABLE pees
(
    account_id   BIGINT PRIMARY KEY,
    opening_date DATE,
    employer     VARCHAR(255),

    CONSTRAINT fk_pees_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE assets
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    asset_type_id     BIGINT NOT NULL,
    isin              VARCHAR(50),
    ticker            VARCHAR(50),
    issuer_country_id BIGINT,
    currency_id       BIGINT NOT NULL,
    description       VARCHAR(1000),

    CONSTRAINT fk_assets_type
        FOREIGN KEY (asset_type_id) REFERENCES asset_types (id),

    CONSTRAINT fk_assets_country
        FOREIGN KEY (issuer_country_id) REFERENCES countries (id),

    CONSTRAINT fk_assets_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id)
);

CREATE TABLE asset_prices
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    asset_id    BIGINT NOT NULL,
    price       DECIMAL(19, 8) NOT NULL,
    currency_id BIGINT NOT NULL,
    date        TIMESTAMP NOT NULL,

    CONSTRAINT fk_asset_prices_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_prices_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id)
);

CREATE TABLE asset_countries
(
    asset_id   BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    weight     DECIMAL(7, 4) NOT NULL,

    PRIMARY KEY (asset_id, country_id),

    CONSTRAINT fk_asset_countries_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_countries_country
        FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE TABLE positions
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id    BIGINT NOT NULL,
    asset_id      BIGINT NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    average_price DECIMAL(19, 8) NOT NULL,
    currency_id   BIGINT NOT NULL,

    CONSTRAINT fk_positions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_positions_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_positions_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id),

    CONSTRAINT uk_positions_account_asset
        UNIQUE (account_id, asset_id)
);

CREATE TABLE transactions
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id          BIGINT NOT NULL,
    transaction_type_id BIGINT NOT NULL,
    amount              DECIMAL(19, 4) NOT NULL,
    currency_id         BIGINT NOT NULL,
    date                TIMESTAMP NOT NULL,
    description         VARCHAR(1000),

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_transactions_type
        FOREIGN KEY (transaction_type_id) REFERENCES transaction_types (id),

    CONSTRAINT fk_transactions_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id)
);

CREATE TABLE trades
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id    BIGINT NOT NULL,
    asset_id      BIGINT NOT NULL,
    trade_type_id BIGINT NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    unit_price    DECIMAL(19, 8) NOT NULL,
    fees          DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_id   BIGINT NOT NULL,
    date          TIMESTAMP NOT NULL,

    CONSTRAINT fk_trades_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_trades_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_trades_type
        FOREIGN KEY (trade_type_id) REFERENCES trade_types (id),

    CONSTRAINT fk_trades_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id)
);
