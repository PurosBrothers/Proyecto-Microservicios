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