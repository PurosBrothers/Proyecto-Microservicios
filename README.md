<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Cloud-2025.0.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Keycloak-OAuth2%20%2F%20OIDC-4E9FD1?style=for-the-badge&logo=keycloak&logoColor=white" />
  <img src="https://img.shields.io/badge/RabbitMQ-3--Management-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" />
  <img src="https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white" />
</p>

# 🏗️ Marketplace Microservices Platform

> **Plataforma de marketplace basada en arquitectura de microservicios** para la gestión integral de servicios turísticos y ecológicos. Permite a proveedores publicar servicios de alojamiento, alimentación, transporte y paseos ecológicos, mientras que los clientes pueden explorar, agregar al carrito, realizar transacciones y procesar pagos de forma segura.

---

## 📋 Tabla de Contenidos

- [🎯 Descripción General](#-descripción-general)
- [🏛️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
- [🛠️ Stack Tecnológico](#️-stack-tecnológico)
- [📦 Microservicios](#-microservicios)
  - [🌐 Gateway Server](#-gateway-server-puerto-8080)
  - [⚙️ Config Server](#️-config-server-puerto-8888)
  - [📡 Eureka Server](#-eureka-server-puerto-9989)
  - [👤 User Microservice](#-user-microservice-puerto-8083)
  - [🛒 Marketplace Microservice](#-marketplace-microservice-puerto-8084)
  - [💳 Payment Microservice](#-payment-microservice-puerto-8085)
  - [📊 Transaction Microservice](#-transaction-microservice-puerto-8082)
- [🔐 Seguridad y Autenticación](#-seguridad-y-autenticación)
- [📨 Comunicación entre Microservicios](#-comunicación-entre-microservicios)
- [📈 Monitoreo y Observabilidad](#-monitoreo-y-observabilidad)
- [🗄️ Modelo de Datos](#️-modelo-de-datos)
- [🚀 Despliegue y Ejecución](#-despliegue-y-ejecución)
- [🧪 Endpoints de la API](#-endpoints-de-la-api)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [🧩 Patrones y Buenas Prácticas](#-patrones-y-buenas-prácticas)

---

## 🎯 Descripción General

Esta plataforma es un **sistema distribuido de marketplace** diseñado con arquitectura de microservicios, orientado al sector de servicios turísticos y ecológicos. El sistema gestiona el ciclo completo de una transacción comercial:

1. **Registro y autenticación** de usuarios (clientes y proveedores) con gestión de identidades centralizada
2. **Catálogo de servicios** con clasificaciones polimórficas (alojamiento, alimentación, transporte, paseos ecológicos)
3. **Carrito de compras** con persistencia por usuario
4. **Procesamiento de transacciones** con estados de seguimiento
5. **Procesamiento de pagos** con cuentas bancarias simuladas y estados de pago

### ¿Por qué Microservicios?

| Beneficio | Implementación |
|-----------|---------------|
| **Desacoplamiento** | Cada servicio tiene su propia base de datos lógica y se comunica vía REST/mensajería |
| **Escalabilidad independiente** | Cada microservicio se despliega como contenedor Docker aislado |
| **Resiliencia** | Fallos en un servicio no propagan al resto gracias a Eureka y el Gateway |
| **Tecnología heterogénea** | Marketplace y Payment usan GraphQL; User y Transaction usan REST |
| **Despliegue continuo** | Docker Compose orquesta todos los servicios con un solo comando |

---

## 🏛️ Arquitectura del Sistema

```
                            ┌─────────────────────────┐
                            │     Frontend Client      │
                            │  (React/Angular/Vue)     │
                            └────────────┬────────────┘
                                         │ HTTP / HTTPS
                                         ▼
                            ┌─────────────────────────┐
                            │    🌐 Gateway Server     │
                            │       (Port 8080)        │
                            │  Spring Cloud Gateway    │
                            │  + OAuth2 Resource Server│
                            │  + CORS Configuration    │
                            │  + Auth Controller       │
                            └──────┬────┬────┬────┬───┘
                   ┌───────────────┘    │    │    └──────────────┐
                   │                    │    │                   │
                   ▼                    ▼    ▼                   ▼
        ┌──────────────────┐ ┌────────────────────┐ ┌──────────────────┐
        │  👤 User MS      │ │  🛒 Marketplace MS │ │  📊 Transaction  │
        │   (Port 8083)    │ │    (Port 8084)     │ │      MS          │
        │                  │ │                    │ │   (Port 8082)    │
        │  REST API        │ │  REST + GraphQL    │ │                  │
        │  JPA + Security  │ │  JPA + AMQP        │ │  REST + WebFlux  │
        │  Keycloak Admin  │ │  OAuth2 RS         │ │  JPA + AMQP      │
        └────────┬─────────┘ └────────┬───────────┘ └────────┬─────────┘
                 │                    │                      │
                 │     ┌──────────────┘                      │
                 │     │     ┌───────────────────────────────┘
                 ▼     ▼     ▼
        ┌──────────────────────────┐      ┌──────────────────────┐
        │    🐘 PostgreSQL 15      │      │   💳 Payment MS      │
        │      (Port 5432)         │      │    (Port 8085)       │
        │                          │      │                      │
        │  ├── marketplace_db      │      │  GraphQL API         │
        │  ├── user_ms_db          │      │  WebFlux + JPA       │
        │  └── keycloak_db         │      │  AMQP + Micrometer   │
        └──────────────────────────┘      └──────────┬───────────┘
                                                     │
                 ┌───────────────────────────────────┘
                 ▼
        ┌──────────────────────────┐
        │    🐇 RabbitMQ           │
        │    (Ports 5672/15672)    │
        │    Message Broker        │
        │    Async Communication   │
        └──────────────────────────┘

        ┌────────────────────────────────────────────────────────┐
        │                   Servicios de Soporte                  │
        │                                                         │
        │  ⚙️ Config Server (8888)  │  📡 Eureka Server (9989)   │
        │  Configuración centralizada│  Service Discovery          │
        │                            │                             │
        │  🔐 Keycloak (8081)       │  📈 Prometheus + Grafana   │
        │  Identity & Access Mgmt   │  Métricas y Dashboards      │
        └────────────────────────────────────────────────────────┘
```

---

## 🛠️ Stack Tecnológico

### Backend Core

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 17 (LTS) | Lenguaje principal de desarrollo |
| **Spring Boot** | 3.5.5 / 3.5.6 | Framework base de cada microservicio |
| **Spring Cloud** | 2025.0.0 | Ecosistema de microservicios (Config, Gateway, Eureka) |
| **Spring Cloud Gateway** | — | API Gateway reactivo con enrutamiento dinámico |
| **Spring Cloud Config** | — | Servidor de configuración centralizada |
| **Netflix Eureka** | — | Service Discovery y registro de servicios |
| **Spring Data JPA** | — | ORM y acceso a datos con Hibernate |
| **Spring Security** | — | Seguridad a nivel de servicio |
| **Spring WebFlux** | — | Programación reactiva (Gateway, Payment, Transaction) |
| **Spring for GraphQL** | — | API GraphQL para Marketplace y Payment |
| **Spring AMQP** | — | Integración con RabbitMQ para mensajería asíncrona |
| **Spring Boot Actuator** | — | Health checks, métricas y endpoints de monitoreo |
| **Project Lombok** | 1.18.30 | Reducción de boilerplate en entidades y DTOs |
| **Bean Validation** | — | Validación declarativa de datos de entrada |

### Seguridad

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Keycloak** | Latest (26.3.3 local) | Identity Provider — OAuth2 / OpenID Connect |
| **Spring OAuth2 Resource Server** | — | Validación de tokens JWT en cada microservicio |
| **Spring Security OAuth2 JOSE** | — | Procesamiento y verificación de JWT (JSON Web Tokens) |
| **Keycloak Admin Client** | 24.0.1 | Gestión programática de usuarios y roles en Keycloak |
| **RESTEasy Client** | 6.2.4.Final | Cliente JAX-RS para comunicación con Keycloak Admin API |

### Base de Datos

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **PostgreSQL** | 15 | Base de datos relacional principal |
| **Hibernate** | (Incluido en Spring Boot) | ORM — Mapeo objeto-relacional |
| **HikariCP** | (Incluido en Spring Boot) | Connection pooling de alto rendimiento |

### Mensajería y Comunicación

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **RabbitMQ** | 3-management | Message broker para comunicación asíncrona entre servicios |
| **AMQP 0-9-1** | — | Protocolo de mensajería (colas, exchanges, bindings) |

### Monitoreo y Observabilidad

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Prometheus** | 3.5.0 | Recolección y almacenamiento de métricas |
| **Grafana** | 12.1.1 | Visualización de métricas en dashboards |
| **Micrometer** | (Incluido) | Exportación de métricas en formato Prometheus |
| **Spring Boot Actuator** | — | Endpoints `/actuator/health`, `/actuator/prometheus` |

### Infraestructura y DevOps

| Tecnología | Propósito |
|-----------|-----------|
| **Docker** | Contenedorización de cada microservicio |
| **Docker Compose** | Orquestación local de todos los servicios (10 contenedores) |
| **Maven** | Build tool y gestión de dependencias |

### APIs

| Tipo | Servicios | Propósito |
|------|-----------|-----------|
| **REST** | User MS, Transaction MS, Gateway | APIs CRUD tradicionales con JSON |
| **GraphQL** | Marketplace MS, Payment MS | Consultas flexibles con schema tipado |

---

## 📦 Microservicios

### 🌐 Gateway Server (Puerto 8080)

> **Punto de entrada único** para todas las peticiones del frontend. Actúa como reverse proxy, balanceador de carga y capa de autenticación centralizada.

**Responsabilidades:**
- 🔀 **Enrutamiento dinámico** — Descubre servicios vía Eureka y enruta automáticamente
- 🔐 **Autenticación centralizada** — Gestiona registro y login con Keycloak
- 🛡️ **Validación JWT** — Verifica tokens OAuth2 en cada request entrante
- 🌍 **CORS** — Configuración global para frontends en puertos 3000, 4200, 5173, 8080
- 📁 **Multipart** — Soporte para carga de archivos hasta 10MB
- 🔄 **UID Replacement Filter** — Filtro personalizado para inyectar el UID del usuario autenticado

**Endpoints de autenticación:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/auth/register` | Registra usuario en Keycloak + user-ms |
| `POST` | `/auth/login` | Autentica y retorna tokens JWT |
| `GET` | `/auth/tipos-usuario` | Lista tipos disponibles (CLIENTE, PROVEEDOR) |
| `GET` | `/auth/verificar-correo?correo=` | Verifica si un correo ya está registrado |
| `GET` | `/auth/debug-keycloak` | Debug de configuración Keycloak |

**Tecnologías clave:** Spring Cloud Gateway, WebFlux, OAuth2 Resource Server, Keycloak Admin Client

---

### ⚙️ Config Server (Puerto 8888)

> **Servidor de configuración centralizada** que almacena y distribuye los archivos de configuración de todos los microservicios.

**Configuraciones gestionadas:**
- `gateway-server.yml` — Rutas, CORS, Keycloak, Eureka
- `marketplace-ms.yml` — Base de datos, GraphQL, RabbitMQ
- `payment-ms.yml` — Base de datos, GraphQL, Prometheus
- `transaction-ms.yml` — Base de datos, WebFlux, RabbitMQ
- `user-ms.yml` — Base de datos, Security, Keycloak

**Ventajas:**
- ✅ Un solo lugar para cambiar configuraciones de todos los servicios
- ✅ Perfiles por entorno (`docker`, `local`, `prod`)
- ✅ Recarga dinámica vía `/actuator/refresh`

---

### 📡 Eureka Server (Puerto 9989)

> **Registro y descubrimiento de servicios.** Cada microservicio se registra al iniciar y consulta Eureka para encontrar otros servicios.

**Servicios registrados:**
- `gateway-server`
- `user-ms`
- `marketplace-ms`
- `payment-ms`
- `transaction-ms`

**Dashboard:** Accesible en `http://localhost:9989` para visualizar el estado de todos los servicios registrados.

---

### 👤 User Microservice (Puerto 8083)

> **Gestión de usuarios** con herencia de entidades (Cliente/Proveedor) y sincronización bidireccional con Keycloak.

**Modelo de dominio:**

```
              ┌──────────────────────────┐
              │      Usuario (abstract)  │
              │──────────────────────────│
              │  id: String (UUID)       │
              │  nombre: String          │
              │  apellido: String        │
              │  edad: Integer           │
              │  correo: String (unique) │
              │  descripcion: String     │
              │  telefono: String        │
              │  direccion: String       │
              │  fotoUrl: String         │
              │  fotoData: byte[]        │
              │  fotoTipo: String        │
              │  tipoUsuario: Enum       │
              │  fechaRegistro: DateTime │
              └────────┬─────────────────┘
                       │
           ┌───────────┴───────────┐
           │                       │
    ┌──────┴──────┐     ┌──────────┴──────────┐
    │   Cliente   │     │     Proveedor       │
    │─────────────│     │─────────────────────│
    │ (hereda     │     │  paginaWeb: String  │
    │  todos los  │     │  redesSociales: []  │
    │  campos)    │     │  calificación: Float│
    └─────────────┘     └─────────────────────┘
```

**Estrategia de herencia:** `SINGLE_TABLE` con `@DiscriminatorColumn` — alta eficiencia en consultas, una sola tabla para todos los tipos de usuario.

**Endpoints REST:**

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `GET` | `/users` | ❌ | Listar todos los usuarios |
| `GET` | `/users/{id}` | ❌ | Obtener usuario por ID |
| `PUT` | `/users/{id}` | ✅ JWT | Actualizar usuario (sincroniza con Keycloak) |
| `DELETE` | `/users/{id}` | ✅ JWT | Eliminar usuario de BD y Keycloak |
| `POST` | `/users/internal/create` | 🔒 Interno | Crear usuario desde Gateway |
| `GET` | `/users/info` | ❌ | Información del sistema |

**Capas de la aplicación:**

```
Controller → Service → Repository → PostgreSQL
     │           │
     │           └── KeycloakService (sincronización)
     │
     └── UsuarioMapper (Entity ↔ DTO)
         ValidationExceptionHandler
         TipoUsuarioValidator (@ValidTipoUsuario)
```

---

### 🛒 Marketplace Microservice (Puerto 8084)

> **Catálogo de servicios turísticos** con sistema de clasificación polimórfica, calificaciones, comentarios, multimedia y búsqueda por categoría.

**Modelo de dominio:**

```
 ┌────────────────────────────────────────────────────────────────┐
 │                         Item                                   │
 │  id, titulo, descripcion, precio, stock, visualizaciones       │
 │  lugarInicio, capacidadMaxima, fechaPublicacion                │
 │  paisDestino, flag, population, gini, fifa, maps               │
 │  fechaDisponibilidadInicio, fechaDisponibilidadFin             │
 │────────────────────────────────────────────────────────────────│
 │  clasificacion ──────► Clasificacion (polimórfica)             │
 │  tags ───────────────► List<ItemTag>                           │
 │  fotos ──────────────► List<ItemFoto>                          │
 │  videos ─────────────► List<ItemVideo>                         │
 │  links ──────────────► List<ItemLink>                          │
 │  calificaciones ─────► List<Calificacion>                      │
 │  serviciosIncluidos ─► List<ServiciosIncluidos>                │
 │  preguntasFrecuentes ► List<PreguntaFrecuente>                 │
 └────────────────────────────────────────────────────────────────┘

        Clasificacion (SINGLE_TABLE, @DiscriminatorColumn)
        ├── Alojamiento      (tipo de hospedaje, amenidades)
        ├── Alimentacion      (restricciones dietéticas)
        ├── Transporte        (tipo de vehículo, ruta)
        └── PaseosEcologicos  (dificultad, duración)
```

**API dual (REST + GraphQL):**

*GraphQL Query de ejemplo:*
```graphql
query {
  itemsPorClasificacion(clasificacion: "Alojamiento") {
    id
    titulo
    descripcion
    precio
    clasificacion {
      tipo
      lugarInicio
      capacidadMaxima
    }
    tags { tag }
    fotos { url }
    calificaciones { puntuacion comentario }
    serviciosIncluidos { nombre descripcion }
  }
}
```

**Integración con RabbitMQ:**
- `RabbitMQSender` — Publica eventos de actualización de items
- `Receiver` — Escucha mensajes entrantes
- `UpdateItemReceiver` — Procesa actualizaciones de stock desde Transaction MS

**Seguridad:** OAuth2 Resource Server con JWT + `JwtSecurityContext` para extraer información del usuario autenticado.

---

### 💳 Payment Microservice (Puerto 8085)

> **Procesamiento de pagos** con gestión de cuentas bancarias simuladas, estados de pago y exposición de API vía GraphQL.

**Modelo de dominio:**

```
  ┌───────────────────────┐     ┌────────────────────────────┐
  │        Pago            │     │      ClienteBanco          │
  │────────────────────────│     │────────────────────────────│
  │  id: Long              │     │  id: Long                  │
  │  uid: String           │     │  uid: String (unique)      │
  │  reservaId: Long       │     │  cuentaBancariaEncrypted   │
  │  monto: BigDecimal     │     │  claveBancariaEncrypted    │
  │  fechaPago: DateTime   │     │  saldo: BigDecimal         │
  │  estadoPago: Enum      │     └────────────────────────────┘
  │    ├── PENDIENTE       │
  │    ├── COMPLETADO      │
  │    └── CANCELADO       │
  │  referencia: String    │
  └────────────────────────┘
```

**API GraphQL — Queries:**

| Query | Descripción |
|-------|-------------|
| `pagos` | Listar todos los pagos |
| `pago(id)` | Obtener pago por ID |
| `pagoByUid(uid)` | Obtener pago por UID del usuario |
| `pagosByReserva(reservaId)` | Pagos por ID de reserva |
| `clienteBancos` | Listar todas las cuentas bancarias |
| `clienteBancoByUid(uid)` | Cuenta bancaria por UID |

**API GraphQL — Mutations:**

| Mutation | Descripción |
|----------|-------------|
| `createPago(...)` | Crear nuevo pago |
| `updateEstadoPago(id, estado)` | Actualizar estado del pago |
| `deletePago(id)` | Eliminar pago |
| `createClienteBanco(...)` | Registrar cuenta bancaria |
| `updateCuentaBancaria(id, cuenta)` | Actualizar cuenta |
| `deleteClienteBanco(id)` | Eliminar cuenta bancaria |

**Monitoreo:** Integración con **Micrometer + Prometheus** para exportar métricas de rendimiento en `/actuator/prometheus`.

**Mensajería:** Escucha mensajes de procesamiento de pago desde Transaction MS vía RabbitMQ y publica confirmaciones.

---

### 📊 Transaction Microservice (Puerto 8082)

> **Gestión de transacciones y carrito de compras.** Orquesta el flujo completo desde agregar items al carrito hasta crear la transacción y solicitar el procesamiento de pago.

**Modelo de dominio:**

```
  ┌──────────────────────────┐       ┌────────────────────────┐
  │     CarritoCompra        │       │     Transaccion         │
  │──────────────────────────│       │─────────────────────────│
  │  id: Long                │       │  id: Long               │
  │  uid: String             │       │  UID: String            │
  │  items ──► ItemCarrito[] │       │  estado: String         │
  └──────────────────────────┘       │    ├── PENDING          │
                                     │    ├── COMPLETED        │
  ┌──────────────────────────┐       │    └── FAILED           │
  │     ItemCarrito          │       │  fechaTransaccion: Date │
  │──────────────────────────│       │  montoTotal: BigDecimal │
  │  id: Long                │       │  codigoConfirmacion     │
  │  itemId: Long            │       │  observaciones: String  │
  │  titulo: String          │       │  itemsPagados ──► []    │
  │  precio: BigDecimal      │       │  itemsPorPagar ──► []   │
  │  cantidad: Integer       │       └─────────────────────────┘
  └──────────────────────────┘
                                     ┌────────────────────────┐
                                     │   ItemTransaccion       │
                                     │─────────────────────────│
                                     │  id: Long               │
                                     │  itemId: Long           │
                                     │  titulo: String         │
                                     │  precio: BigDecimal     │
                                     │  cantidad: Integer      │
                                     └─────────────────────────┘
```

**Endpoints REST:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/carrito-compra/list-items-carrito` | Listar items del carrito por UID |
| `PUT` | `/carrito-compra/agregar-item-carrito?uid=` | Agregar item al carrito |
| `DELETE` | `/carrito-compra/delete-item-carrito?uid=&itemId=` | Eliminar item del carrito |
| `GET` | `/transaccion/list/{uid}` | Listar transacciones por UID |
| `POST` | `/transaccion/create/{uid}` | Crear transacción desde carrito |
| `POST` | `/transaccion/process-payment` | Crear transacción + enviar a Payment MS |

**Flujo de procesamiento de pago:**

```
1. Cliente POST /transaccion/process-payment {uid, itemIds}
2. Transaction MS crea la Transaccion desde el CarritoCompra
3. Transaction MS publica mensaje vía RabbitMQ → Payment MS
4. Payment MS procesa el pago y verifica saldo
5. Payment MS publica confirmación vía RabbitMQ → Transaction MS
6. Transaction MS actualiza estado: PENDING → COMPLETED / FAILED
7. Transaction MS publica actualización de stock → Marketplace MS
```

---

## 🔐 Seguridad y Autenticación

El sistema implementa **OAuth2 / OpenID Connect** con Keycloak como Identity Provider:

```
  ┌─────────────────┐     ┌────────────────────┐     ┌──────────────┐
  │    Frontend      │────►│   Gateway Server   │────►│   Keycloak   │
  │                  │     │                    │     │  (Port 8081) │
  │  1. POST /login  │     │  2. Forward to     │     │              │
  │                  │◄────│     Keycloak        │◄────│  3. Validate │
  │  4. Recibe JWT   │     │                    │     │     & Issue  │
  │                  │     │  5. Valida JWT en   │     │     Token   │
  │  6. Requests con │────►│     cada request    │     │              │
  │     Bearer Token │     │                    │     │  Realm:      │
  │                  │     │  7. Enruta al MS    │     │  proyect-ms  │
  └─────────────────┘     └────────────────────┘     │  -realm      │
                                                      └──────────────┘
```

**Características de seguridad:**
- 🔑 **JWT Tokens** — Emitidos por Keycloak, validados por cada microservicio
- 👥 **Roles** — CLIENTE y PROVEEDOR con permisos diferenciados
- 🛡️ **@PreAuthorize** — Control de acceso a nivel de endpoint
- 🔒 **Datos bancarios encriptados** — Cuenta y clave bancaria almacenados como texto cifrado
- 📧 **Verificación de unicidad de correo** — Validación en Keycloak antes de registrar
- 🗑️ **Limpieza transaccional** — Si falla la creación en user-ms, se elimina el usuario de Keycloak

**Configuración del Realm:**
- Realm: `proyect-ms-realm`
- Client ID: `admin-cli`
- Import automático de configuración al iniciar (`realm-config.json`)

---

## 📨 Comunicación entre Microservicios

### Comunicación Síncrona (REST/HTTP)

| Origen | Destino | Propósito |
|--------|---------|-----------|
| Gateway → User MS | `POST /users/internal/create` | Crear usuario después del registro en Keycloak |
| Gateway → Keycloak | Admin REST API | CRUD de usuarios, obtención de tokens |
| Marketplace MS → User MS | Validación de proveedor | Verificar que el usuario es un proveedor válido |

### Comunicación Asíncrona (RabbitMQ)

```
┌──────────────────┐                    ┌──────────────────┐
│  Transaction MS   │  ──── Queue ────► │   Payment MS      │
│                   │  ProcessPayment   │                   │
│  "Procesar pago   │  Message          │  "Procesar pago   │
│   de transacción" │                   │   y confirmar"    │
└──────────────────┘                    └──────────────────┘
        ▲                                        │
        │         PaymentConfirmation            │
        └──────────── Queue ◄────────────────────┘

┌──────────────────┐                    ┌──────────────────┐
│  Transaction MS   │  ──── Queue ────► │  Marketplace MS   │
│                   │  UpdateItem       │                   │
│  "Actualizar      │  Message          │  "Decrementar     │
│   stock del item" │                   │   stock del item" │
└──────────────────┘                    └──────────────────┘

┌──────────────────┐                    ┌──────────────────┐
│  Marketplace MS   │  ──── Queue ────► │  Transaction MS   │
│                   │  AddToCart        │                   │
│  "Agregar item    │  Message          │  "Agregar al      │
│   al carrito"     │                   │   carrito"        │
└──────────────────┘                    └──────────────────┘
```

**Configuración de RabbitMQ:**
- Puerto AMQP: `5672`
- Panel de gestión: `http://localhost:15672` (usuario: `guest` / contraseña: `guest`)

---

## 📈 Monitoreo y Observabilidad

### Stack de Monitoreo

| Componente | Puerto | Propósito |
|-----------|--------|-----------|
| **Prometheus** | — | Scraping de métricas desde `/actuator/prometheus` |
| **Grafana** | — | Dashboards interactivos de rendimiento |
| **Spring Actuator** | `/actuator/*` | Health checks, info, métricas por servicio |

### Métricas Expuestas

Los microservicios **Payment MS** y **Transaction MS** integran `micrometer-registry-prometheus` para exportar:
- Latencia de requests HTTP
- Tasa de errores
- Uso de conexiones de base de datos (HikariCP)
- Métricas de colas de RabbitMQ
- JVM memory, threads, garbage collection

### Actuator Endpoints (Gateway)

| Endpoint | Propósito |
|----------|-----------|
| `/actuator/health` | Estado de salud del servicio |
| `/actuator/info` | Información del servicio |
| `/actuator/gateway/routes` | Rutas configuradas en el gateway |
| `/actuator/refresh` | Recarga de configuración desde Config Server |

---

## 🗄️ Modelo de Datos

### Bases de Datos

El sistema utiliza una instancia de **PostgreSQL 15** con 3 bases de datos lógicas:

| Base de Datos | Microservicio(s) | Descripción |
|--------------|-------------------|-------------|
| `marketplace_db` | Marketplace MS, Transaction MS, Payment MS | Catálogo, transacciones y pagos |
| `user_ms_db` | User MS | Usuarios, clientes y proveedores |
| `keycloak_db` | Keycloak | Identidades, roles y sesiones |

### Diagrama E-R Simplificado

```
┌─────────────────────── user_ms_db ───────────────────────┐
│                                                           │
│  usuario (SINGLE_TABLE inheritance)                       │
│  ├── id (PK, UUID from Keycloak)                         │
│  ├── nombre, apellido, correo (UNIQUE)                   │
│  ├── edad, descripcion, telefono, direccion              │
│  ├── foto_url, foto_data (BYTEA), foto_tipo              │
│  ├── tipo_usuario (discriminator)                         │
│  ├── tipo_usuario_enum (CLIENTE | PROVEEDOR)             │
│  ├── fecha_registro                                       │
│  ├── pagina_web (solo PROVEEDOR)                         │
│  └── calificacion_promedio (solo PROVEEDOR)              │
│                                                           │
│  proveedor_redes_sociales (collection table)             │
│  ├── proveedor_id (FK → usuario.id)                      │
│  └── red_social                                           │
└───────────────────────────────────────────────────────────┘

┌─────────────────────── marketplace_db ───────────────────┐
│                                                           │
│  item                     clasificacion (SINGLE_TABLE)   │
│  ├── id (PK)              ├── id (PK)                    │
│  ├── titulo               ├── tipo (discriminator)       │
│  ├── descripcion          │   ├── Alojamiento            │
│  ├── precio               │   ├── Alimentacion           │
│  ├── stock                │   ├── Transporte             │
│  ├── clasificacion_id(FK) │   └── PaseosEcologicos       │
│  ├── pais_destino, flag   ├── usuario_id                 │
│  └── ...                  ├── precio, lugar_inicio       │
│                           └── capacidad_maxima           │
│  item_tag, item_foto,                                    │
│  item_video, item_link,                                  │
│  calificacion, servicios_incluidos,                      │
│  preguntas_frecuentes,                                   │
│  requisitos_especiales,                                  │
│  restricciones_dieteticas                                │
└───────────────────────────────────────────────────────────┘
```

---

## 🚀 Despliegue y Ejecución

### Prerequisitos

| Herramienta | Versión Mínima |
|------------|----------------|
| **Docker** | 20.10+ |
| **Docker Compose** | 2.0+ |
| **Java** | 17+ (solo para desarrollo local) |
| **Maven** | 3.8+ (solo para desarrollo local) |

### Despliegue con Docker Compose (Recomendado)

```bash
# 1. Clonar el repositorio
git clone <url-del-repositorio>
cd Proyecto-Microservicios

# 2. Levantar todos los servicios (10 contenedores)
docker-compose up --build

# 3. Verificar que todos los servicios estén corriendo
docker-compose ps
```

**Orden de inicio (gestionado por `depends_on`):**

```
1. PostgreSQL (healthcheck: pg_isready)
2. Config Server
3. Eureka Server
4. RabbitMQ
5. Keycloak (espera a PostgreSQL healthy)
6. Gateway Server
7. User MS, Marketplace MS, Payment MS, Transaction MS
```

### Puertos del Sistema

| Servicio | Puerto | URL |
|----------|--------|-----|
| 🌐 Gateway Server | `8080` | `http://localhost:8080` |
| 🔐 Keycloak | `8081` | `http://localhost:8081` |
| 📊 Transaction MS | `8082` | `http://localhost:8082` |
| 👤 User MS | `8083` | `http://localhost:8083` |
| 🛒 Marketplace MS | `8084` | `http://localhost:8084` |
| 💳 Payment MS | `8085` | `http://localhost:8085` |
| ⚙️ Config Server | `8888` | `http://localhost:8888` |
| 📡 Eureka Dashboard | `9989` | `http://localhost:9989` |
| 🐇 RabbitMQ Management | `15672` | `http://localhost:15672` |
| 🐘 PostgreSQL | `5432` | `localhost:5432` |

### Red de Docker

Todos los servicios operan en una red bridge personalizada (`my_network`) con subnet `172.25.0.0/16` e IPs estáticas asignadas para garantizar resolución DNS confiable entre contenedores.

---

## 🧪 Endpoints de la API

### Flujo Completo de Ejemplo

```bash
# 1. Registrar un proveedor
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Jason",
    "apellido": "Todd",
    "correo": "jasontodd@gmail.com",
    "password": "password123",
    "tipoUsuario": "PROVEEDOR",
    "edad": 20,
    "descripcion": "Guía turístico",
    "telefono": "+573123164634",
    "direccion": "Bogotá, Colombia",
    "paginaWeb": "https://miturismo.com",
    "redesSociales": ["@jasontodd"]
  }'

# 2. Iniciar sesión
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jasontodd@gmail.com",
    "password": "password123"
  }'
# → Retorna access_token JWT

# 3. Consultar items del marketplace (GraphQL)
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "query": "{ itemsPorClasificacion(clasificacion: \"Alojamiento\") { id titulo precio } }"
  }'

# 4. Agregar item al carrito
curl -X PUT "http://localhost:8082/carrito-compra/agregar-item-carrito?uid=<USER_ID>" \
  -H "Content-Type: application/json" \
  -d '{"itemId": 1, "titulo": "Hotel Bogotá", "precio": 150000, "cantidad": 1}'

# 5. Procesar pago
curl -X POST http://localhost:8082/transaccion/process-payment \
  -H "Content-Type: application/json" \
  -d '{"uid": "<USER_ID>", "itemIds": [1]}'
```

---

## 📁 Estructura del Proyecto

```
Proyecto-Microservicios/
│
├── 🌐 gateway_server/                 # API Gateway (Spring Cloud Gateway)
│   └── src/main/java/.../
│       ├── config/SecurityConfig       # OAuth2 + JWT configuration
│       ├── controller/AuthController   # Register, Login, tipos-usuario
│       ├── dto/                        # AuthRequest, RegistroUsuarioDto
│       ├── service/                    # KeycloakService, UserClientService
│       ├── filter/UidReplacementFilter # Custom GatewayFilter
│       └── validation/                 # @ValidTipoUsuario
│
├── ⚙️ config-server/                   # Configuración centralizada
│   └── src/main/resources/config/
│       ├── gateway-server.yml
│       ├── marketplace-ms.yml
│       ├── payment-ms.yml
│       ├── transaction-ms.yml
│       └── user-ms.yml
│
├── 📡 eureka_server/                   # Service Discovery (Netflix Eureka)
│
├── 👤 user-ms/                         # Microservicio de Usuarios
│   └── src/main/java/.../
│       ├── entity/                     # Usuario (abstract), Cliente, Proveedor
│       ├── enums/TipoUsuario           # CLIENTE, PROVEEDOR
│       ├── controller/                 # UserController, ImageController
│       ├── service/                    # UsuarioService, KeycloakService
│       ├── repository/                 # UsuarioRepository (JPA)
│       ├── mapper/UsuarioMapper        # Entity ↔ DTO mapping
│       ├── dto/                        # UsuarioDTO, CreateUserRequestDTO
│       ├── config/                     # SecurityConfig, RabbitMQConfig
│       ├── init/DbInitializer          # Seed data on startup
│       ├── validation/                 # Custom validators
│       └── exception/                  # GlobalExceptionHandler
│
├── 🛒 marketplace-ms/                  # Microservicio del Marketplace
│   └── src/main/java/.../
│       ├── entities/                   # Item, Clasificacion*, Calificacion...
│       │   ├── Clasificacion (abstract)
│       │   ├── Alojamiento, Alimentacion, Transporte, PaseosEcologicos
│       │   ├── ItemTag, ItemFoto, ItemVideo, ItemLink
│       │   ├── Calificacion, Comentario, ServiciosIncluidos
│       │   ├── PreguntaFrecuente, RequisitosEspeciales
│       │   └── RestriccionesDieteticas, Maps
│       ├── controllers/                # REST controllers
│       ├── services/                   # ItemService, ClasificacionService...
│       ├── repositories/              # 12+ JPA repositories
│       ├── mappers/                    # 9 mapper classes (Factory pattern)
│       ├── messagingrabbitmq/         # RabbitMQ sender, receivers, config
│       ├── security/                   # JwtSecurityContext
│       └── graphql/schema.graphqls    # GraphQL schema
│
├── 💳 payment-ms/                      # Microservicio de Pagos
│   └── src/main/java/.../
│       ├── models/                     # Pago, ClienteBanco, EstadoPago
│       ├── resolvers/                  # GraphQL resolvers (Query + Mutation)
│       ├── services/                   # PagoService, ClienteBancoService
│       ├── repository/                 # PagoRepository, ClienteBancoRepo
│       ├── dtos/                       # PaymentConfirmationMessageDTO
│       ├── messagingrabbitmq/         # RabbitMQ config, sender, receiver
│       └── graphql/schema.graphqls    # GraphQL schema (Queries + Mutations)
│
├── 📊 transaction-ms/                  # Microservicio de Transacciones
│   └── src/main/java/.../
│       ├── models/                     # Transaccion, CarritoCompra, ItemCarrito
│       ├── controllers/               # TransaccionController, CarritoController
│       ├── services/                   # TransaccionService, CarritoService...
│       ├── repository/                # 4 JPA repositories
│       ├── dtos/                       # 9 DTOs for messaging & API
│       ├── mappers/                    # TransaccionMapper, CarritoMapper...
│       └── messagingrabbitmq/         # RabbitMQ config, sender, receiver
│
├── 🔐 keycloak-config/                # Keycloak realm export
│   └── realm-config.json              # Auto-imported on Keycloak startup
│
├── 📈 prometheus-3.5.0.linux-amd64/   # Prometheus server
├── 📊 grafana-12.1.1/                  # Grafana dashboards
│
├── 🐘 init-db.sql                      # PostgreSQL init (3 databases)
├── 🐘 create-user-db.sql               # User DB creation script
├── 🐳 docker-compose.yml               # Orquestación de 10 servicios
└── 📄 README.md                         # Este archivo
```

---

## 🧩 Patrones y Buenas Prácticas

### Patrones de Diseño Implementados

| Patrón | Implementación |
|--------|---------------|
| **API Gateway** | Spring Cloud Gateway como punto de entrada único |
| **Service Discovery** | Netflix Eureka para registro y descubrimiento dinámico |
| **Centralized Configuration** | Spring Cloud Config Server con perfiles por entorno |
| **Database per Service** | Cada servicio usa su propia base de datos lógica |
| **Event-Driven Architecture** | RabbitMQ para comunicación asíncrona entre servicios |
| **CQRS (parcial)** | GraphQL para consultas complejas, REST para comandos |
| **DTO Pattern** | Separación entre entidades JPA y objetos de transferencia |
| **Mapper Pattern** | Clases mapper dedicadas (Entity ↔ DTO) |
| **Factory Pattern** | `ClasificacionFactory` para crear subtipos polimórficos |
| **Template Method** | Clase abstracta `Usuario` con validación `@PrePersist` |
| **Single Table Inheritance** | `Usuario` y `Clasificacion` con `@DiscriminatorColumn` |
| **Repository Pattern** | Spring Data JPA repositories para acceso a datos |
| **Saga Pattern (orquestado)** | Flujo de pago: Transaction → Payment → Confirmación |
| **Compensating Transaction** | Rollback de Keycloak si falla creación en user-ms |

### Buenas Prácticas

- ✅ **Configuración externalizada** — Todos los valores configurables en Config Server
- ✅ **Perfiles de entorno** — `docker` y `local` separados
- ✅ **Health checks** — PostgreSQL con healthcheck antes de permitir arranque de servicios
- ✅ **Restart policies** — `restart: on-failure` en todos los contenedores
- ✅ **Validación de entrada** — `@Valid`, `@NotBlank`, custom validators (`@ValidTipoUsuario`)
- ✅ **Manejo de errores** — `GlobalExceptionHandler`, `GraphQLExceptionResolver`
- ✅ **Logging estructurado** — Emojis + `@Slf4j` para trazabilidad en logs
- ✅ **Datos encriptados** — Información bancaria almacenada como texto cifrado
- ✅ **Idempotencia** — IDs de usuario generados por Keycloak (UUID), no auto-increment
- ✅ **IPs estáticas en Docker** — Red personalizada con subnet fija para consistencia

---

<p align="center">
  <b>Desarrollado con</b> ☕ <b>Java 17</b> + 🍃 <b>Spring Boot</b> + 🐳 <b>Docker</b>
</p>
