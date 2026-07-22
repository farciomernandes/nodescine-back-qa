-- Criação das tabelas (banco zerado — Flyway roda antes do Hibernate DDL)
CREATE TABLE IF NOT EXISTS categories (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) UNIQUE,
    slug       VARCHAR(255),
    image_url  VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genre (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Seed de categorias regionais obrigatórias para NORDESCINE
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Ficção', 'ficcao', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Animação', 'animacao', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Documentário', 'documentario', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Doc-Fic', 'doc-fic', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Nacional', 'nacional', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Nordestino', 'nordestino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, slug, created_at, updated_at) VALUES ('Cearense', 'cearense', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;

-- Seed dos 50 gêneros padronizados para NORDESCINE
INSERT INTO genre (name, created_at, updated_at) VALUES ('Ação', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Afro-Cinema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Afro-Latino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Ambiental', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Ativismo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Aventura', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Biografia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Blaxploitation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Ciência', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Cinema Negro', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Comédia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Crime', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Cult', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Dança', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Direitos Humanos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Drama', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Educacional', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Esporte', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Experimental', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Fantasia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Faroeste', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Feminismo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Guerra', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Histórico', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Horror', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Indígena', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Infantil', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Investigativo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Jurídico', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('LGBTQIA+', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Medicina', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Migração', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Militar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Mistério', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Musical', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Natureza', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Noir', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Nordestern', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Periferia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Policial', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Político', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Queer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Religioso', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Road Movie', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Romance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Sci-Fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Social', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Suspense', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Terror', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;
INSERT INTO genre (name, created_at, updated_at) VALUES ('Urbano', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (name) DO NOTHING;