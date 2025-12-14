CREATE TABLE IF NOT EXISTS account(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id CHAR(36) NOT NULL UNIQUE,
    balance DECIMAL(19, 8) NOT NULL
);

CREATE TABLE IF NOT EXISTS asset(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    quantity DECIMAL(19, 8) NOT NULL,

    UNIQUE KEY uq_account_symbol(account_id, symbol),
    CONSTRAINT fk_asset_account
                FOREIGN KEY (account_id) REFERENCES account(id)
                ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trade_history(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id CHAR(36) NOT NULL UNIQUE,
    account_id BIGINT NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    executed_at TIMESTAMP NOT NULL,
    action ENUM('BUY', 'SELL') NOT NULL,
    quantity DECIMAL(19, 8) NOT NULL,
    quote_price DECIMAL(19, 8) NOT NULL,
    transaction_delta DECIMAL(19, 8) NOT NULL,

    INDEX idx_account_time (account_id, executed_at DESC),
    CONSTRAINT fk_trade_account
        FOREIGN KEY (account_id) REFERENCES account(id),
    CHECK (quantity > 0)
);