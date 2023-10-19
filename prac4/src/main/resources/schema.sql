CREATE TABLE stock
(
    uuid   UUID NOT NULL,
    name   VARCHAR(255),
    ticker VARCHAR(255),
    price  DOUBLE PRECISION,
    amount BIGINT,
    CONSTRAINT pk_stock PRIMARY KEY (uuid)
);

CREATE TABLE account
(
    uuid  UUID         NOT NULL,
    name  VARCHAR(255) NOT NULL UNIQUE,
    funds DOUBLE PRECISION,
    CONSTRAINT pk_account PRIMARY KEY (uuid)
);

CREATE TABLE market_request
(
    uuid         UUID NOT NULL,
    account_uuid UUID NOT NULL,
    stock_uuid   UUID NOT NULL,
    amount       INTEGER,
    date_time    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_market_request PRIMARY KEY (uuid),
    CONSTRAINT fk_market_request_on_account_uuid FOREIGN KEY (account_uuid) REFERENCES account (uuid),
    CONSTRAINT fk_market_request_on_stock_uuid FOREIGN KEY (stock_uuid) REFERENCES stock (uuid)
);

CREATE TABLE account_stock
(
    uuid         UUID NOT NULL,
    account_uuid UUID NOT NULL,
    stock_uuid   UUID NOT NULL,
    amount       INTEGER,
    CONSTRAINT pk_account_stock PRIMARY KEY (uuid),
    CONSTRAINT unique_account_stock UNIQUE (account_uuid, stock_uuid),
    CONSTRAINT fk_account_stocks_on_account FOREIGN KEY (account_uuid) REFERENCES account (uuid),
    CONSTRAINT fk_account_stocks_on_stock FOREIGN KEY (stock_uuid) REFERENCES stock (uuid)
);