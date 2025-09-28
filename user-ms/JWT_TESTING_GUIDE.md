# GUÍA COMPLETA DE PRUEBAS CON JWT

## 🎯 CÓMO PROBAR LOS ENDPOINTS CON AUTENTICACIÓN

### 1️⃣ PASO 1: OBTENER TOKEN JWT

```http
POST http://localhost:8083/users/authenticate
Content-Type: application/json

{
    "email": "laura.martinez@gmail.com",
    "password": "password123"
}
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "refreshToken": "refresh_token_placeholder",
  "expiresIn": 300,
  "tokenType": "Bearer",
  "username": "laura.martinez@gmail.com"
}
```

### 2️⃣ PASO 2: USAR EL TOKEN EN PETICIONES AUTENTICADAS

**Copia el `accessToken` y úsalo en el header `Authorization`:**

#### ✅ Test de Actualización (requiere JWT):
```http
PUT http://localhost:8083/users/2a36ca84-0166-46b2-80b4-0838987388bf
Authorization: Bearer [TU_TOKEN_AQUÍ]
Content-Type: application/json

{
    "nombre": "Laura Martínez Actualizada",
    "edad": 29,
    "correo": "laura.martinez@gmail.com"
}
```

#### ✅ Test sin Token (debería dar 401):
```http
PUT http://localhost:8083/users/2a36ca84-0166-46b2-80b4-0838987388bf
Content-Type: application/json

{
    "nombre": "Intento sin token"
}
```

### 3️⃣ ENDPOINTS POR TIPO DE ACCESO

#### 🔓 **PÚBLICOS** (no requieren token):
- `POST /users/authenticate` - Autenticación
- `GET /users/info` - Información del sistema
- `GET /users` - Listar usuarios
- `GET /users/{id}` - Obtener usuario específico
- `POST /users` - Crear usuario

#### 🔒 **PROTEGIDOS** (requieren JWT válido):
- `PUT /users/{id}` - Actualizar usuario
- `DELETE /users/{id}` - Eliminar usuario

### 4️⃣ USUARIOS DISPONIBLES PARA AUTENTICACIÓN

| Email | Password | Tipo |
|-------|----------|------|
| laura.martinez@gmail.com | password123 | Cliente |
| andres.silva@hotmail.com | password123 | Cliente |
| carmen.rodriguez@yahoo.com | password123 | Cliente |
| reservas@casaverde.com | password123 | Proveedor |
| info@saborcaribe.com | password123 | Proveedor |
| contacto@ecoaventuras.com | password123 | Proveedor |
| reservas@viajesfacil.com | password123 | Proveedor |

### 5️⃣ CÓDIGOS DE RESPUESTA ESPERADOS

| Situación | Código | Descripción |
|-----------|--------|-------------|
| Token válido | 200 | Petición exitosa |
| Sin token en endpoint protegido | 401 | No autorizado |
| Token inválido/expirado | 401 | No autorizado |
| Token válido pero recurso no existe | 404 | No encontrado |

### 6️⃣ MANEJO DE ERRORES COMUNES

#### ❌ Error 401 - Token expirado:
- **Solución:** Volver a autenticarse (los tokens duran 5 minutos)

#### ❌ Error 401 - Token malformado:
- **Solución:** Verificar que el header sea `Authorization: Bearer TOKEN`

#### ❌ Error 403 - Sin permisos suficientes:
- **Solución:** Usar un usuario con los permisos adecuados

### 7️⃣ EJEMPLO COMPLETO EN CURL

```bash
# 1. Obtener token
TOKEN=$(curl -s -X POST http://localhost:8083/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{"email":"laura.martinez@gmail.com","password":"password123"}' \
  | jq -r '.accessToken')

# 2. Usar token en petición protegida
curl -X PUT http://localhost:8083/users/2a36ca84-0166-46b2-80b4-0838987388bf \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laura Actualizada","edad":30}'
```

### 8️⃣ INTEGRACIÓN FRONTEND

```javascript
// 1. Autenticación
const authResponse = await fetch('/users/authenticate', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'laura.martinez@gmail.com',
    password: 'password123'
  })
});

const { accessToken } = await authResponse.json();

// 2. Almacenar token
localStorage.setItem('authToken', accessToken);

// 3. Usar en peticiones
const response = await fetch('/users/123', {
  method: 'PUT',
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(userData)
});
```

## 🎉 ¡SISTEMA COMPLETO!

Tu microservicio ahora tiene:
- ✅ Autenticación completa con Keycloak
- ✅ Endpoints protegidos con JWT
- ✅ CRUD completo con sincronización Keycloak
- ✅ Usuarios de prueba del marketplace turístico
- ✅ Validación de tokens automática
- ✅ Manejo de errores apropiado