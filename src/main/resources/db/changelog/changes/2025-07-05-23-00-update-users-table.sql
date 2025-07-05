ALTER TABLE users 
CHANGE COLUMN is_enabled enabled BOOLEAN NOT NULL DEFAULT TRUE,
CHANGE COLUMN is_account_non_expired account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
CHANGE COLUMN is_account_non_locked account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
CHANGE COLUMN is_credentials_non_expired credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE;