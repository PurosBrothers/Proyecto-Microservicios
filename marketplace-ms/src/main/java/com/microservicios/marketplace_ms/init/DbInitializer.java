package com.microservicios.marketplace_ms.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Calificacion;
import com.microservicios.marketplace_ms.entities.Comentario;
import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.entities.ItemFoto;
import com.microservicios.marketplace_ms.entities.ItemLink;
import com.microservicios.marketplace_ms.entities.ItemTag;
import com.microservicios.marketplace_ms.entities.ItemVideo;
import com.microservicios.marketplace_ms.entities.Maps;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.PreguntaFrecuente;
import com.microservicios.marketplace_ms.entities.RequisitosEspeciales;
import com.microservicios.marketplace_ms.entities.RestriccionesDieteticas;
import com.microservicios.marketplace_ms.entities.ServiciosIncluidos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.repositories.CalificacionRepository;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;
import com.microservicios.marketplace_ms.repositories.ComentarioRepository;
import com.microservicios.marketplace_ms.repositories.ItemFotoRepository;
import com.microservicios.marketplace_ms.repositories.ItemLinkRepository;
import com.microservicios.marketplace_ms.repositories.ItemRepository;
import com.microservicios.marketplace_ms.repositories.ItemTagRepository;
import com.microservicios.marketplace_ms.repositories.ItemVideoRepository;
import com.microservicios.marketplace_ms.repositories.PreguntaFrecuenteRepository;
import com.microservicios.marketplace_ms.repositories.RequisitosEspecialesRepository;
import com.microservicios.marketplace_ms.repositories.RestriccionesDieteticasRepository;
import com.microservicios.marketplace_ms.repositories.ServiciosIncluidosRepository;

@Component
public class DbInitializer implements CommandLineRunner {

    private final ClasificacionRepository clasificacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final ItemFotoRepository itemFotoRepository;
    private final ItemLinkRepository itemLinkRepository;
    private final ItemRepository itemRepository;
    private final ItemTagRepository itemTagRepository;
    private final ItemVideoRepository itemVideoRepository;
    private final PreguntaFrecuenteRepository preguntaFrecuenteRepository;
    private final RequisitosEspecialesRepository requisitosEspecialesRepository;
    private final RestriccionesDieteticasRepository restriccionesDieteticasRepository;
    private final ServiciosIncluidosRepository serviciosIncluidosRepository;

    public DbInitializer(ClasificacionRepository clasificacionRepository,
            CalificacionRepository calificacionRepository,
            ComentarioRepository comentarioRepository,
            ItemFotoRepository itemFotoRepository,
            ItemLinkRepository itemLinkRepository,
            ItemRepository itemRepository,
            ItemTagRepository itemTagRepository,
            ItemVideoRepository itemVideoRepository,
            PreguntaFrecuenteRepository preguntaFrecuenteRepository,
            RequisitosEspecialesRepository requisitosEspecialesRepository,
            RestriccionesDieteticasRepository restriccionesDieteticasRepository,
            ServiciosIncluidosRepository serviciosIncluidosRepository) {
        this.clasificacionRepository = clasificacionRepository;
        this.calificacionRepository = calificacionRepository;
        this.comentarioRepository = comentarioRepository;
        this.itemFotoRepository = itemFotoRepository;
        this.itemLinkRepository = itemLinkRepository;
        this.itemRepository = itemRepository;
        this.itemTagRepository = itemTagRepository;
        this.itemVideoRepository = itemVideoRepository;
        this.preguntaFrecuenteRepository = preguntaFrecuenteRepository;
        this.requisitosEspecialesRepository = requisitosEspecialesRepository;
        this.restriccionesDieteticasRepository = restriccionesDieteticasRepository;
        this.serviciosIncluidosRepository = serviciosIncluidosRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create test data
        createTestData();
    }

    private void createTestData() {
        /*
         * UIDs de ejemplo para pruebas (simulando UUIDs de Keycloak):
         * - "23ae7f4b-e507-4e9c-8ed6-dec20bf9d04a" = dickgrayson@gmail.com (token JWT real de prueba)
         * - "b4c87f2e-9d15-4a3b-8f7e-1c2d3e4f5a6b" = usuario-ejemplo-2
         * - "c5d87f3f-ae25-5b4c-9f8e-2d3e4f5a6b7c" = usuario-ejemplo-3
         * 
         * Nota: Los UIDs ahora son String (UUID) en lugar de Long para compatibilidad con Keycloak
         */
        // Create Alojamiento
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setLugarInicio("Bogota");
        alojamiento.setPrecio(new BigDecimal("150.00"));
        alojamiento.setFechaDisponibilidadInicio(LocalDateTime.now());
        alojamiento.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(7));
        alojamiento.setCapacidadMaxima(1);
        alojamiento.setFechaCheckin(LocalDateTime.now().plusDays(1));
        alojamiento.setFechaCheckout(LocalDateTime.now().plusDays(7));
        alojamiento.setTipoInmueble("Hotel");
        alojamiento.setNumeroBanos(2);
        alojamiento.setNumeroHabitaciones(2);
        alojamiento.setLat(new BigDecimal("4.7110"));
        alojamiento.setLng(new BigDecimal("-74.0721"));
        alojamiento.setUsuarioId("proveedor-001"); // Usuario proveedor de ejemplo

        alojamiento = (Alojamiento) clasificacionRepository.save(alojamiento);

        // Create Alimentacion
        Alimentacion alimentacion = new Alimentacion();
        alimentacion.setLugarInicio("Medellin");
        alimentacion.setPrecio(new BigDecimal("25.00"));
        alimentacion.setFechaDisponibilidadInicio(LocalDateTime.now());
        alimentacion.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(1));
        alimentacion.setCapacidadMaxima(50);
        alimentacion.setHoraInicio(LocalTime.of(12, 0));
        alimentacion.setHoraFinal(LocalTime.of(15, 0));
        alimentacion.setTipoComida("Lunch");
        alimentacion.setMenuIncluido("Pasta, Salad, Dessert");
        alimentacion.setLatitud(new BigDecimal("6.2442"));
        alimentacion.setLongitud(new BigDecimal("-75.5812"));
        alimentacion.setUsuarioId("proveedor-002"); // Usuario proveedor de ejemplo

        alimentacion = (Alimentacion) clasificacionRepository.save(alimentacion);

        // Create Transporte
        Transporte transporte = new Transporte();
        transporte.setLugarInicio("Cali");
        transporte.setPrecio(new BigDecimal("50.00"));
        transporte.setFechaDisponibilidadInicio(LocalDateTime.now());
        transporte.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(1));
        transporte.setCapacidadMaxima(20);
        transporte.setLugarDestino("Bogota");
        transporte.setHoraSalida(LocalDateTime.now().plusHours(2));
        transporte.setHoraLlegada(LocalDateTime.now().plusHours(4));
        transporte.setTipoTransporte("Bus");
        transporte.setDuracionViaje(120);
        transporte.setRutaGps("Route 1");
        transporte.setUsuarioId("proveedor-003"); // Usuario proveedor de ejemplo

        transporte = (Transporte) clasificacionRepository.save(transporte);

        // Create PaseosEcologicos
        PaseosEcologicos paseos = new PaseosEcologicos();
        paseos.setLugarInicio("Cartagena");
        paseos.setPrecio(new BigDecimal("75.00"));
        paseos.setFechaDisponibilidadInicio(LocalDateTime.now());
        paseos.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(3));
        paseos.setCapacidadMaxima(15);
        paseos.setDuracionHoras(4);
        paseos.setNivelDificultad("Medium");
        paseos.setEquipoIncluido(true);
        paseos.setGuiaIncluido(true);
        paseos.setEdadMinima(12);
        paseos.setPuntoEncuentro("Central Park");
        paseos.setRutaEncuentro("Street 123");
        paseos.setUsuarioId("proveedor-004"); // Usuario proveedor de ejemplo

        paseos = (PaseosEcologicos) clasificacionRepository.save(paseos);

        // Create Alojamiento con ciudadOrigen: Colombia y datos específicos
        Alojamiento alojamientoColombia = new Alojamiento();
        alojamientoColombia.setLugarInicio("Colombia");
        alojamientoColombia.setPaisDestino("Colombia");
        alojamientoColombia.setPrecio(new BigDecimal("200.00"));
        alojamientoColombia.setFechaDisponibilidadInicio(LocalDateTime.now());
        alojamientoColombia.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(30));
        alojamientoColombia.setCapacidadMaxima(4);
        alojamientoColombia.setFechaCheckin(LocalDateTime.of(2025, 12, 1, 14, 0)); // 2025-12-01T14:00:00
        alojamientoColombia.setFechaCheckout(LocalDateTime.of(2025, 12, 5, 11, 0)); // 2025-12-05T11:00:00
        alojamientoColombia.setTipoInmueble("Apartamento");
        alojamientoColombia.setNumeroBanos(2);
        alojamientoColombia.setNumeroHabitaciones(3);
        alojamientoColombia.setDireccion("Carrera 7 #23-45, Chapinero, Bogotá");
        alojamientoColombia.setLat(new BigDecimal("4.7110"));
        alojamientoColombia.setLng(new BigDecimal("-74.0721"));
        alojamientoColombia.setFlag("🇨🇴");
        alojamientoColombia.setPopulation(53057212L);
        alojamientoColombia.setGini(51.3);
        alojamientoColombia.setFifa("COL");
        
        // Maps específicos
        Maps mapsColombia = new Maps();
        mapsColombia.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Carrera+7+%2323-45%2C+Chapinero%2C+Bogot%C3%A1");
        mapsColombia.setOpenStreetMaps(null);
        alojamientoColombia.setMaps(mapsColombia);
        
        alojamientoColombia.setUsuarioId("proveedor-colombia");

        alojamientoColombia = (Alojamiento) clasificacionRepository.save(alojamientoColombia);

        // Create Alimentacion con ciudadOrigen: Colombia
        Alimentacion alimentacionColombia = new Alimentacion();
        alimentacionColombia.setLugarInicio("Colombia");
        alimentacionColombia.setPaisDestino("Colombia");
        alimentacionColombia.setPrecio(new BigDecimal("35.00"));
        alimentacionColombia.setFechaDisponibilidadInicio(LocalDateTime.now());
        alimentacionColombia.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(7));
        alimentacionColombia.setCapacidadMaxima(60);
        alimentacionColombia.setHoraInicio(LocalTime.of(11, 0));
        alimentacionColombia.setHoraFinal(LocalTime.of(16, 0));
        alimentacionColombia.setTipoComida("Almuerzo");
        alimentacionColombia.setMenuIncluido("Sancocho, Arroz, Pollo, Ensalada");
        alimentacionColombia.setLatitud(new BigDecimal("4.6097"));
        alimentacionColombia.setLongitud(new BigDecimal("-74.0817"));
        alimentacionColombia.setFlag("🇨🇴");
        alimentacionColombia.setPopulation(53057212L);
        alimentacionColombia.setGini(51.3);
        alimentacionColombia.setFifa("COL");
        
        Maps mapsAlimentacionColombia = new Maps();
        mapsAlimentacionColombia.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Carrera+7+%2323-45%2C+Chapinero%2C+Bogot%C3%A1");
        mapsAlimentacionColombia.setOpenStreetMaps(null);
        alimentacionColombia.setMaps(mapsAlimentacionColombia);
        
        alimentacionColombia.setUsuarioId("proveedor-colombia");

        alimentacionColombia = (Alimentacion) clasificacionRepository.save(alimentacionColombia);

        // Create Transporte con ciudadOrigen: Colombia  
        Transporte transporteColombia = new Transporte();
        transporteColombia.setLugarInicio("Colombia");
        transporteColombia.setPaisDestino("Colombia");
        transporteColombia.setPrecio(new BigDecimal("75.00"));
        transporteColombia.setFechaDisponibilidadInicio(LocalDateTime.now());
        transporteColombia.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(14));
        transporteColombia.setCapacidadMaxima(25);
        transporteColombia.setLugarDestino("Cartagena");
        transporteColombia.setHoraSalida(LocalDateTime.now().plusHours(24));
        transporteColombia.setHoraLlegada(LocalDateTime.now().plusHours(28));
        transporteColombia.setTipoTransporte("Bus");
        transporteColombia.setDuracionViaje(240); // 4 horas
        transporteColombia.setRutaGps("Ruta Bogotá-Cartagena");
        transporteColombia.setFlag("🇨🇴");
        transporteColombia.setPopulation(53057212L);
        transporteColombia.setGini(51.3);
        transporteColombia.setFifa("COL");
        
        Maps mapsTransporteColombia = new Maps();
        mapsTransporteColombia.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Carrera+7+%2323-45%2C+Chapinero%2C+Bogot%C3%A1");
        mapsTransporteColombia.setOpenStreetMaps(null);
        transporteColombia.setMaps(mapsTransporteColombia);
        
        transporteColombia.setUsuarioId("proveedor-colombia");

        transporteColombia = (Transporte) clasificacionRepository.save(transporteColombia);

        // Create PaseosEcologicos con ciudadOrigen: Colombia
        PaseosEcologicos paseosColombia = new PaseosEcologicos();
        paseosColombia.setLugarInicio("Colombia");
        paseosColombia.setPaisDestino("Colombia");
        paseosColombia.setPrecio(new BigDecimal("120.00"));
        paseosColombia.setFechaDisponibilidadInicio(LocalDateTime.now());
        paseosColombia.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(21));
        paseosColombia.setCapacidadMaxima(12);
        paseosColombia.setDuracionHoras(8);
        paseosColombia.setNivelDificultad("Fácil");
        paseosColombia.setEquipoIncluido(true);
        paseosColombia.setGuiaIncluido(true);
        paseosColombia.setEdadMinima(8);
        paseosColombia.setPuntoEncuentro("Parque Simón Bolívar");
        paseosColombia.setRutaEncuentro("Avenida 68 #45-12");
        paseosColombia.setFlag("🇨🇴");
        paseosColombia.setPopulation(53057212L);
        paseosColombia.setGini(51.3);
        paseosColombia.setFifa("COL");
        
        Maps mapsPaseosColombia = new Maps();
        mapsPaseosColombia.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Carrera+7+%2323-45%2C+Chapinero%2C+Bogot%C3%A1");
        mapsPaseosColombia.setOpenStreetMaps(null);
        paseosColombia.setMaps(mapsPaseosColombia);
        
        paseosColombia.setUsuarioId("proveedor-colombia");

        paseosColombia = (PaseosEcologicos) clasificacionRepository.save(paseosColombia);

        // Now create Items para las clasificaciones colombianas

        // Create Item con Alojamiento Colombia
        Item itemAlojamientoColombia = new Item();
        itemAlojamientoColombia.setTitulo("Apartamento en Bogotá, Colombia");
        itemAlojamientoColombia.setDescripcion("Apartamento moderno en Chapinero, Bogotá. Datos de país completos: Colombia 🇨🇴");
        itemAlojamientoColombia.setFechaPublicacion(LocalDate.now());
        itemAlojamientoColombia.setStock(null); // Alojamiento no usa stock
        itemAlojamientoColombia.setVisualizaciones(0);
        itemAlojamientoColombia.setCalificacionPromedio(0L);
        itemAlojamientoColombia.setLugarInicio(alojamientoColombia.getLugarInicio());
        itemAlojamientoColombia.setPrecio(alojamientoColombia.getPrecio());
        itemAlojamientoColombia.setFechaDisponibilidadInicio(alojamientoColombia.getFechaDisponibilidadInicio());
        itemAlojamientoColombia.setFechaDisponibilidadFin(alojamientoColombia.getFechaDisponibilidadFin());
        itemAlojamientoColombia.setCapacidadMaxima(alojamientoColombia.getCapacidadMaxima());
        itemAlojamientoColombia.setClasificacion(alojamientoColombia);

        itemAlojamientoColombia = itemRepository.save(itemAlojamientoColombia);

        // Create Item con Alimentacion Colombia
        Item itemAlimentacionColombia = new Item();
        itemAlimentacionColombia.setTitulo("Almuerzo típico colombiano");
        itemAlimentacionColombia.setDescripcion("Sancocho y platos típicos colombianos en el corazón de Bogotá");
        itemAlimentacionColombia.setFechaPublicacion(LocalDate.now());
        itemAlimentacionColombia.setStock(alimentacionColombia.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemAlimentacionColombia.setVisualizaciones(0);
        itemAlimentacionColombia.setCalificacionPromedio(0L);
        itemAlimentacionColombia.setLugarInicio(alimentacionColombia.getLugarInicio());
        itemAlimentacionColombia.setPrecio(alimentacionColombia.getPrecio());
        itemAlimentacionColombia.setFechaDisponibilidadInicio(alimentacionColombia.getFechaDisponibilidadInicio());
        itemAlimentacionColombia.setFechaDisponibilidadFin(alimentacionColombia.getFechaDisponibilidadFin());
        itemAlimentacionColombia.setCapacidadMaxima(alimentacionColombia.getCapacidadMaxima());
        itemAlimentacionColombia.setClasificacion(alimentacionColombia);

        itemAlimentacionColombia = itemRepository.save(itemAlimentacionColombia);

        // Create Item con Transporte Colombia
        Item itemTransporteColombia = new Item();
        itemTransporteColombia.setTitulo("Bus Bogotá-Cartagena");
        itemTransporteColombia.setDescripcion("Transporte cómodo de Bogotá a Cartagena con datos completos de Colombia");
        itemTransporteColombia.setFechaPublicacion(LocalDate.now());
        itemTransporteColombia.setStock(transporteColombia.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemTransporteColombia.setVisualizaciones(0);
        itemTransporteColombia.setCalificacionPromedio(0L);
        itemTransporteColombia.setLugarInicio(transporteColombia.getLugarInicio());
        itemTransporteColombia.setPrecio(transporteColombia.getPrecio());
        itemTransporteColombia.setFechaDisponibilidadInicio(transporteColombia.getFechaDisponibilidadInicio());
        itemTransporteColombia.setFechaDisponibilidadFin(transporteColombia.getFechaDisponibilidadFin());
        itemTransporteColombia.setCapacidadMaxima(transporteColombia.getCapacidadMaxima());
        itemTransporteColombia.setClasificacion(transporteColombia);

        itemTransporteColombia = itemRepository.save(itemTransporteColombia);

        // Create Item con PaseosEcologicos Colombia
        Item itemPaseosColombia = new Item();
        itemPaseosColombia.setTitulo("Ecotour por Bogotá y alrededores");
        itemPaseosColombia.setDescripcion("Tour ecológico con datos completos del país: Colombia 🇨🇴");
        itemPaseosColombia.setFechaPublicacion(LocalDate.now());
        itemPaseosColombia.setStock(paseosColombia.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemPaseosColombia.setVisualizaciones(0);
        itemPaseosColombia.setCalificacionPromedio(0L);
        itemPaseosColombia.setLugarInicio(paseosColombia.getLugarInicio());
        itemPaseosColombia.setPrecio(paseosColombia.getPrecio());
        itemPaseosColombia.setFechaDisponibilidadInicio(paseosColombia.getFechaDisponibilidadInicio());
        itemPaseosColombia.setFechaDisponibilidadFin(paseosColombia.getFechaDisponibilidadFin());
        itemPaseosColombia.setCapacidadMaxima(paseosColombia.getCapacidadMaxima());
        itemPaseosColombia.setClasificacion(paseosColombia);

        itemPaseosColombia = itemRepository.save(itemPaseosColombia);

        // Now create Items

        // Create Item with Alojamiento classification
        Item itemAlojamiento = new Item();
        itemAlojamiento.setTitulo("Hotel en Bogota");
        itemAlojamiento.setDescripcion("Un hotel cómodo en el centro de Bogota");
        itemAlojamiento.setFechaPublicacion(LocalDate.now());
        itemAlojamiento.setStock(null); // Alojamiento no usa stock
        itemAlojamiento.setVisualizaciones(0);
        itemAlojamiento.setCalificacionPromedio(0L);
        itemAlojamiento.setLugarInicio(alojamiento.getLugarInicio());
        itemAlojamiento.setPrecio(alojamiento.getPrecio());
        itemAlojamiento.setFechaDisponibilidadInicio(alojamiento.getFechaDisponibilidadInicio());
        itemAlojamiento.setFechaDisponibilidadFin(alojamiento.getFechaDisponibilidadFin());
        itemAlojamiento.setCapacidadMaxima(alojamiento.getCapacidadMaxima());
        itemAlojamiento.setClasificacion(alojamiento);

        itemAlojamiento = itemRepository.save(itemAlojamiento);

        // Create PreguntaFrecuente for the Item
        PreguntaFrecuente pregunta1 = new PreguntaFrecuente();
        pregunta1.setPregunta("¿El hotel incluye desayuno?");
        pregunta1.setItem(itemAlojamiento);
        preguntaFrecuenteRepository.save(pregunta1);

        PreguntaFrecuente pregunta2 = new PreguntaFrecuente();
        pregunta2.setPregunta("¿Hay estacionamiento disponible?");
        pregunta2.setItem(itemAlojamiento);
        preguntaFrecuenteRepository.save(pregunta2);

        // Create ItemFoto for the Item
        ItemFoto foto1 = new ItemFoto();
        foto1.setUrl("https://example.com/hotel1.jpg");
        foto1.setItem(itemAlojamiento);
        itemFotoRepository.save(foto1);

        ItemFoto foto2 = new ItemFoto();
        foto2.setUrl("https://example.com/hotel2.jpg");
        foto2.setItem(itemAlojamiento);
        itemFotoRepository.save(foto2);

        // Create ItemTag for the Item
        ItemTag tag1 = new ItemTag();
        tag1.setTag("Luxury");
        tag1.setItem(itemAlojamiento);
        itemTagRepository.save(tag1);

        ItemTag tag2 = new ItemTag();
        tag2.setTag("City Center");
        tag2.setItem(itemAlojamiento);
        itemTagRepository.save(tag2);

        // Create ItemVideo for the Item
        ItemVideo video1 = new ItemVideo();
        video1.setUrl("https://example.com/hotel-video1.mp4");
        video1.setItem(itemAlojamiento);
        itemVideoRepository.save(video1);

        ItemVideo video2 = new ItemVideo();
        video2.setUrl("https://example.com/hotel-video2.mp4");
        video2.setItem(itemAlojamiento);
        itemVideoRepository.save(video2);

        // Create ItemLink for the Item
        ItemLink link1 = new ItemLink();
        link1.setTag("https://example.com/hotel-booking");
        link1.setItem(itemAlojamiento);
        itemLinkRepository.save(link1);

        ItemLink link2 = new ItemLink();
        link2.setTag("https://example.com/hotel-reviews");
        link2.setItem(itemAlojamiento);
        itemLinkRepository.save(link2);

        // Create Item with Alimentacion classification
        Item itemAlimentacion = new Item();
        itemAlimentacion.setTitulo("Almuerzo en Medellín");
        itemAlimentacion.setDescripcion("Delicioso almuerzo en Medellín");
        itemAlimentacion.setFechaPublicacion(LocalDate.now());
        itemAlimentacion.setStock(alimentacion.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemAlimentacion.setVisualizaciones(0);
        itemAlimentacion.setCalificacionPromedio(0L);
        itemAlimentacion.setLugarInicio(alimentacion.getLugarInicio());
        itemAlimentacion.setPrecio(alimentacion.getPrecio());
        itemAlimentacion.setFechaDisponibilidadInicio(alimentacion.getFechaDisponibilidadInicio());
        itemAlimentacion.setFechaDisponibilidadFin(alimentacion.getFechaDisponibilidadFin());
        itemAlimentacion.setCapacidadMaxima(alimentacion.getCapacidadMaxima());
        itemAlimentacion.setClasificacion(alimentacion);

        itemAlimentacion = itemRepository.save(itemAlimentacion);

        // Create PreguntaFrecuente for Alimentacion
        PreguntaFrecuente preguntaAlimentacion1 = new PreguntaFrecuente();
        preguntaAlimentacion1.setPregunta("¿El menú incluye opciones vegetarianas?");
        preguntaAlimentacion1.setItem(itemAlimentacion);
        preguntaFrecuenteRepository.save(preguntaAlimentacion1);

        PreguntaFrecuente preguntaAlimentacion2 = new PreguntaFrecuente();
        preguntaAlimentacion2.setPregunta("¿Hay restricciones dietéticas disponibles?");
        preguntaAlimentacion2.setItem(itemAlimentacion);
        preguntaFrecuenteRepository.save(preguntaAlimentacion2);

        // Create Item with Transporte classification
        Item itemTransporte = new Item();
        itemTransporte.setTitulo("Bus Cali-Bogotá");
        itemTransporte.setDescripcion("Viaje cómodo en bus");
        itemTransporte.setFechaPublicacion(LocalDate.now());
        itemTransporte.setStock(transporte.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemTransporte.setVisualizaciones(0);
        itemTransporte.setCalificacionPromedio(0L);
        itemTransporte.setLugarInicio(transporte.getLugarInicio());
        itemTransporte.setPrecio(transporte.getPrecio());
        itemTransporte.setFechaDisponibilidadInicio(transporte.getFechaDisponibilidadInicio());
        itemTransporte.setFechaDisponibilidadFin(transporte.getFechaDisponibilidadFin());
        itemTransporte.setCapacidadMaxima(transporte.getCapacidadMaxima());
        itemTransporte.setClasificacion(transporte);

        itemTransporte = itemRepository.save(itemTransporte);

        // Create PreguntaFrecuente for Transporte
        PreguntaFrecuente preguntaTransporte1 = new PreguntaFrecuente();
        preguntaTransporte1.setPregunta("¿El precio incluye equipaje?");
        preguntaTransporte1.setItem(itemTransporte);
        preguntaFrecuenteRepository.save(preguntaTransporte1);

        PreguntaFrecuente preguntaTransporte2 = new PreguntaFrecuente();
        preguntaTransporte2.setPregunta("¿Hay paradas intermedias?");
        preguntaTransporte2.setItem(itemTransporte);
        preguntaFrecuenteRepository.save(preguntaTransporte2);

        // Create Item with PaseosEcologicos classification
        Item itemPaseos = new Item();
        itemPaseos.setTitulo("Tour Ecológico Cartagena");
        itemPaseos.setDescripcion("Tour guiado ecológico");
        itemPaseos.setFechaPublicacion(LocalDate.now());
        itemPaseos.setStock(paseos.getCapacidadMaxima()); // Stock = capacidadMaxima
        itemPaseos.setVisualizaciones(0);
        itemPaseos.setCalificacionPromedio(0L);
        itemPaseos.setLugarInicio(paseos.getLugarInicio());
        itemPaseos.setPrecio(paseos.getPrecio());
        itemPaseos.setFechaDisponibilidadInicio(paseos.getFechaDisponibilidadInicio());
        itemPaseos.setFechaDisponibilidadFin(paseos.getFechaDisponibilidadFin());
        itemPaseos.setCapacidadMaxima(paseos.getCapacidadMaxima());
        itemPaseos.setClasificacion(paseos);

        itemPaseos = itemRepository.save(itemPaseos);

        // Create PreguntaFrecuente for PaseosEcologicos
        PreguntaFrecuente preguntaPaseos1 = new PreguntaFrecuente();
        preguntaPaseos1.setPregunta("¿Qué equipo se incluye en el tour?");
        preguntaPaseos1.setItem(itemPaseos);
        preguntaFrecuenteRepository.save(preguntaPaseos1);

        PreguntaFrecuente preguntaPaseos2 = new PreguntaFrecuente();
        preguntaPaseos2.setPregunta("¿Hay límite de edad para participar?");
        preguntaPaseos2.setItem(itemPaseos);
        preguntaFrecuenteRepository.save(preguntaPaseos2);

        // Create Comentario PADRE con Calificación
        Comentario comentarioPadre = new Comentario();
        comentarioPadre.setUid("23ae7f4b-e507-4e9c-8ed6-dec20bf9d04a"); // UUID de ejemplo simulando Keycloak
        comentarioPadre.setTitulo("Excelente experiencia");
        comentarioPadre.setCuerpo("El alojamiento superó mis expectativas. Muy recomendado!");
        comentarioPadre.setLikes(10);
        comentarioPadre.setParent(null); // Es comentario padre
        comentarioPadre.setItem(itemAlojamiento); // Asociar al item

        comentarioPadre = comentarioRepository.save(comentarioPadre);

        // Create Calificacion para el comentario padre
        Calificacion calificacion = new Calificacion();
        calificacion.setUid("23ae7f4b-e507-4e9c-8ed6-dec20bf9d04a"); // Mismo UID del comentario
        calificacion.setPuntuacion(5);
        calificacion.setComentario(comentarioPadre);
        calificacion.setFechaCalificacion(LocalDateTime.now());
        calificacion.setItem(itemAlojamiento);

        calificacion = calificacionRepository.save(calificacion);

        // Actualizar relación bidireccional
        comentarioPadre.setCalificacion(calificacion);
        comentarioPadre = comentarioRepository.save(comentarioPadre);

        // Create respuesta al comentario (SIN calificación)
        Comentario respuesta1 = new Comentario();
        respuesta1.setUid("b4c87f2e-9d15-4a3b-8f7e-1c2d3e4f5a6b"); // UUID de otro usuario
        respuesta1.setTitulo("Totalmente de acuerdo");
        respuesta1.setCuerpo("Yo también tuve una experiencia increíble allí. El servicio es de primera.");
        respuesta1.setLikes(3);
        respuesta1.setParent(comentarioPadre); // Es respuesta
        respuesta1.setItem(null); // Las respuestas no se asocian al item
        respuesta1.setCalificacion(null); // Sin calificación

        comentarioRepository.save(respuesta1);

        // Create otra calificación padre para el mismo item
        Comentario comentarioPadre2 = new Comentario();
        comentarioPadre2.setUid("c5d87f3f-ae25-5b4c-9f8e-2d3e4f5a6b7c"); // UUID de tercer usuario
        comentarioPadre2.setTitulo("Buena relación calidad-precio");
        comentarioPadre2.setCuerpo("Aunque hay algunos detalles que mejorar, en general cumple con lo prometido.");
        comentarioPadre2.setLikes(5);
        comentarioPadre2.setParent(null);
        comentarioPadre2.setItem(itemAlojamiento);

        comentarioPadre2 = comentarioRepository.save(comentarioPadre2);

        // Calificación para el segundo comentario padre
        Calificacion calificacion2 = new Calificacion();
        calificacion2.setUid("c5d87f3f-ae25-5b4c-9f8e-2d3e4f5a6b7c"); // Mismo UID del comentario
        calificacion2.setPuntuacion(4);
        calificacion2.setComentario(comentarioPadre2);
        calificacion2.setFechaCalificacion(LocalDateTime.now());
        calificacion2.setItem(itemAlojamiento);

        calificacion2 = calificacionRepository.save(calificacion2);
        comentarioPadre2.setCalificacion(calificacion2);
        comentarioRepository.save(comentarioPadre2);

        // Create comentario con UID real de token JWT para pruebas
        // Este UID corresponde al token JWT de dickgrayson@gmail.com que aparece en los archivos de prueba
        Comentario comentarioPruebaJWT = new Comentario();
        comentarioPruebaJWT.setUid("23ae7f4b-e507-4e9c-8ed6-dec20bf9d04a"); // UID real del token JWT de prueba
        comentarioPruebaJWT.setTitulo("Comentario de prueba JWT");
        comentarioPruebaJWT.setCuerpo("Este comentario se puede editar/eliminar con el token JWT de dickgrayson@gmail.com");
        comentarioPruebaJWT.setLikes(0);
        comentarioPruebaJWT.setParent(null);
        comentarioPruebaJWT.setItem(itemAlimentacion); // Asociar a otro item para variedad

        comentarioPruebaJWT = comentarioRepository.save(comentarioPruebaJWT);

        // Calificación para el comentario de prueba JWT
        Calificacion calificacionPruebaJWT = new Calificacion();
        calificacionPruebaJWT.setUid("23ae7f4b-e507-4e9c-8ed6-dec20bf9d04a"); // Mismo UID
        calificacionPruebaJWT.setPuntuacion(4);
        calificacionPruebaJWT.setComentario(comentarioPruebaJWT);
        calificacionPruebaJWT.setFechaCalificacion(LocalDateTime.now());
        calificacionPruebaJWT.setItem(itemAlimentacion);

        calificacionPruebaJWT = calificacionRepository.save(calificacionPruebaJWT);
        comentarioPruebaJWT.setCalificacion(calificacionPruebaJWT);
        comentarioRepository.save(comentarioPruebaJWT);

        // Create ItemTag
        ItemTag tag = new ItemTag();
        tag.setTag("Luxury");
        tag.setItem(itemAlojamiento);

        itemTagRepository.save(tag);

        // Create RequisitosEspeciales
        RequisitosEspeciales req1 = new RequisitosEspeciales();
        req1.setRequisito("ID required");
        RequisitosEspeciales req2 = new RequisitosEspeciales();
        req2.setRequisito("Minimum age 18");

        requisitosEspecialesRepository.saveAll(Arrays.asList(req1, req2));

        alojamiento.setRequisitosEspeciales(Arrays.asList(req1, req2));
        clasificacionRepository.save(alojamiento);

        alimentacion.setRequisitosEspeciales(Arrays.asList(req1, req2));
        clasificacionRepository.save(alimentacion);

        // Create RestriccionesDieteticas
        RestriccionesDieteticas restriccion = new RestriccionesDieteticas();
        restriccion.setNombre("Vegetarian");
        restriccion.setAlimentacion(alimentacion);

        restriccionesDieteticasRepository.save(restriccion);

        // Create ServiciosIncluidos
        ServiciosIncluidos servicio1 = new ServiciosIncluidos();
        servicio1.setServicios("WiFi");
        servicio1.setItem(itemAlojamiento);

        ServiciosIncluidos servicio2 = new ServiciosIncluidos();
        servicio2.setServicios("Breakfast");
        servicio2.setItem(itemAlojamiento);

        serviciosIncluidosRepository.saveAll(Arrays.asList(servicio1, servicio2));
    }
}