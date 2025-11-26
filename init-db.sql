-- Crear bases de datos si no existen
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'marketplace_db') THEN
      CREATE DATABASE marketplace_db;
   END IF;
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_ms_db') THEN
      CREATE DATABASE user_ms_db;
   END IF;
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak_db') THEN
      CREATE DATABASE keycloak_db;
   END IF;
END
$$;

-- Crear usuario para Keycloak si no existe
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'keycloak_user') THEN
      CREATE ROLE keycloak_user LOGIN PASSWORD 'password';
      GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_user;
   END IF;
END
$$;