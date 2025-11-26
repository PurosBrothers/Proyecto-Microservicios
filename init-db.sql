-- Crear bases de datos si no existen
-- Primero, nos conectamos a la base de datos postgres para poder crear otras bases de datos

-- Crear marketplace_db si no existe
SELECT 'CREATE DATABASE marketplace_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'marketplace_db')\gexec

-- Crear user_ms_db si no existe  
SELECT 'CREATE DATABASE user_ms_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_ms_db')\gexec

-- Crear keycloak_db si no existe
SELECT 'CREATE DATABASE keycloak_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak_db')\gexec

-- Conectarse a la base de datos postgres para crear usuarios
\c postgres

-- Crear usuario para Keycloak si no existe
DO $
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'keycloak_user') THEN
      CREATE ROLE keycloak_user LOGIN PASSWORD 'password';
      GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_user;
   END IF;
END
$;

-- También crear el usuario postgres con permisos para todas las bases de datos
GRANT ALL PRIVILEGES ON DATABASE marketplace_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE user_ms_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO postgres;