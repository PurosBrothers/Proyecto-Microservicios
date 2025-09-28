### CONFIGURACIÓN DEL CLIENTE KEYCLOAK PARA AUTENTICACIÓN

## Problema Detectado
El endpoint de autenticación está fallando porque el cliente `user-ms-client` en Keycloak 
no está configurado correctamente para permitir "Direct Access Grants" (Resource Owner Password Credentials).

## Pasos para Configurar el Cliente en Keycloak:

### 1. Acceder a Keycloak Admin Console
- URL: http://localhost:8081
- Usuario: admin  
- Contraseña: admin123

### 2. Seleccionar el Realm
- En la esquina superior izquierda, seleccionar `proyect-ms-realm`

### 3. Configurar el Cliente user-ms-client

#### 3.1 Ir a Clients
- Click en "Clients" en el menú lateral izquierdo

#### 3.2 Crear o Editar el Cliente
Si no existe `user-ms-client`:
- Click "Create client"
- Client ID: `user-ms-client`
- Client type: `OpenID Connect`
- Click "Next"

#### 3.3 Configurar Capability Settings (MUY IMPORTANTE)
- **Client authentication**: `ON` (habilitado)
- **Authorization**: `OFF` (deshabilitado)  
- **Standard flow**: `ON` (habilitado)
- **Direct access grants**: `ON` ⭐ **CRÍTICO - DEBE ESTAR HABILITADO**
- **Implicit flow**: `OFF` (deshabilitado)
- **Service accounts roles**: `OFF` (deshabilitado)

#### 3.4 Configurar Client Secret
- Ve a la pestaña "Credentials"
- Verifica que el Client Secret sea: `Aq3vK9EfTGwoWpK3j48RkN6Q0yCUZqGd`
- Si es diferente, copia el secreto y actualiza la configuración del microservicio

### 4. Verificar Usuarios
- Ve a "Users" en el menú lateral
- Verifica que los usuarios del marketplace turístico estén creados:
  - laura.martinez@gmail.com
  - andres.silva@hotmail.com
  - reservas@casaverde.com
  - info@saborcaribe.com
  - etc.

### 5. Probar Autenticación
Una vez configurado el cliente, los tests de autenticación deberían funcionar:

```http
POST http://localhost:8083/users/authenticate
Content-Type: application/json

{
    "email": "laura.martinez@gmail.com",
    "password": "password123"
}
```

## Usuarios de Prueba Disponibles:

### Clientes:
- laura.martinez@gmail.com / password123
- andres.silva@hotmail.com / password123  
- carmen.rodriguez@yahoo.com / password123

### Proveedores:
- reservas@casaverde.com / password123
- info@saborcaribe.com / password123
- contacto@ecoaventuras.com / password123
- reservas@viajesfacil.com / password123

## Notas Técnicas:
- El endpoint `/users/authenticate` usa REST API directo de Keycloak
- Requiere que "Direct access grants" esté habilitado en el cliente
- El client secret debe coincidir con la configuración del microservicio
- El realm debe ser `proyect-ms-realm`