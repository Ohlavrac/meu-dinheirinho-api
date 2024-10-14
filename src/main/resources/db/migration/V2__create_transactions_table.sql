CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE transaction (
    id UUID DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY UNIQUE,
    title VARCHAR(100) NOT NULL,
    value REAL NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE,
    user_id UUID REFERENCES users(id)
); 