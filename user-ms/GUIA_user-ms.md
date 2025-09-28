# GUÍA COMPLETA DE PRUEBAS HTTP - USER-MS CON KEYCLOAK

## CONFIGURACIÓN DEL ENTORNO

### Servicios requeridos:
- **Keycloak:** http://localhost:8081
- **Config Server:** http://localhost:8888
- **User-MS:** http://localhost:8083
- **H2 Database Console:** http://localhost:8083/h2-console

### Credenciales Keycloak:
- **Realm:** proyecto-ms-realm
- **Client ID:** user-ms-client
- **Client Secret:** EjjsZvV6byq8zVfDlzScQDVvT3JwHWpi

---

## PRERREQUISITOS PARA EL DBINITIALIZER

### 1. KEYCLOAK DEBE ESTAR EJECUTÁNDOSE

**Comando para ejecutar Keycloak:**
```bash
cd "C:\Proyecto-Microservicios\keycloak-26.3.3\bin"
.\kc.bat start-dev --http-port=8081
```

**Verificar que Keycloak esté funcionando:**
- Acceder a http://localhost:8081
- Login con admin/admin
- Debe existir el realm "proyecto-ms-realm"
- Debe existir el client "user-ms-client"

### 2. CONFIG SERVER DEBE ESTAR EJECUTÁNDOSE

**Comando:**
```bash
cd "C:\Proyecto-Microservicios\config-server"
mvn spring-boot:run
```

**Verificar:**
- Puerto 8888 debe estar escuchando
- Endpoint http://localhost:8888/user-ms/default debe responder

### 3. USER-MS DEBE CONECTARSE EXITOSAMENTE

**Logs a verificar en user-ms:**
```
Conexión con Keycloak establecida correctamente
Usuario creado en Keycloak con ID: [UUID]
Cliente [Nombre] creado con ID de Keycloak: [UUID]
```

### 4. CONFIGURACIÓN EN KEYCLOAK

**El realm "proyecto-ms-realm" debe tener:**
- Direct access grants: ENABLED
- Service accounts roles: ENABLED
- Client authentication: ENABLED

---

## FLUJO DETALLADO DE PRUEBAS

### FASE 1: VERIFICACIÓN INICIAL

**1.1 Verificar que user-ms está funcionando:**
```http
GET http://localhost:8083/actuator/health
```
**Respuesta esperada:** `{"status":"UP"}`

**1.2 Verificar conexión con Keycloak:**
```http
GET http://localhost:8083/users/info
```
**Respuesta esperada:**
```json
{
  "totalUsers": 6,
  "keycloakAvailable": true,
  "message": "Keycloak integrado - UUIDs generados automáticamente"
}
```

**1.3 Ver usuarios precargados:**
```http
GET http://localhost:8083/users
```
**Respuesta esperada:** Array con 6 usuarios (3 clientes + 3 proveedores)

### FASE 2: OBTENER TOKEN JWT

**Razón:** Los endpoints PUT y DELETE requieren autenticación JWT. Spring Security valida que el token provenga del realm configurado.

**2.1 Token con usuario precargado:**
```http
POST http://localhost:8081/realms/proyecto-ms-realm/protocol/openid_connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&client_id=user-ms-client&client_secret=EjjsZvV6byq8zVfDlzScQDVvT3JwHWpi&username=juan.perez@test.com&password=password123&scope=openid
```

**Respuesta esperada:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "token_type": "Bearer",
  "scope": "openid email profile"
}
```

**Por qué funciona:** El usuario juan.perez@test.com fue creado por DbInitializer tanto en Keycloak como en la BD local con la misma contraseña.

### FASE 3: CREAR NUEVOS USUARIOS

**Razón:** Demuestra que la integración Keycloak funciona creando usuarios simultáneamente en ambos sistemas.

**3.1 Crear cliente:**
```http
POST http://localhost:8083/users
Content-Type: application/json

{
  "nombre": "Ana Rodríguez",
  "edad": 28,
  "correo": "ana.rodriguez@test.com",
  "password": "password123",
  "descripcion": "Nueva cliente",
  "direccion": "Carrera 15 #45-20",
  "telefono": "+57 301 234 5678",
  "tipoUsuario": "CLIENTE"
}
```

**Flujo interno:**
1. UserController.createUser() recibe la petición
2. KeycloakService.createKeycloakUser() crea el usuario en Keycloak
3. Keycloak devuelve un UUID único
4. Se crea la entidad Cliente con ese UUID
5. Se guarda en la base de datos H2

**Respuesta esperada:**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "nombre": "Ana Rodríguez",
  "correo": "ana.rodriguez@test.com",
  "edad": 28,
  "fechaRegistro": "2025-09-28T10:30:00Z"
}
```

**3.2 Crear proveedor:**
```http
POST http://localhost:8083/users
Content-Type: application/json

{
  "nombre": "Distribuidora Norte",
  "edad": 15,
  "correo": "ventas@distribuidoranorte.com",
  "password": "password123",
  "tipoUsuario": "PROVEEDOR",
  "paginaWeb": "https://distribuidoranorte.com",
  "redesSociales": ["@distrinorte"],
  "calificacionPromedio": 4.2
}
```

### FASE 4: ACTUALIZAR USUARIOS

**Razón:** Demuestra que las actualizaciones se sincronizan entre Keycloak y la BD local.

**4.1 Actualizar usuario (requiere JWT del paso 2.1):**
```http
PUT http://localhost:8083/users/REEMPLAZAR_CON_ID_REAL
Authorization: Bearer REEMPLAZAR_CON_JWT_TOKEN
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "edad": 31,
  "correo": "juan.perez@test.com",
  "descripcion": "Cliente VIP actualizado"
}
```

**Flujo interno:**
1. Spring Security valida el JWT token
2. Extrae el subject (user ID) del token
3. KeycloakService.updateKeycloakUser() actualiza en Keycloak
4. UsuarioService.updateUsuario() actualiza en BD local

### FASE 5: ELIMINAR USUARIOS

**5.1 Eliminar usuario (requiere JWT):**
```http
DELETE http://localhost:8083/users/REEMPLAZAR_CON_ID_REAL
Authorization: Bearer REEMPLAZAR_CON_JWT_TOKEN
```

**Flujo interno:**
1. KeycloakService.deleteKeyCloakUser() elimina de Keycloak
2. UsuarioService.deleteUsuario() elimina de BD local

---

## CÓMO COMPROBAR EL FUNCIONAMIENTO

### 1. EN H2 DATABASE CONSOLE

**Acceso:**
- URL: http://localhost:8083/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Usuario: sa
- Contraseña: (vacío)

**Consultas útiles:**
```sql
-- Ver todos los usuarios con sus IDs de Keycloak
SELECT id, nombre, correo, tipo_usuario, fecha_registro FROM USUARIO;

-- Contar usuarios por tipo
SELECT tipo_usuario, COUNT(*) as cantidad FROM USUARIO GROUP BY tipo_usuario;

-- Ver detalles específicos de clientes
SELECT u.*, c.direccion, c.telefono 
FROM USUARIO u 
JOIN CLIENTE c ON u.id = c.id 
WHERE u.tipo_usuario = 'CLIENTE';
```

### 2. EN KEYCLOAK ADMIN CONSOLE

**Acceso:**
- URL: http://localhost:8081
- Usuario: admin / Contraseña: admin

**Verificaciones:**
1. Ir a "Users" en el realm "proyecto-ms-realm"
2. Debe haber al menos 6 usuarios con emails de test
3. Cada usuario debe tener:
   - Email verificado: true
   - Enabled: true
   - Contraseña configurada

### 3. EN LOGS DE USER-MS

**Mensajes esperados:**
```
Conexión con Keycloak establecida correctamente
Iniciando carga de datos de prueba con integración Keycloak...
Usuario creado en Keycloak con ID: [UUID]
Cliente Juan Pérez creado con ID de Keycloak: [UUID]
Datos de prueba cargados exitosamente
Total de usuarios creados: 6
```

---

## SOLUCIÓN DE PROBLEMAS COMUNES

### Error: "Keycloak no está disponible"

**Causa:** Keycloak no está ejecutándose o el realm no existe
**Solución:**
1. Verificar que Keycloak esté en puerto 8081
2. Crear el realm "proyecto-ms-realm"
3. Reiniciar user-ms

### Error: "Usuario ya existe en Keycloak"

**Causa:** Intento de crear usuario duplicado
**Solución:** Normal, la validación está funcionando

### Error: "No se pudo actualizar en Keycloak"

**Causa:** JWT token inválido o usuario no existe en Keycloak
**Solución:**
1. Verificar que el token JWT es válido
2. Verificar que el usuario existe en Keycloak

### Error de conexión en H2

**Causa:** user-ms no está ejecutándose
**Solución:** Iniciar user-ms y verificar puerto 8083

---

## ORDEN DE EJECUCIÓN CORRECTO

1. **Iniciar Keycloak** (puerto 8081)
2. **Crear realm y client** en Keycloak
3. **Iniciar Config Server** (puerto 8888)
4. **Iniciar User-MS** (puerto 8083)
5. **Verificar logs** de DbInitializer
6. **Ejecutar pruebas HTTP** en orden

Este flujo garantiza que todos los componentes estén correctamente integrados y funcionando.