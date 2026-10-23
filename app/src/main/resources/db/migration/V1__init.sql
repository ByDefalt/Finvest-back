-- Finvest - Database schema
-- MariaDB
--
-- Tables de référence : pas d'id technique, la clé primaire est le code métier
-- (banks -> bic). Les tables qui les référencent utilisent une colonne <x>_code.
--
CREATE TABLE personal_informations
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE users
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    personal_information_id BIGINT       NOT NULL,
    password                VARCHAR(255) NOT NULL,

    CONSTRAINT fk_users_personal_information
        FOREIGN KEY (personal_information_id) REFERENCES personal_informations (id)
);

CREATE TABLE users_external
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    personal_information_id BIGINT NOT NULL,

    CONSTRAINT fk_users_external_personal_information
        FOREIGN KEY (personal_information_id) REFERENCES personal_informations (id)
);

-- ---------------------------------------------------------------------------
-- Tables de référence
-- ---------------------------------------------------------------------------

CREATE TABLE banks
(
    bic  VARCHAR(50)  NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    logo VARCHAR(500)
);

CREATE TABLE currencies
(
    code VARCHAR(10) NOT NULL PRIMARY KEY
);

CREATE TABLE countries
(
    code VARCHAR(10) NOT NULL PRIMARY KEY
);

CREATE TABLE account_statuses
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE management_types
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE asset_types
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE transaction_types
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE trade_types
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE account_types
(
    code VARCHAR(50) NOT NULL PRIMARY KEY
);

-- ---------------------------------------------------------------------------
-- Comptes
-- ---------------------------------------------------------------------------

CREATE TABLE accounts
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bank_bic            VARCHAR(50)    NOT NULL,
    name                VARCHAR(255)   NOT NULL,
    balance             DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_code       VARCHAR(10)    NOT NULL,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at           TIMESTAMP,
    account_status_code VARCHAR(50)    NOT NULL,
    description         VARCHAR(1000),
    account_type_code   VARCHAR(50)    NOT NULL,

    CONSTRAINT fk_accounts_account_type
        FOREIGN KEY (account_type_code) REFERENCES account_types (code),

    CONSTRAINT fk_accounts_bank
        FOREIGN KEY (bank_bic) REFERENCES banks (bic),

    CONSTRAINT fk_accounts_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code),

    CONSTRAINT fk_accounts_status
        FOREIGN KEY (account_status_code) REFERENCES account_statuses (code)
);

CREATE TABLE account_balance_snapshots
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    balance       DECIMAL(19, 4) NOT NULL,
    currency_code VARCHAR(10)    NOT NULL,
    snapshot_date DATE           NOT NULL,

    CONSTRAINT fk_abs_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_abs_currency FOREIGN KEY (currency_code) REFERENCES currencies (code),
    CONSTRAINT uk_abs_account_date UNIQUE (account_id, snapshot_date)
);

CREATE TABLE account_owners
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id           BIGINT        NOT NULL,
    user_id              BIGINT,
    user_external_id     BIGINT,
    ownership_percentage DECIMAL(5, 2) NOT NULL,

    CONSTRAINT fk_account_owners_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_account_owners_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_account_owners_user_external
        FOREIGN KEY (user_external_id) REFERENCES users_external (id),

    CONSTRAINT uk_account_owners_account_user
        UNIQUE (account_id, user_id),

    CONSTRAINT chk_account_owners_percentage
        CHECK (ownership_percentage >= 0 AND ownership_percentage <= 100),

    CONSTRAINT chk_account_owners_user_exclusive
        CHECK (
            (user_id IS NOT NULL AND user_external_id IS NULL) OR
            (user_id IS NULL AND user_external_id IS NOT NULL)
            )
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
    account_id           BIGINT PRIMARY KEY,
    contract_number      VARCHAR(100) NOT NULL UNIQUE,
    opening_date         DATE         NOT NULL,
    management_type_code VARCHAR(50)  NOT NULL,

    CONSTRAINT fk_assurances_vie_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_assurances_vie_management_type
        FOREIGN KEY (management_type_code) REFERENCES management_types (code)
);

CREATE TABLE pers
(
    account_id           BIGINT PRIMARY KEY,
    contract_number      VARCHAR(100) NOT NULL UNIQUE,
    opening_date         DATE         NOT NULL,
    management_type_code VARCHAR(50)  NOT NULL,

    CONSTRAINT fk_pers_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_pers_management_type
        FOREIGN KEY (management_type_code) REFERENCES management_types (code)
);

CREATE TABLE pees
(
    account_id   BIGINT PRIMARY KEY,
    opening_date DATE         NOT NULL,
    employer     VARCHAR(255) NOT NULL,

    CONSTRAINT fk_pees_account
        FOREIGN KEY (account_id) REFERENCES accounts (id)
);

-- ---------------------------------------------------------------------------
-- Actifs
-- ---------------------------------------------------------------------------

CREATE TABLE assets
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    asset_type_code     VARCHAR(50)  NOT NULL,
    isin                VARCHAR(50)  NOT NULL UNIQUE,
    ticker              VARCHAR(50)  NOT NULL,
    issuer_country_code VARCHAR(10)  NOT NULL,
    currency_code       VARCHAR(10)  NOT NULL,
    description         VARCHAR(1000),

    CONSTRAINT fk_assets_type
        FOREIGN KEY (asset_type_code) REFERENCES asset_types (code),

    CONSTRAINT fk_assets_country
        FOREIGN KEY (issuer_country_code) REFERENCES countries (code),

    CONSTRAINT fk_assets_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code)
);

CREATE TABLE asset_prices
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id      BIGINT         NOT NULL,
    price         DECIMAL(19, 8) NOT NULL,
    currency_code VARCHAR(10)    NOT NULL,
    date          TIMESTAMP      NOT NULL,

    CONSTRAINT fk_asset_prices_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_prices_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code),

    CONSTRAINT uk_asset_prices_asset_date
        UNIQUE (asset_id, date)
);

CREATE TABLE asset_countries
(
    asset_id     BIGINT        NOT NULL,
    country_code VARCHAR(10)   NOT NULL,
    weight       DECIMAL(7, 4) NOT NULL,

    PRIMARY KEY (asset_id, country_code),

    CONSTRAINT fk_asset_countries_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_asset_countries_country
        FOREIGN KEY (country_code) REFERENCES countries (code)
);

CREATE TABLE positions
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    asset_id      BIGINT         NOT NULL,
    quantity      DECIMAL(19, 8) NOT NULL,
    average_price DECIMAL(19, 8) NOT NULL,
    currency_code VARCHAR(10)    NOT NULL,

    CONSTRAINT fk_positions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_positions_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_positions_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code),

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
    currency_code VARCHAR(10)    NOT NULL,
    snapshot_date DATE           NOT NULL,

    CONSTRAINT fk_ps_position FOREIGN KEY (position_id) REFERENCES positions (id),
    CONSTRAINT fk_ps_currency FOREIGN KEY (currency_code) REFERENCES currencies (code),
    CONSTRAINT uk_ps_position_date UNIQUE (position_id, snapshot_date)
);

CREATE TABLE exchange_rates
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_currency_code VARCHAR(10)    NOT NULL,
    to_currency_code   VARCHAR(10)    NOT NULL,
    rate               DECIMAL(19, 8) NOT NULL,
    rate_date          DATE           NOT NULL,

    CONSTRAINT fk_er_from FOREIGN KEY (from_currency_code) REFERENCES currencies (code),
    CONSTRAINT fk_er_to FOREIGN KEY (to_currency_code) REFERENCES currencies (code),
    CONSTRAINT uk_er_pair_date UNIQUE (from_currency_code, to_currency_code, rate_date)
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

-- ---------------------------------------------------------------------------
-- Mouvements
-- ---------------------------------------------------------------------------

CREATE TABLE transactions
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id            BIGINT         NOT NULL,
    transaction_type_code VARCHAR(50)    NOT NULL,
    amount                DECIMAL(19, 4) NOT NULL,
    currency_code         VARCHAR(10)    NOT NULL,
    date                  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description           VARCHAR(1000),

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_transactions_type
        FOREIGN KEY (transaction_type_code) REFERENCES transaction_types (code),

    CONSTRAINT fk_transactions_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code)
);

CREATE TABLE trades
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id      BIGINT         NOT NULL,
    asset_id        BIGINT         NOT NULL,
    trade_type_code VARCHAR(50)    NOT NULL,
    quantity        DECIMAL(19, 8) NOT NULL,
    unit_price      DECIMAL(19, 8) NOT NULL,
    fees            DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency_code   VARCHAR(10)    NOT NULL,
    date            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trades_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),

    CONSTRAINT fk_trades_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id),

    CONSTRAINT fk_trades_type
        FOREIGN KEY (trade_type_code) REFERENCES trade_types (code),

    CONSTRAINT fk_trades_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code)
);