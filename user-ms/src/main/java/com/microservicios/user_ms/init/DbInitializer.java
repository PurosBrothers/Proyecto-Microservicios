package com.microservicios.user_ms.init;

import com.microservicios.user_ms.entity.Cliente;
import com.microservicios.user_ms.entity.Proveedor;
import com.microservicios.user_ms.enums.TipoUsuario;
import com.microservicios.user_ms.repository.UsuarioRepository;
import com.microservicios.user_ms.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final KeycloakService keycloakService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carga de datos de prueba con integración Keycloak...");

        // Limpiar todos los usuarios existentes
        clearAllUsers();

        // Verificar conexión con Keycloak
        if (!keycloakService.isKeycloakAvailable()) {
            log.warn("Keycloak no está disponible. Los datos se crearán sin integración Keycloak.");
        }

        // Crear clientes de prueba con integración Keycloak
        createTestClientesWithKeycloak();

        // Crear proveedores de prueba con integración Keycloak
        createTestProveedoresWithKeycloak();

        log.info("✅ Inicialización completada - Usuarios de prueba creados exitosamente");

        log.info("Datos de prueba cargados exitosamente");
        log.info("Total de usuarios creados: {}", usuarioRepository.count());

        // Información útil para pruebas
        printTestInformation();
    }

    private void clearAllUsers() {
        log.info("Eliminando todos los usuarios existentes...");
        
        long userCount = usuarioRepository.count();
        if (userCount > 0) {
            log.info("Encontrados {} usuarios en la base de datos", userCount);
            
            // Eliminar todos los usuarios de la base de datos local
            usuarioRepository.deleteAll();
            
            // Opcional: También eliminar usuarios de Keycloak
            // Nota: Esto eliminaría TODOS los usuarios del realm, usar con precaución
            // keycloakService.deleteAllUsers();
            
            log.info("Todos los usuarios han sido eliminados de la base de datos local");
        } else {
            log.info("No se encontraron usuarios existentes");
        }
    }

    private void createTestClientesWithKeycloak() {
        // Cliente 1 - Persona joven que busca aventuras
        String email1 = "laura.martinez@gmail.com";
        String keycloakId1 = null;
        
        if (keycloakService.userExistsInKeycloak(email1)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", email1);
            keycloakId1 = keycloakService.getUserKeycloakId(email1);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId1, TipoUsuario.CLIENTE);
        } else {
            keycloakId1 = keycloakService.createKeycloakUserWithRole(email1, "Laura", "Martínez", "password123", TipoUsuario.CLIENTE);
        }
        
        if (keycloakId1 != null) {
            Cliente cliente1 = new Cliente();
            cliente1.setId(keycloakId1);
            cliente1.setNombre("Laura Martínez");
            cliente1.setEdad(28);
            cliente1.setFotoUrl("https://randomuser.me/api/portraits/women/1.jpg");
            cliente1.setDescripcion("Amante de la naturaleza y los viajes en familia. Siempre buscando nuevas aventuras por Colombia ✈️🌿");
            cliente1.setCorreo("laura.martinez@gmail.com");
            cliente1.setDireccion("Carrera 15 #45-20, Bogotá, Colombia");
            cliente1.setTelefono("+57 301 456 7890");
            cliente1.setTipoUsuario(TipoUsuario.CLIENTE); // CAMPO OBLIGATORIO
            usuarioRepository.save(cliente1);
            log.info("Cliente Laura Martínez creado/actualizado con ID de Keycloak: {}", keycloakId1);
        }

        // Cliente 2 - Profesional que viaja por trabajo
        String email2 = "andres.silva@hotmail.com";
        String keycloakId2 = null;
        
        if (keycloakService.userExistsInKeycloak(email2)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", email2);
            keycloakId2 = keycloakService.getUserKeycloakId(email2);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId2, TipoUsuario.CLIENTE);
        } else {
            keycloakId2 = keycloakService.createKeycloakUserWithRole(email2, "Andrés", "Silva", "password123", TipoUsuario.CLIENTE);
        }
        
        if (keycloakId2 != null) {
            Cliente cliente2 = new Cliente();
            cliente2.setId(keycloakId2);
            cliente2.setNombre("Andrés Silva");
            cliente2.setEdad(34);
            cliente2.setFotoUrl("https://randomuser.me/api/portraits/men/2.jpg");
            cliente2.setDescripcion("Ingeniero que aprovecha los viajes de trabajo para conocer lugares increíbles. Weekend explorer 🏔️📸");
            cliente2.setCorreo("andres.silva@hotmail.com");
            cliente2.setDireccion("Calle 72 #11-35, Medellín, Colombia");
            cliente2.setTelefono("+57 312 789 0123");
            cliente2.setTipoUsuario(TipoUsuario.CLIENTE); // CAMPO OBLIGATORIO
            usuarioRepository.save(cliente2);
            log.info("Cliente Andrés Silva creado/actualizado con ID de Keycloak: {}", keycloakId2);
        }

        // Cliente 3 - Familia que planifica vacaciones
        String email3 = "carmen.rodriguez@yahoo.com";
        String keycloakId3 = null;
        
        if (keycloakService.userExistsInKeycloak(email3)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", email3);
            keycloakId3 = keycloakService.getUserKeycloakId(email3);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId3, TipoUsuario.CLIENTE);
        } else {
            keycloakId3 = keycloakService.createKeycloakUserWithRole(email3, "Carmen", "Rodríguez", "password123", TipoUsuario.CLIENTE);
        }
        
        if (keycloakId3 != null) {
            Cliente cliente3 = new Cliente();
            cliente3.setId(keycloakId3);
            cliente3.setNombre("Carmen Rodríguez");
            cliente3.setEdad(42);
            cliente3.setFotoUrl("https://randomuser.me/api/portraits/women/3.jpg");
            cliente3.setDescripcion("Mamá de dos niños que ama planear vacaciones familiares perfectas. Buscando experiencias inolvidables 👨‍👩‍👧‍👦🌊");
            cliente3.setCorreo("carmen.rodriguez@yahoo.com");
            cliente3.setDireccion("Transversal 45 #123-67, Cartagena, Colombia");
            cliente3.setTelefono("+57 318 234 5678");
            cliente3.setTipoUsuario(TipoUsuario.CLIENTE); // CAMPO OBLIGATORIO
            usuarioRepository.save(cliente3);
            log.info("Cliente Carmen Rodríguez creado/actualizado con ID de Keycloak: {}", keycloakId3);
        }

        log.info("Proceso de creación de clientes completado");
    }

    private void createTestProveedoresWithKeycloak() {
        // Proveedor 1 - Hotel Boutique
        String emailP1 = "reservas@casaverde.com";
        String keycloakId1 = null;
        
        if (keycloakService.userExistsInKeycloak(emailP1)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", emailP1);
            keycloakId1 = keycloakService.getUserKeycloakId(emailP1);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId1, TipoUsuario.PROVEEDOR);
        } else {
            keycloakId1 = keycloakService.createKeycloakUserWithRole(emailP1, "Casa Verde", "Hotel", "password123", TipoUsuario.PROVEEDOR);
        }
        
        if (keycloakId1 != null) {
            Proveedor proveedor1 = new Proveedor();
            proveedor1.setId(keycloakId1);
            proveedor1.setNombre("Casa Verde Hotel Boutique");
            proveedor1.setEdad(12); // Años en el mercado turístico
            proveedor1.setFotoUrl("https://randomuser.me/api/portraits/women/4.jpg");
            proveedor1.setDescripcion("Hotel boutique ecológico en el corazón del Eje Cafetero. Alojamiento sostenible con vista a las montañas");
            proveedor1.setCorreo("reservas@casaverde.com");
            proveedor1.setDireccion("Km 7 Vía Armenia-Salento, Quindío, Colombia"); // CAMPO REQUERIDO
            proveedor1.setTelefono("+57 6 789 4561");
            proveedor1.setTipoUsuario(TipoUsuario.PROVEEDOR); // CAMPO OBLIGATORIO
            proveedor1.setPaginaWeb("https://casaverdehotel.com");
            proveedor1.setRedesSociales(Arrays.asList("@casaverdehotel", "casaverde_colombia", "Casa Verde Hotel"));
            proveedor1.setCalificacionPromedio(4.7f);
            usuarioRepository.save(proveedor1);
            log.info("Proveedor Casa Verde Hotel creado/actualizado con ID de Keycloak: {}", keycloakId1);
        }

        // Proveedor 2 - Restaurante Típico
        String emailP2 = "info@saborcaribe.com";
        String keycloakId2 = null;
        
        if (keycloakService.userExistsInKeycloak(emailP2)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", emailP2);
            keycloakId2 = keycloakService.getUserKeycloakId(emailP2);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId2, TipoUsuario.PROVEEDOR);
        } else {
            keycloakId2 = keycloakService.createKeycloakUserWithRole(emailP2, "Sabor Caribe", "Restaurante", "password123", TipoUsuario.PROVEEDOR);
        }
        
        if (keycloakId2 != null) {
            Proveedor proveedor2 = new Proveedor();
            proveedor2.setId(keycloakId2);
            proveedor2.setNombre("Restaurante Sabor Caribe");
            proveedor2.setEdad(8); // Años sirviendo comida típica
            proveedor2.setFotoUrl("https://randomuser.me/api/portraits/men/5.jpg");
            proveedor2.setDescripcion("Auténtica cocina caribeña colombiana. Especialistas en pescados frescos, patacones y ceviche costeño");
            proveedor2.setCorreo("info@saborcaribe.com");
            proveedor2.setDireccion("Calle del Arsenal #8-19, Centro Histórico, Cartagena, Colombia"); // CAMPO REQUERIDO
            proveedor2.setTelefono("+57 5 312 7890");
            proveedor2.setTipoUsuario(TipoUsuario.PROVEEDOR); // CAMPO OBLIGATORIO
            proveedor2.setPaginaWeb("https://saborcaribe.com");
            proveedor2.setRedesSociales(Arrays.asList("@saborcaribe_oficial", "saborcaribe_ctg"));
            proveedor2.setCalificacionPromedio(4.5f);
            usuarioRepository.save(proveedor2);
            log.info("Proveedor Sabor Caribe creado/actualizado con ID de Keycloak: {}", keycloakId2);
        }

        // Proveedor 3 - Tours Ecológicos
        String emailP3 = "contacto@ecoaventuras.com";
        String keycloakId3 = null;
        
        if (keycloakService.userExistsInKeycloak(emailP3)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", emailP3);
            keycloakId3 = keycloakService.getUserKeycloakId(emailP3);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId3, TipoUsuario.PROVEEDOR);
        } else {
            keycloakId3 = keycloakService.createKeycloakUserWithRole(emailP3, "Eco Aventuras", "Colombia", "password123", TipoUsuario.PROVEEDOR);
        }
        
        if (keycloakId3 != null) {
            Proveedor proveedor3 = new Proveedor();
            proveedor3.setId(keycloakId3);
            proveedor3.setNombre("Eco Aventuras Colombia");
            proveedor3.setEdad(15); // Años organizando tours
            proveedor3.setFotoUrl("https://randomuser.me/api/portraits/men/6.jpg");
            proveedor3.setDescripcion("Tours ecológicos y senderismo en parques nacionales. Especialistas en avistamiento de aves y fotografía de naturaleza");
            proveedor3.setCorreo("contacto@ecoaventuras.com");
            proveedor3.setDireccion("Carrera 11 #93-15, Zona Rosa, Bogotá, Colombia"); // CAMPO REQUERIDO
            proveedor3.setTelefono("+57 1 456 7890");
            proveedor3.setTipoUsuario(TipoUsuario.PROVEEDOR); // CAMPO OBLIGATORIO
            proveedor3.setPaginaWeb("https://ecoaventurascolombia.com");
            proveedor3.setRedesSociales(Arrays.asList("@ecoaventuras_co", "ecoaventuras_colombia"));
            proveedor3.setCalificacionPromedio(4.9f);
            usuarioRepository.save(proveedor3);
            log.info("Proveedor Eco Aventuras creado/actualizado con ID de Keycloak: {}", keycloakId3);
        }

        // Proveedor 4 - Transporte Turístico
        String emailP4 = "reservas@viajesfacil.com";
        String keycloakId4 = null;
        
        if (keycloakService.userExistsInKeycloak(emailP4)) {
            log.info("Usuario {} ya existe en Keycloak, obteniendo ID y verificando rol...", emailP4);
            keycloakId4 = keycloakService.getUserKeycloakId(emailP4);
            // Asegurarse de que tenga el rol correcto
            keycloakService.assignRoleToExistingUser(keycloakId4, TipoUsuario.PROVEEDOR);
        } else {
            keycloakId4 = keycloakService.createKeycloakUserWithRole(emailP4, "Viajes Fácil", "Transporte", "password123", TipoUsuario.PROVEEDOR);
        }
        
        if (keycloakId4 != null) {
            Proveedor proveedor4 = new Proveedor();
            proveedor4.setId(keycloakId4);
            proveedor4.setNombre("Viajes Fácil Transporte");
            proveedor4.setEdad(20); // Años en transporte turístico
            proveedor4.setFotoUrl("https://randomuser.me/api/portraits/women/7.jpg");
            proveedor4.setDescripcion("Transporte cómodo y seguro para turistas. Rutas a destinos turísticos con guías especializados y vehículos climatizados");
            proveedor4.setCorreo("reservas@viajesfacil.com");
            proveedor4.setDireccion("Avenida El Poblado #12-45, Medellín, Colombia"); // CAMPO REQUERIDO
            proveedor4.setTelefono("+57 4 234 5678");
            proveedor4.setTipoUsuario(TipoUsuario.PROVEEDOR); // CAMPO OBLIGATORIO
            proveedor4.setPaginaWeb("https://viajesfacil.com");
            proveedor4.setRedesSociales(Arrays.asList("@viajesfacil_co", "viajesfacil_colombia"));
            proveedor4.setCalificacionPromedio(4.6f);
            usuarioRepository.save(proveedor4);
            log.info("Proveedor Viajes Fácil creado/actualizado con ID de Keycloak: {}", keycloakId4);
        }

        log.info("Proceso de creación de proveedores completado");
    }

    private void printTestInformation() {
        log.info("\n" +
                "=============== INFORMACIÓN PARA PRUEBAS (KEYCLOAK CON ROLES) ===============\n" +
                "USUARIOS DE PRUEBA CREADOS EN KEYCLOAK Y LOCAL CON ROLES AUTOMÁTICOS:\n" +
                "   Consulta la base de datos PostgreSQL user_ms_db para ver los IDs reales generados por Keycloak\n" +
                "\n" +
                "CLIENTES (ROL: CLIENTE):\n" +
                "   • Laura Martínez - Email: laura.martinez@gmail.com - Password: password123\n" +
                "   • Andrés Silva - Email: andres.silva@hotmail.com - Password: password123\n" +
                "   • Carmen Rodríguez - Email: carmen.rodriguez@yahoo.com - Password: password123\n" +
                "\n" +
                "PROVEEDORES (ROL: PROVEEDOR):\n" +
                "   • Casa Verde Hotel - Email: reservas@casaverde.com - Password: password123\n" +
                "   • Sabor Caribe Rest - Email: info@saborcaribe.com - Password: password123\n" +
                "   • Eco Aventuras - Email: contacto@ecoaventuras.com - Password: password123\n" +
                "   • Viajes Fácil - Email: reservas@viajesfacil.com - Password: password123\n" +
                "\n" +
                "ENDPOINTS PARA PROBAR:\n" +
                "   GET    /users/tipos-usuario  - Ver tipos de usuario disponibles\n" +
                "   GET    /users/verificar-correo?correo=X - Verificar si correo existe\n" +
                "   POST   /users               - Registrar usuario CON ROL OBLIGATORIO\n" +
                "   GET    /users/{id}          - Obtener usuario por ID\n" +
                "   GET    /users               - Listar todos los usuarios\n" +
                "   PUT    /users/{id}          - Actualizar usuario (requiere JWT)\n" +
                "   DELETE /users/{id}          - Eliminar usuario (requiere JWT)\n" +
                "\n" +
                "ROLES EN KEYCLOAK:\n" +
                "   • CLIENTE - Para usuarios que consumen servicios\n" +
                "   • PROVEEDOR - Para usuarios que ofrecen servicios\n" +
                "   ⚠️  OBLIGATORIO: Todo usuario DEBE tener un rol asignado\n" +
                "\n" +
                "AUTENTICACIÓN CON KEYCLOAK:\n" +
                "   URL Token: http://localhost:8081/realms/proyect-ms-realm/protocol/openid-connect/token\n" +
                "   Realm: proyect-ms-realm\n" +
                "   Client ID: user-ms-client\n" +
                "   Client Secret: Aq3vK9EfTGwoWpK3j48RkN6Q0yCUZqGd\n" +
                "   Usuarios y contraseñas: Ver lista arriba\n" +
                "\n" +
                "BASE DE DATOS POSTGRESQL:\n" +
                "   Host: localhost:5432\n" +
                "   Database: user_ms_db\n" +
                "   Usuario: postgres\n" +
                "   Contraseña: password\n" +
                "\n" +
                "CONSULTA SQL PARA VER DATOS COMPLETOS:\n" +
                "   SELECT id, nombre, correo, tipo_usuario_enum, telefono, direccion FROM USUARIO;\n" +
                "========================================================");
    }
}