DATABASE:
-- Creazione del database
CREATE DATABASE auth_db;

-- Uso del database
USE auth_db;

-- Creazione della tabella users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    CONSTRAINT uk_email UNIQUE (email)
);

UTILIZZO:
1 Registrazione utente:
POST http://localhost:8080/api/auth/register
Body (JSON):
{
    "email": "test@example.com",
    "password": "password123",
    "nome": "Mario",
    "cognome": "Rossi"
}

Esempio risposta:
{
    "token": "eyJhbGciOiJIUzI1...",
    "email": "test@example.com"
}

2 Login Utente:
POST http://localhost:8080/api/auth/login
Body (JSON):
{
    "email": "test@example.com",
    "password": "password123"
}

Esempio risposta:
{
    "token": "eyJhbGciOiJIUzI1...",
    "email": "test@example.com"
}
