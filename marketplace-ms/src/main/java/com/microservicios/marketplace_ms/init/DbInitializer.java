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
import com.microservicios.marketplace_ms.entities.RequisitosEspeciales;
import com.microservicios.marketplace_ms.entities.RestriccionesDieteticas;
import com.microservicios.marketplace_ms.entities.ServiciosIncluidos;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.repositories.AlojamientoRepository;
import com.microservicios.marketplace_ms.repositories.AlimentacionRepository;
import com.microservicios.marketplace_ms.repositories.CalificacionRepository;
import com.microservicios.marketplace_ms.repositories.ComentarioRepository;
import com.microservicios.marketplace_ms.repositories.ItemFotoRepository;
import com.microservicios.marketplace_ms.repositories.ItemLinkRepository;
import com.microservicios.marketplace_ms.repositories.ItemRepository;
import com.microservicios.marketplace_ms.repositories.ItemTagRepository;
import com.microservicios.marketplace_ms.repositories.ItemVideoRepository;
import com.microservicios.marketplace_ms.repositories.PaseosEcologicosRepository;
import com.microservicios.marketplace_ms.repositories.RequisitosEspecialesRepository;
import com.microservicios.marketplace_ms.repositories.RestriccionesDieteticasRepository;
import com.microservicios.marketplace_ms.repositories.ServiciosIncluidosRepository;
import com.microservicios.marketplace_ms.repositories.TransporteRepository;

@Component
public class DbInitializer implements CommandLineRunner {

    private final AlojamientoRepository alojamientoRepository;
    private final AlimentacionRepository alimentacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final ItemFotoRepository itemFotoRepository;
    private final ItemLinkRepository itemLinkRepository;
    private final ItemRepository itemRepository;
    private final ItemTagRepository itemTagRepository;
    private final ItemVideoRepository itemVideoRepository;
    private final PaseosEcologicosRepository paseosEcologicosRepository;
    private final RequisitosEspecialesRepository requisitosEspecialesRepository;
    private final RestriccionesDieteticasRepository restriccionesDieteticasRepository;
    private final ServiciosIncluidosRepository serviciosIncluidosRepository;
    private final TransporteRepository transporteRepository;

    public DbInitializer(AlojamientoRepository alojamientoRepository,
                          AlimentacionRepository alimentacionRepository,
                          CalificacionRepository calificacionRepository,
                          ComentarioRepository comentarioRepository,
                          ItemFotoRepository itemFotoRepository,
                          ItemLinkRepository itemLinkRepository,
                          ItemRepository itemRepository,
                          ItemTagRepository itemTagRepository,
                          ItemVideoRepository itemVideoRepository,
                          PaseosEcologicosRepository paseosEcologicosRepository,
                          RequisitosEspecialesRepository requisitosEspecialesRepository,
                          RestriccionesDieteticasRepository restriccionesDieteticasRepository,
                          ServiciosIncluidosRepository serviciosIncluidosRepository,
                          TransporteRepository transporteRepository) {
        this.alojamientoRepository = alojamientoRepository;
        this.alimentacionRepository = alimentacionRepository;
        this.calificacionRepository = calificacionRepository;
        this.comentarioRepository = comentarioRepository;
        this.itemFotoRepository = itemFotoRepository;
        this.itemLinkRepository = itemLinkRepository;
        this.itemRepository = itemRepository;
        this.itemTagRepository = itemTagRepository;
        this.itemVideoRepository = itemVideoRepository;
        this.paseosEcologicosRepository = paseosEcologicosRepository;
        this.requisitosEspecialesRepository = requisitosEspecialesRepository;
        this.restriccionesDieteticasRepository = restriccionesDieteticasRepository;
        this.serviciosIncluidosRepository = serviciosIncluidosRepository;
        this.transporteRepository = transporteRepository;
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
        alojamiento.setCapacidadMaxima(4);
        alojamiento.setFechaCheckin(LocalDateTime.now().plusDays(1));
        alojamiento.setFechaCheckout(LocalDateTime.now().plusDays(7));
        alojamiento.setTipoInmueble("Hotel");
        alojamiento.setNumeroBanos(2);
        alojamiento.setNumeroHabitaciones(2);
        alojamiento.setLat(new BigDecimal("4.7110"));
        alojamiento.setLng(new BigDecimal("-74.0721"));

        alojamiento = alojamientoRepository.save(alojamiento);

        // Create Item for Alojamiento
        Item itemAlojamiento = new Item();
        itemAlojamiento.setClasificacion(alojamiento);
        itemAlojamiento.setTitulo("Hotel en Bogota");
        itemAlojamiento.setDescripcion("Un hotel cómodo en el centro de Bogota");
        itemAlojamiento.setFechaPublicacion(LocalDate.now());
        itemAlojamiento.setStock(10);
        itemAlojamiento.setVisualizaciones(0);
        itemAlojamiento.setCalificacionPromedio(0L);
        itemAlojamiento = itemRepository.save(itemAlojamiento);

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

        alimentacion = alimentacionRepository.save(alimentacion);

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

        transporte = transporteRepository.save(transporte);

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

        paseos = paseosEcologicosRepository.save(paseos);

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
        alojamientoRepository.save(alojamiento);

        alimentacion.setRequisitosEspeciales(Arrays.asList(req1, req2));
        alimentacionRepository.save(alimentacion);

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