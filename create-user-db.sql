-- Script para crear la base de datos user_ms_db manualmente
-- Ejecutar como: psql -h localhost -U postgres -d postgres -f create-user-db.sql

-- Crear la base de datos user_ms_db si no existe
CREATE DATABASE user_ms_db;

-- Conectarse a la nueva base de datos y crear las tablas necesarias
\c user_ms_db

-- Aquí puedes agregar la creación de tablas específicas para user-ms si es necesario
-- Por ejemplo:
-- CREATE TABLE usuarios (
--     id SERIAL PRIMARY KEY,
--     nombre VARCHAR(255) NOT NULL,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Otorgar permisos al usuario postgres
GRANT ALL PRIVILEGES ON DATABASE user_ms_db TO postgres;