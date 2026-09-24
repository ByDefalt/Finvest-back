SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE trades;
TRUNCATE TABLE transactions;
TRUNCATE TABLE positions;
TRUNCATE TABLE asset_countries;
TRUNCATE TABLE asset_prices;
TRUNCATE TABLE assets;

TRUNCATE TABLE compte_courants;
TRUNCATE TABLE livrets;
TRUNCATE TABLE peas;
TRUNCATE TABLE comptes_titres;
TRUNCATE TABLE assurances_vie;
TRUNCATE TABLE pers;
TRUNCATE TABLE pees;

TRUNCATE TABLE account_owners;
TRUNCATE TABLE accounts;
TRUNCATE TABLE account_types;

TRUNCATE TABLE trade_types;
TRUNCATE TABLE transaction_types;
TRUNCATE TABLE asset_types;
TRUNCATE TABLE management_types;
TRUNCATE TABLE account_statuses;

TRUNCATE TABLE banks;
TRUNCATE TABLE countries;
TRUNCATE TABLE currencies;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;