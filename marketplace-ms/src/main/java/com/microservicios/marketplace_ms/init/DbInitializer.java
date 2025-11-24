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

        // Create Comentario
        Comentario comentario = new Comentario();
        comentario.setUid(1L);
        comentario.setTitulo("Great experience");
        comentario.setCuerpo("Loved the place!");
        comentario.setLikes(10);

        comentario = comentarioRepository.save(comentario);

        // Create Calificacion
        Calificacion calificacion = new Calificacion();
        calificacion.setUid(1L);
        calificacion.setPuntuacion(5);
        calificacion.setComentario(comentario);
        calificacion.setFechaCalificacion(LocalDateTime.now());
        calificacion.setItem(itemAlojamiento);

        calificacionRepository.save(calificacion);

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