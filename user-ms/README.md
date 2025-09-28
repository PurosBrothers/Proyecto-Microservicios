# Microservicio de Usuario (user-ms)

## Descripción
Este microservicio gestiona la información de usuarios (clientes y proveedores) en el sistema de microservicios.

## Tecnologías
- Spring Boot 3.5.6
- Spring Data JPA
- H2 Database (para desarrollo)
- Spring Security con OAuth2 Resource Server (Keycloak)
- Eureka Client
- Spring Cloud Config

## Configuración
- **Puerto**: 8083
- **Base de datos**: H2 en memoria
- **Eureka Server**: localhost:9989
- **Config Server**: localhost:8888
- **Keycloak**: localhost:8081

## Endpoints principales

### Crear usuario
```
POST /usuarios
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "edad": 30,
  "correo": "juan@example.com",
  "direccion": "Calle 123",
  "telefono": "555-1234"
}
```

### Obtener usuario
```
GET /usuarios/{id}
Authorization: Bearer <token>
```

### Actualizar usuario
```
PUT /usuarios/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "edad": 31
}
```

### Eliminar usuario
```
DELETE /usuarios/{id}
Authorization: Bearer <token>
```

## Consola H2
Accede a http://localhost:8083/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **Usuario**: sa
- **Contraseña**: (vacía)

## Pruebas

### Ejecutar pruebas unitarias
```bash
mvn test
```

### Pruebas HTTP con archivos .http
Los archivos de prueba HTTP se encuentran en `src/test/java/com/microservicios/user_ms/http/`:

- `UsuarioController.http`: Pruebas de los endpoints REST del controlador de usuarios
- `ActuatorEndpoints.http`: Pruebas de los endpoints de actuator

Para ejecutar estas pruebas, puedes usar:
- **VS Code** con la extensión "REST Client"
- **IntelliJ IDEA** (soporte nativo para archivos .http)

### Pruebas de integración manuales

1. **Obtener token de Keycloak** (ejemplo con curl):
```bash
curl -X POST http://localhost:8081/realms/mi-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=mi-client&username=usuario&password=password&scope=openid"
```

2. **Crear usuario**:
```bash
curl -X POST http://localhost:8083/usuarios \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María García",
    "edad": 25,
    "correo": "maria@example.com",
    "direccion": "Avenida 456",
    "telefono": "555-5678"
  }'
```

3. **Obtener usuario**:
```bash
curl -X GET http://localhost:8083/usuarios/<user-id> \
  -H "Authorization: Bearer <token>"
```

## Datos iniciales
Los datos de ejemplo se cargan desde `import.sql` y contienen 3 usuarios de prueba.

## Arquitectura
- **Entity**: Usuario (abstracta), Cliente, Proveedor
- **Repository**: UsuarioRepository
- **Service**: UsuarioService
- **Controller**: UsuarioController
- **DTO**: UsuarioDTO
- **Mapper**: UsuarioMapper
- **Security**: SecurityConfig con JWT
- **Init**: DbInitializer para datos iniciales

## Notas importantes
- El ID del usuario se obtiene automáticamente del token JWT de Keycloak (campo `sub`)
- La base de datos se recrea en cada inicio (`ddl-auto: create-drop`)
- Requiere autenticación para todas las operaciones