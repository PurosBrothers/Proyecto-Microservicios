package com.microservicios.marketplace_ms.init;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Clasificacion;
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
        // Fix existing classifications that have null tipo
        fixExistingClasificaciones();
    }

    private void createTestData() {
      
        // Create Alojamiento
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setLugarInicio("Bogota");
        alojamiento.setPaisDestino("Colombia");
        alojamiento.setPrecio(new BigDecimal("150.00"));
        alojamiento.setFechaDisponibilidadInicio(LocalDateTime.now());
        alojamiento.setFechaDisponibilidadFin(LocalDateTime.now().plusDays(7));
        alojamiento.setCapacidadMaxima(1);
        alojamiento.setFechaCheckin(LocalDateTime.now().plusDays(1));
        alojamiento.setFechaCheckout(LocalDateTime.now().plusDays(7));
        alojamiento.setTipoInmueble("Hotel");
        alojamiento.setNumeroBanos(2);
        alojamiento.setNumeroHabitaciones(2);
        alojamiento.setDireccion("Calle 93 #15-22, Zona Rosa, Bogotá");
        alojamiento.setLat(new BigDecimal("4.7110"));
        alojamiento.setLng(new BigDecimal("-74.0721"));
        
        // Datos de clima para Bogotá
        alojamiento.setTemperaturaActual(18.2);
        alojamiento.setViento(12.5);
        alojamiento.setCodigoClima(1); // Código para cielo claro
        alojamiento.setLluvia(0.0);
        alojamiento.setPrecipitacion(0.0);
        alojamiento.setProbabilidadPrecipitacion(10);
        
        // Datos de país para el alojamiento original
        alojamiento.setFlag("🇨🇴");
        alojamiento.setPopulation(53057212L);
        alojamiento.setGini(51.3);
        alojamiento.setFifa("COL");
        
        // Maps específicos usando dirección del alojamiento
        Maps mapsOriginal = new Maps();
        mapsOriginal.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Calle+93+%2315-22%2C+Zona+Rosa%2C+Bogot%C3%A1");
        mapsOriginal.setOpenStreetMaps(null);
        alojamiento.setMaps(mapsOriginal);
        
        alojamiento.setUsuarioId("proveedor-001"); // Usuario proveedor de ejemplo
        alojamiento.setTipo("Alojamiento"); // Asegurar que el tipo esté establecido

        System.out.println("Creando Alojamiento con tipo: " + alojamiento.getTipo());
        alojamiento = (Alojamiento) clasificacionRepository.save(alojamiento);
        System.out.println("Alojamiento creado con ID: " + alojamiento.getId() + ", tipo: " + alojamiento.getTipo());

        // Create Alimentacion
        Alimentacion alimentacion = new Alimentacion();
        alimentacion.setLugarInicio("Medellin");
        alimentacion.setPaisDestino("Colombia");
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
        alimentacion.setTipo("Alimentacion"); // Asegurar que el tipo esté establecido
        
        // Datos de país para Colombia
        alimentacion.setFlag("🇨🇴");
        alimentacion.setPopulation(53057212L);
        alimentacion.setGini(51.3);
        alimentacion.setFifa("COL");
        
        // Maps específicos usando ubicación del restaurante
        Maps mapsAlimentacion = new Maps();
        mapsAlimentacion.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Restaurante+El+Sabor+Medell%C3%ADn%2C+Antioquia%2C+Colombia");
        mapsAlimentacion.setOpenStreetMaps(null);
        alimentacion.setMaps(mapsAlimentacion);

        System.out.println("Creando Alimentacion con tipo: " + alimentacion.getTipo());
        alimentacion = (Alimentacion) clasificacionRepository.save(alimentacion);
        System.out.println("Alimentacion creada con ID: " + alimentacion.getId() + ", tipo: " + alimentacion.getTipo());

        // Create Transporte
        Transporte transporte = new Transporte();
        transporte.setLugarInicio("Cali");
        transporte.setPaisDestino("Colombia");
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
        transporte.setTipo("Transporte"); // Asegurar que el tipo esté establecido
        
        // Datos de país para Colombia
        transporte.setFlag("🇨🇴");
        transporte.setPopulation(53057212L);
        transporte.setGini(51.3);
        transporte.setFifa("COL");
        
        // Maps específicos usando lugarDestino del transporte
        Maps mapsTransporte = new Maps();
        mapsTransporte.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Terminal+de+Transporte+Bogot%C3%A1%2C+Cundinamarca%2C+Colombia");
        mapsTransporte.setOpenStreetMaps(null);
        transporte.setMaps(mapsTransporte);

        System.out.println("Creando Transporte con tipo: " + transporte.getTipo());
        transporte = (Transporte) clasificacionRepository.save(transporte);
        System.out.println("Transporte creado con ID: " + transporte.getId() + ", tipo: " + transporte.getTipo());

        // Create PaseosEcologicos
        PaseosEcologicos paseos = new PaseosEcologicos();
        paseos.setLugarInicio("Cartagena");
        paseos.setPaisDestino("Colombia");
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
        paseos.setTipo("PaseosEcologicos"); // Asegurar que el tipo esté establecido
        
        // Datos de país para Colombia
        paseos.setFlag("🇨🇴");
        paseos.setPopulation(53057212L);
        paseos.setGini(51.3);
        paseos.setFifa("COL");
        
        // Maps específicos usando puntoEncuentro del paseo ecológico
        Maps mapsPaseos = new Maps();
        mapsPaseos.setGoogleMaps("https://www.google.com/maps/search/?api=1&query=Central+Park+Cartagena%2C+Boluvar%2C+Cartagena+de+Indias%2C+Bol%C3%ADvar%2C+Colombia");
        mapsPaseos.setOpenStreetMaps(null);
        paseos.setMaps(mapsPaseos);

        System.out.println("Creando PaseosEcologicos con tipo: " + paseos.getTipo());
        paseos = (PaseosEcologicos) clasificacionRepository.save(paseos);
        System.out.println("PaseosEcologicos creado con ID: " + paseos.getId() + ", tipo: " + paseos.getTipo());











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
    
    private void fixExistingClasificaciones() {
        System.out.println("=== CORRIGIENDO CLASIFICACIONES EXISTENTES ===");
        
        // Buscar todas las clasificaciones
        List<Clasificacion> todasClasificaciones = clasificacionRepository.findAll();
        
        for (Clasificacion clasificacion : todasClasificaciones) {
            String tipoActual = clasificacion.getTipo();
            System.out.println("Clasificación ID: " + clasificacion.getId() + 
                             ", Tipo actual: '" + tipoActual + 
                             "', Lugar: " + clasificacion.getLugarInicio());
            
            boolean necesitaGuardar = false;
            
            // Si el tipo es nulo, "Desconocido" o vacío, intentar corregirlo
            if (tipoActual == null || tipoActual.equals("Desconocido") || tipoActual.trim().isEmpty()) {
                
                String nuevoTipo = null;
                
                // Determinar el tipo basado en la clase Java
                if (clasificacion instanceof Alojamiento) {
                    nuevoTipo = "Alojamiento";
                } else if (clasificacion instanceof Alimentacion) {
                    nuevoTipo = "Alimentacion";
                } else if (clasificacion instanceof Transporte) {
                    nuevoTipo = "Transporte";
                } else if (clasificacion instanceof PaseosEcologicos) {
                    nuevoTipo = "PaseosEcologicos";
                }
                
                if (nuevoTipo != null) {
                    clasificacion.setTipo(nuevoTipo);
                    System.out.println("  ✓ CORREGIDO tipo a: '" + nuevoTipo + "'");
                    necesitaGuardar = true;
                } else {
                    System.out.println("  ✗ No se pudo determinar el tipo");
                }
            } else {
                System.out.println("  ✓ Tipo correcto: '" + tipoActual + "'");
            }
            
            // Asegurar que tenga paisDestino si es Colombia
            if (clasificacion.getPaisDestino() == null || clasificacion.getPaisDestino().trim().isEmpty()) {
                clasificacion.setPaisDestino("Colombia");
                System.out.println("  ✓ Agregado paisDestino: 'Colombia'");
                necesitaGuardar = true;
            }
            
            // Asegurar que tenga datos de país si es Colombia
            if (clasificacion.getFlag() == null || clasificacion.getPopulation() == null || 
                clasificacion.getFifa() == null || clasificacion.getGini() == null) {
                
                clasificacion.setFlag("🇨🇴");
                clasificacion.setPopulation(53057212L);
                clasificacion.setGini(51.3);
                clasificacion.setFifa("COL");
                System.out.println("  ✓ Agregados datos de país de Colombia");
                necesitaGuardar = true;
            }
            
            // Asegurar que tenga Maps específico según el tipo
            if (clasificacion.getMaps() == null || clasificacion.getMaps().getGoogleMaps() == null) {
                Maps maps = new Maps();
                String googleMapsUrl = null;
                
                if (clasificacion instanceof Alojamiento) {
                    Alojamiento alo = (Alojamiento) clasificacion;
                    if (alo.getDireccion() != null) {
                        googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + 
                                       URLEncoder.encode(alo.getDireccion(), StandardCharsets.UTF_8);
                    }
                } else if (clasificacion instanceof Transporte) {
                    Transporte trans = (Transporte) clasificacion;
                    if (trans.getLugarDestino() != null) {
                        googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + 
                                       URLEncoder.encode("Terminal " + trans.getLugarDestino() + ", Colombia", StandardCharsets.UTF_8);
                    }
                } else if (clasificacion instanceof PaseosEcologicos) {
                    PaseosEcologicos paseos = (PaseosEcologicos) clasificacion;
                    if (paseos.getPuntoEncuentro() != null) {
                        googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + 
                                       URLEncoder.encode(paseos.getPuntoEncuentro() + ", Colombia", StandardCharsets.UTF_8);
                    }
                }
                
                if (googleMapsUrl != null) {
                    maps.setGoogleMaps(googleMapsUrl);
                    maps.setOpenStreetMaps(null);
                    clasificacion.setMaps(maps);
                    System.out.println("  ✓ Agregado Maps específico");
                    necesitaGuardar = true;
                }
            }
            
            if (necesitaGuardar) {
                clasificacionRepository.save(clasificacion);
            }
        }
        
        System.out.println("=== CORRECCIÓN DE CLASIFICACIONES COMPLETADA ===");
    }
}