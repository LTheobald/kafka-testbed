CREATE TABLE transactions (
  timestamp TIMESTAMP NOT NULL,
  transaction_id UUID NOT NULL,
  merchant_id VARCHAR(255) NOT NULL,
  amount DOUBLE PRECISION NOT NULL,
  currency VARCHAR(3) NOT NULL,
  statement_narrative TEXT NOT NULL
);