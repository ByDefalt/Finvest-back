-- Finvest - Database schema
-- MariaDB

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE banks
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bic  VARCHAR(50)  NOT NULL UNIQUE,
    logo VARCHAR(500)
);

CREATE TABLE currencies
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE countries
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE account_statuses
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE management_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE asset_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE transaction_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE trade_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE account_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE accounts
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    bank_id           BIGINT         NOT NULL,
    name              VARCHAR(255)   NOT NULL,
    balance           DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_id       BIGINT         NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at         TIMESTAMP,
    account_status_id BIGINT         NOT NULL,
    description       VARCHAR(1000),
    account_type_id   BIGINT         NOT NULL,

    CONSTRAINT fk_accounts_account_type
        FOREIGN KEY (account_type_id) REFERENCES account_types (id),

    CONSTRAINT fk_accounts_bank
        FOREIGN KEY (bank_id) REFERENCES banks (id),

    CONSTRAINT fk_accounts_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id),

    CONSTRAINT fk_accounts_status
        FOREIGN KEY (account_status_id) REFERENCES account_statuses (id)
);

CREATE TABLE account_balance_snapshots
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    balance       DECIMAL(19, 4) NOT NULL,
    currency_id   BIGINT         NOT NULL,
    snapshot_date DATE           NOT NULL,

    CONSTRAINT fk_abs_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_abs_currency FOREIGN KEY (currency_id) REFERENCES currencies (id),
    CONSTRAINT uk_abs_account_date UNIQUE (account_id, snapshot_date)
);

CREATE TABLE account_owners
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id           BIGINT        NOT NULL,
    user_id              BIGINT,
    name                 VARCHAR(255)  NOT NULL,
    ownership_percentage DECIMAL(5, 2) NOT NULL,

    CONSTRAINT fk_account_owners_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_account_owners_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT uk_account_owners_account_user
        UNIQUE (account_id, user_id),

    CONSTRAINT chk_account_owners_percentage
        CHECK (ownership_percentage >= 0 AND ownership_percentage <= 100)
);

CREATE TABLE compte_courants
(
    account_id      BIGINT PRIMARY KEY,
    iban            VARCHAR(50)    NOT NULL UNIQUE,
    bic             VARCHAR(50)    NOT NULL,
    account_number  VARCHAR(100)   NOT NULL,
    overdraft_limit DECIMAL(19, 4) NOT NULL DEFAULT 0,
    holder_name     VARCHAR(255)   NOT NULL,

    CONSTRAINT fk_compte_courants_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE livrets
(
    account_id    BIGINT PRIMARY KEY,
    interest_rate DECIMAL(8, 4)  NOT NULL DEFAULT 0,
    ceiling       DECIMAL(19, 4) NOT NULL,

    CONSTRAINT fk_livrets_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE peas
(
    account_id    BIGINT PRIMARY KEY,
    opening_date  DATE           NOT NULL,
    deposit_limit DECIMAL(19, 4) NOT NULL,

    CONSTRAINT fk_peas_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE comptes_titres
(
    account_id     BIGINT PRIMARY KEY,
    account_number VARCHAR(100) NOT NULL UNIQUE,

    CONSTRAINT fk_comptes_titres_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE assurances_vie
(
    account_id         BIGINT PRIMARY KEY,
    contract_number    VARCHAR(100) NOT NULL UNIQUE,
    opening_date       DATE         NOT NULL,
    management_type_id BIGINT       NOT NULL,

    CONSTRAINT fk_assurances_vie_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_assurances_vie_management_type
        FOREIGN KEY (management_type_id) REFERENCES management_types (id)
);

CREATE TABLE pers
(
    account_id         BIGINT PRIMARY KEY,
    contract_number    VARCHAR(100) NOT NULL UNIQUE,
    opening_date       DATE         NOT NULL,
    management_type_id BIGINT       NOT NULL,

    CONSTRAINT fk_pers_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_pers_management_type
        FOREIGN KEY (management_type_id) REFERENCES management_types (id)
);

CREATE TABLE pees
(
    account_id   BIGINT PRIMARY KEY,
    opening_date DATE         NOT NULL,
    employer     VARCHAR(255) NOT NULL,

    CONSTRAINT fk_pees_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE assets
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    asset_type_id     BIGINT       NOT NULL,
    isin              VARCHAR(50)  NOT NULL UNIQUE,
    ticker            VARCHAR(50)  NOT NULL,
    issuer_country_id BIGINT       NOT NULL,
    currency_id       BIGINT       NOT NULL,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id    BIGINT         NOT NULL,
    price       DECIMAL(19, 8) NOT NULL,
    currency_id BIGINT         NOT NULL,
    date        TIMESTAMP      NOT NULL,

    CONSTRAINT fk_asset_prices_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_prices_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id),

    CONSTRAINT uk_asset_prices_asset_date
        UNIQUE (asset_id, date)
);

CREATE TABLE asset_countries
(
    asset_id   BIGINT        NOT NULL,
    country_id BIGINT        NOT NULL,
    weight     DECIMAL(7, 4) NOT NULL,

    PRIMARY KEY (asset_id, country_id),

    CONSTRAINT fk_asset_countries_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_countries_country
        FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE TABLE positions
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    asset_id      BIGINT         NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    average_price DECIMAL(19, 8) NOT NULL,
    currency_id   BIGINT         NOT NULL,

    CONSTRAINT fk_positions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_positions_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_positions_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id),

    CONSTRAINT uk_positions_account_asset
        UNIQUE (account_id, asset_id)
);

CREATE TABLE position_snapshots
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    position_id   BIGINT         NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    market_value  DECIMAL(19, 4) NOT NULL,
    average_price DECIMAL(19, 8) NOT NULL,
    currency_id   BIGINT         NOT NULL,
    snapshot_date DATE           NOT NULL,

    CONSTRAINT fk_ps_position FOREIGN KEY (position_id) REFERENCES positions (id),
    CONSTRAINT fk_ps_currency FOREIGN KEY (currency_id) REFERENCES currencies (id),
    CONSTRAINT uk_ps_position_date UNIQUE (position_id, snapshot_date)
);

CREATE TABLE exchange_rates
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_currency_id BIGINT         NOT NULL,
    to_currency_id   BIGINT         NOT NULL,
    rate             DECIMAL(19, 8) NOT NULL,
    rate_date        DATE           NOT NULL,

    CONSTRAINT fk_er_from FOREIGN KEY (from_currency_id) REFERENCES currencies (id),
    CONSTRAINT fk_er_to FOREIGN KEY (to_currency_id) REFERENCES currencies (id),
    CONSTRAINT uk_er_pair_date UNIQUE (from_currency_id, to_currency_id, rate_date)
);

CREATE TABLE account_owner_snapshots
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_owner_id     BIGINT        NOT NULL,
    ownership_percentage DECIMAL(5, 2) NOT NULL,
    snapshot_date        DATE          NOT NULL,

    CONSTRAINT fk_aos_owner FOREIGN KEY (account_owner_id) REFERENCES account_owners (id),
    CONSTRAINT uk_aos_owner_date UNIQUE (account_owner_id, snapshot_date)
);

CREATE INDEX idx_aos_owner_date ON account_owner_snapshots (account_owner_id, snapshot_date);
CREATE INDEX idx_abs_account_date ON account_balance_snapshots (account_id, snapshot_date);
CREATE INDEX idx_ps_position_date ON position_snapshots (position_id, snapshot_date);
CREATE INDEX idx_er_date ON exchange_rates (rate_date);

CREATE TABLE transactions
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id          BIGINT         NOT NULL,
    transaction_type_id BIGINT         NOT NULL,
    amount              DECIMAL(19, 4) NOT NULL,
    currency_id         BIGINT         NOT NULL,
    date                TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    asset_id      BIGINT         NOT NULL,
    trade_type_id BIGINT         NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    unit_price    DECIMAL(19, 8) NOT NULL,
    fees          DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_id   BIGINT         NOT NULL,
    date          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trades_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_trades_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_trades_type
        FOREIGN KEY (trade_type_id) REFERENCES trade_types (id),

    CONSTRAINT fk_trades_currency
        FOREIGN KEY (currency_id) REFERENCES currencies (id)
);