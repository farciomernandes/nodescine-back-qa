-- ============================================================
-- NORDESCINE — V1: Full Schema Creation
-- All tables must exist before any seed/data migration runs.
-- ============================================================

-- Users
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    avatar          VARCHAR(255),
    cpf             VARCHAR(255),
    phone           VARCHAR(255),
    postal_code     VARCHAR(255),
    address         VARCHAR(255),
    address_number  VARCHAR(255),
    complement      VARCHAR(255),
    province        VARCHAR(255),
    status          VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    wallet_id       VARCHAR(255),
    birth_date      VARCHAR(255),
    income_value    INTEGER,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    deleted_at      TIMESTAMP
);

-- User roles (ElementCollection)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role    VARCHAR(255) NOT NULL
);

-- User tokens
CREATE TABLE IF NOT EXISTS user_tokens (
    id          BIGSERIAL PRIMARY KEY,
    user_email  VARCHAR(255) NOT NULL,
    token       VARCHAR(500) NOT NULL,
    issued_at   TIMESTAMP NOT NULL,
    expires_at  TIMESTAMP NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT true
);

-- Categories
CREATE TABLE IF NOT EXISTS categories (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) UNIQUE,
    slug       VARCHAR(255),
    image_url  VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Genres
CREATE TABLE IF NOT EXISTS genre (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Movies
CREATE TABLE IF NOT EXISTS movie (
    id                  BIGSERIAL PRIMARY KEY,
    title               VARCHAR(255),
    slug                VARCHAR(255),
    director            VARCHAR(255),
    year                INTEGER,
    price               NUMERIC(19,2),
    duration_in_minutes INTEGER,
    description         TEXT,
    poster              TEXT,
    banner              TEXT,
    trailer             VARCHAR(255),
    movie_url           VARCHAR(255),
    movie_type          VARCHAR(255) NOT NULL,
    format              VARCHAR(50) DEFAULT 'UNKNOWN',
    moderation_status   VARCHAR(50) DEFAULT 'PUBLISHED',
    category_id         BIGINT REFERENCES categories(id),
    created_by          VARCHAR(255),
    active              BOOLEAN,
    producer_deadline   VARCHAR(255),
    is_adult_confirmed  BOOLEAN,
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    deleted_at          TIMESTAMP
);

-- Movie cast (ElementCollection)
CREATE TABLE IF NOT EXISTS movie_cast (
    movie_id   BIGINT NOT NULL REFERENCES movie(id),
    actor_name VARCHAR(255)
);

-- Movie genres (ManyToMany join table)
CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id BIGINT NOT NULL REFERENCES movie(id),
    genre_id BIGINT NOT NULL REFERENCES genre(id),
    PRIMARY KEY (movie_id, genre_id)
);

-- Movie reports
CREATE TABLE IF NOT EXISTS movie_report (
    id             BIGSERIAL PRIMARY KEY,
    movie_id       BIGINT REFERENCES movie(id),
    reason         TEXT,
    reporter_email VARCHAR(255),
    created_at     TIMESTAMP
);

-- Transactions
CREATE TABLE IF NOT EXISTS transactions (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL REFERENCES users(id),
    transaction_id   VARCHAR(255),
    movie_id         BIGINT NOT NULL REFERENCES movie(id),
    amount           NUMERIC(19,2) NOT NULL,
    date             VARCHAR(255) NOT NULL,
    status           VARCHAR(255) NOT NULL,
    type             VARCHAR(255),
    encoded_image_pix TEXT,
    payload_pix      VARCHAR(255),
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    deleted_at       TIMESTAMP
);
