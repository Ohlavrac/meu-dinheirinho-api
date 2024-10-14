CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE USERS (
    id UUID DEFAULT gen_random_uuid PRIMARY KEY UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL,
    role VARCHAR(100)
);