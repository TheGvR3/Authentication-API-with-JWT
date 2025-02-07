# Auth Demo API

API di autenticazione sviluppata con Spring Boot e JWT per la gestione degli utenti.

## Tecnologie Utilizzate

- Java 17
- Spring Boot 3.4.2
- MySQL (XAMPP)
- JWT per l'autenticazione
- Spring Security

## Configurazione Database

1. Avvia XAMPP
2. Apri phpMyAdmin (http://localhost/phpmyadmin)
3. Crea un nuovo database:

```sql
CREATE DATABASE auth_db;

USE auth_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    CONSTRAINT uk_email UNIQUE (email)
);
```

## Endpoints API

### Autenticazione

#### Registrazione
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "email": "utente@esempio.com",
    "password": "password123",
    "nome": "Mario",
    "cognome": "Rossi"
}
```

#### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "utente@esempio.com",
    "password": "password123"
}
```

### Gestione Profilo (Richiede JWT Token)

Per tutte le seguenti richieste, è necessario includere il token JWT nell'header:
```http
Authorization: Bearer {il-tuo-token-jwt}
```

#### Visualizza Profilo
```http
GET http://localhost:8080/api/auth/profile
```

#### Aggiorna Profilo
```http
PUT http://localhost:8080/api/auth/update
Content-Type: application/json

{
    "nome": "Nuovo Nome",     // opzionale
    "cognome": "Nuovo Cognome", // opzionale
    "password": "nuovaPassword"  // opzionale
}
```

#### Elimina Account
```http
DELETE http://localhost:8080/api/auth/delete
```

## Risposte API

### Successo
- Registrazione/Login: restituisce token JWT e email
```json
{
    "token": "eyJhbGciOiJIUzI1...",
    "email": "utente@esempio.com"
}
```

- Profilo: restituisce i dati dell'utente
```json
{
    "id": 1,
    "email": "utente@esempio.com",
    "nome": "Mario",
    "cognome": "Rossi"
}
```

### Errori
- 400 Bad Request: quando la richiesta non è valida
```json
{
    "message": "Email già registrata"
}
```
- 401 Unauthorized: quando il token JWT non è valido o è scaduto
```json
{
    "error": "Unauthorized",
    "message": "Token non valido o scaduto"
}
```

## Note di Sicurezza
- Le password vengono crittografate con BCrypt prima di essere salvate nel database
- Il token JWT ha una validità di 24 ore
- Tutte le richieste (tranne login e registrazione) richiedono autenticazione
- Il token deve essere incluso nell'header Authorization come "Bearer {token}"

## Test con Postman

1. **Registrazione**
   - Metodo: POST
   - URL: http://localhost:8080/api/auth/register
   - Body (raw JSON): 
   ```json
   {
       "email": "test@example.com",
       "password": "password123",
       "nome": "Mario",
       "cognome": "Rossi"
   }
   ```

2. **Login**
   - Metodo: POST
   - URL: http://localhost:8080/api/auth/login
   - Body (raw JSON):
   ```json
   {
       "email": "test@example.com",
       "password": "password123"
   }
   ```

3. **Visualizza Profilo**
   - Metodo: GET
   - URL: http://localhost:8080/api/auth/profile
   - Headers:
     - Authorization: Bearer {token-ricevuto-dal-login}

4. **Aggiorna Profilo**
   - Metodo: PUT
   - URL: http://localhost:8080/api/auth/update
   - Headers: 
     - Authorization: Bearer {token-ricevuto-dal-login}
   - Body (raw JSON):
   ```json
   {
       "nome": "Nuovo Nome",
       "cognome": "Nuovo Cognome",
       "password": "nuova-password"
   }
   ```

5. **Elimina Account**
   - Metodo: DELETE
   - URL: http://localhost:8080/api/auth/delete
   - Headers:
     - Authorization: Bearer {token-ricevuto-dal-login}