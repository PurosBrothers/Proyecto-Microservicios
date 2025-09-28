# CONFIGURACIÓN DE KEYCLOAK PASO A PASO

## ❌ PROBLEMA IDENTIFICADO
El realm `proyect-ms-realm` no existe en Keycloak, por eso todos los intentos de autenticación fallan con "Protocol not found".

## ✅ SOLUCIÓN - CREAR REALM Y CLIENTE EN KEYCLOAK

### Paso 1: Acceder a Keycloak Admin Console
1. Ve a: http://localhost:8081
2. Click en "Administration Console"
3. Login: admin / admin123

### Paso 2: Crear el Realm
1. En la esquina superior izquierda, click en el dropdown "Keycloak"
2. Click en "Create realm"
3. **Realm name:** `proyect-ms-realm` (con "y", no "ecto")
4. Click "Create"

### Paso 3: Crear el Cliente
1. Ve a **Clients** en el menú lateral
2. Click **"Create client"**
3. **Client ID:** `user-ms-client`
4. **Client type:** `OpenID Connect`
5. Click **"Next"**

### Paso 4: Configurar el Cliente (CRÍTICO)
En la página "Capability config":
- ✅ **Client authentication:** `ON` 
- ❌ **Authorization:** `OFF`
- ✅ **Standard flow:** `ON`
- ✅ **Direct access grants:** `ON` ⭐ **MUY IMPORTANTE**
- ❌ **Implicit flow:** `OFF`
- ❌ **Service accounts roles:** `OFF`
6. Click **"Save"**

### Paso 5: Configurar Client Secret
1. Ve a la pestaña **"Credentials"**
2. **Opción A:** Usar el secret existente en la configuración:
   - Si el secret generado es diferente a `Aq3vK9EfTGwoWpK3j48RkN6Q0yCUZqGd`
   - Copia el nuevo secret y actualiza `user-ms.yml`
3. **Opción B:** Cambiar el secret (más fácil):
   - Click en "Regenerate secret"
   - Usa: `Aq3vK9EfTGwoWpK3j48RkN6Q0yCUZqGd`

### Paso 6: Crear Usuario de Prueba (Opcional)
1. Ve a **Users** en el menú lateral
2. Click **"Create new user"**
3. **Username:** `laura.martinez@gmail.com`
4. **Email:** `laura.martinez@gmail.com`
5. **First name:** `Laura`
6. **Last name:** `Martínez`
7. **Email verified:** `ON`
8. Click **"Create"**

### Paso 7: Establecer Contraseña
1. En el usuario creado, ve a la pestaña **"Credentials"**
2. Click **"Set password"**
3. **Password:** `password123`
4. **Password confirmation:** `password123`
5. **Temporary:** `OFF` ⭐ (para que no pida cambiar contraseña)
6. Click **"Set password"**

## 🧪 VERIFICAR CONFIGURACIÓN

### Test 1: Verificar que el realm existe
```bash
curl http://localhost:8081/realms/proyect-ms-realm/.well-known/openid_configuration
```
Debería devolver la configuración OpenID, no un error.

### Test 2: Probar autenticación directa
```bash
curl -X POST http://localhost:8081/realms/proyect-ms-realm/protocol/openid_connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=user-ms-client&client_secret=Aq3vK9EfTGwoWpK3j48RkN6Q0yCUZqGd&username=laura.martinez@gmail.com&password=password123"
```

### Test 3: Probar endpoint del microservicio
```http
POST http://localhost:8083/users/authenticate
Content-Type: application/json

{
    "email": "laura.martinez@gmail.com",
    "password": "password123"
}
```

## 📝 NOTA IMPORTANTE
Una vez creado el realm y cliente en Keycloak, el DbInitializer del microservicio automáticamente creará todos los usuarios de prueba del marketplace turístico la próxima vez que arranque.

## 🔧 USUARIOS QUE SE CREARÁN AUTOMÁTICAMENTE:
- laura.martinez@gmail.com
- andres.silva@hotmail.com
- carmen.rodriguez@yahoo.com
- reservas@casaverde.com
- info@saborcaribe.com
- contacto@ecoaventuras.com
- reservas@viajesfacil.com

Todos con contraseña: `password123`