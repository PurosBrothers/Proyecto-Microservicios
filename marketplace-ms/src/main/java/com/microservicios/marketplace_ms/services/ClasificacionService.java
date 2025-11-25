package com.microservicios.marketplace_ms.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservicios.marketplace_ms.entities.Alimentacion;
import com.microservicios.marketplace_ms.entities.Alojamiento;
import com.microservicios.marketplace_ms.entities.Clasificacion;
import com.microservicios.marketplace_ms.entities.Maps;
import com.microservicios.marketplace_ms.entities.PaseosEcologicos;
import com.microservicios.marketplace_ms.entities.RequisitosEspeciales;
import com.microservicios.marketplace_ms.entities.Transporte;
import com.microservicios.marketplace_ms.exceptions.InvalidProviderException;
import com.microservicios.marketplace_ms.repositories.ClasificacionRepository;
import com.microservicios.marketplace_ms.repositories.RequisitosEspecialesRepository;
import com.microservicios.marketplace_ms.security.JwtSecurityContext;

@Service
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private RequisitosEspecialesRepository requisitosEspecialesRepository;

    @Autowired
    private JwtSecurityContext jwtSecurityContext;

    @Autowired
    private RestTemplate restTemplate;

    public Clasificacion createClasificacion(Clasificacion clasificacion) {
        System.out.println("Iniciando creación de clasificación de tipo: " + clasificacion.getTipo());

        // Establecer el usuarioId desde el JWT del usuario autenticado
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new InvalidProviderException("Usuario no autenticado");
        }

        // La validación de PROVEEDOR ya se hace a nivel de Spring Security
        clasificacion.setUsuarioId(currentUserId);
        System.out.println("Usuario autenticado: " + currentUserId);

        // Asegurar que los RequisitosEspeciales existan en la BD
        if (clasificacion.getRequisitosEspeciales() != null) {
            List<RequisitosEspeciales> updatedRequisitos = new ArrayList<>();
            for (RequisitosEspeciales req : clasificacion.getRequisitosEspeciales()) {
                final RequisitosEspeciales finalReq = req;
                if (finalReq.getId() == null) {
                    // Buscar por requisito
                    RequisitosEspeciales existing = requisitosEspecialesRepository.findAll().stream()
                        .filter(r -> r.getRequisito().equals(finalReq.getRequisito()))
                        .findFirst().orElse(null);
                    if (existing != null) {
                        updatedRequisitos.add(existing);
                    } else {
                        RequisitosEspeciales saved = requisitosEspecialesRepository.save(finalReq);
                        updatedRequisitos.add(saved);
                    }
                } else {
                    updatedRequisitos.add(finalReq);
                }
            }
            clasificacion.setRequisitosEspeciales(updatedRequisitos);
        }

        // Determinar el nombre del país: usar paisDestino si no es null, sino lugarInicio
        String countryName = clasificacion.getPaisDestino();
        if (countryName == null || countryName.isEmpty()) {
            countryName = clasificacion.getLugarInicio();
        }

        // Siempre hacer petición a la API de países si hay nombre de país
        if (countryName != null && !countryName.isEmpty()) {
            try {
                String url = "https://restcountries.com/v3.1/name/" + countryName;
                CountryResponse[] responses = restTemplate.getForObject(url, CountryResponse[].class);
                if (responses != null && responses.length > 0) {
                    CountryResponse country = responses[0];
                    clasificacion.setFlag(country.getFlag());
                    clasificacion.setPopulation(country.getPopulation());
                    clasificacion.setFifa(country.getFifa());
                    if (country.getGini() != null && !country.getGini().isEmpty()) {
                        // Tomar el último valor de gini
                        Double giniValue = country.getGini().values().iterator().next();
                        clasificacion.setGini(giniValue);
                    }
                    // Setear mapas desde el país por defecto
                    if (country.getMaps() != null) {
                        Maps maps = new Maps();
                        maps.setGoogleMaps(country.getMaps().get("googleMaps"));
                        maps.setOpenStreetMaps(country.getMaps().get("openStreetMaps"));
                        clasificacion.setMaps(maps);
                    }
                }
            } catch (Exception e) {
                // Log error but don't fail the creation
                System.err.println("Error fetching country data: " + e.getMessage());
            }
        }

        // Determinar la dirección para mapas según el tipo
        String address = null;
        if (clasificacion instanceof Alojamiento) {
            address = ((Alojamiento) clasificacion).getDireccion();
        } else if (clasificacion instanceof Transporte) {
            address = ((Transporte) clasificacion).getLugarDestino();
            System.out.println("Es Transporte, lugar destino: " + address);
        } else {
            System.out.println("Es otro tipo, usando datos de país");
        }

        // Si hay dirección específica, crear enlace de Google Maps para la dirección (sobrescribe los mapas del país)
        if (address != null && !address.isEmpty()) {
            try {
                String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
                String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;
                Maps maps = new Maps();
                maps.setGoogleMaps(googleMapsUrl);
                // openStreetMaps puede dejarse null o asignar algo similar
                clasificacion.setMaps(maps);
                System.out.println("Enlace de Google Maps creado: " + googleMapsUrl);
            } catch (Exception e) {
                // Log error but don't fail the creation
                System.err.println("Error creating maps link: " + e.getMessage());
            }
        }

        Clasificacion saved = clasificacionRepository.save(clasificacion);
        System.out.println("Clasificación creada exitosamente con ID: " + saved.getId());
        return saved;
    }

    public Optional<Clasificacion> getClasificacionById(Long id) {
        Optional<Clasificacion> opt = clasificacionRepository.findById(id);
        opt.ifPresent(this::populateCountryDataIfMissing);
        return opt;
    }

    public List<Clasificacion> getAllClasificaciones() {
        List<Clasificacion> list = clasificacionRepository.findAll();
        list.forEach(this::populateCountryDataIfMissing);
        return list;
    }

    private void populateCountryDataIfMissing(Clasificacion clasificacion) {
        if (clasificacion.getFlag() == null && (clasificacion.getPaisDestino() != null || clasificacion.getLugarInicio() != null)) {
            String countryName = clasificacion.getPaisDestino();
            if (countryName == null || countryName.isEmpty()) {
                countryName = clasificacion.getLugarInicio();
            }
            if (countryName != null && !countryName.isEmpty()) {
                try {
                    String url = "https://restcountries.com/v3.1/name/" + countryName;
                    CountryResponse[] responses = restTemplate.getForObject(url, CountryResponse[].class);
                    if (responses != null && responses.length > 0) {
                        CountryResponse country = responses[0];
                        clasificacion.setFlag(country.getFlag());
                        clasificacion.setPopulation(country.getPopulation());
                        clasificacion.setFifa(country.getFifa());
                        if (country.getGini() != null && !country.getGini().isEmpty()) {
                            Double giniValue = country.getGini().values().iterator().next();
                            clasificacion.setGini(giniValue);
                        }
                        if (country.getMaps() != null) {
                            Maps maps = new Maps();
                            maps.setGoogleMaps(country.getMaps().get("googleMaps"));
                            maps.setOpenStreetMaps(country.getMaps().get("openStreetMaps"));
                            clasificacion.setMaps(maps);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching country data: " + e.getMessage());
                }
            }
        }
    }

    public Clasificacion updateClasificacion(Long id, Clasificacion clasificacion) {
        Optional<Clasificacion> existingOpt = clasificacionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Clasificacion not found");
        }

        Clasificacion existing = existingOpt.get();
        String currentUserId = jwtSecurityContext.getCurrentUserId();

        // Verificar que el usuario actual es el propietario de la clasificación
        if (!existing.getUsuarioId().equals(currentUserId)) {
            throw new InvalidProviderException("Solo puedes actualizar tus propias clasificaciones");
        }

        // Mantener el usuarioId original (no permitir cambio de propietario)
        clasificacion.setId(id);
        clasificacion.setUsuarioId(existing.getUsuarioId());

        return clasificacionRepository.save(clasificacion);
    }

    public void deleteClasificacion(Long id) {
        Optional<Clasificacion> existingOpt = clasificacionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Clasificacion not found");
        }

        Clasificacion existing = existingOpt.get();
        String currentUserId = jwtSecurityContext.getCurrentUserId();

        // Verificar que el usuario actual es el propietario de la clasificación
        if (!existing.getUsuarioId().equals(currentUserId)) {
            throw new InvalidProviderException("Solo puedes eliminar tus propias clasificaciones");
        }

        clasificacionRepository.deleteById(id);
    }

    // Métodos específicos para Alojamiento
    public Optional<Alojamiento> getAlojamientoById(Long id) {
        Optional<Alojamiento> opt = clasificacionRepository.findById(id)
                .filter(c -> c instanceof Alojamiento)
                .map(c -> (Alojamiento) c);
        opt.ifPresent(this::populateCountryDataIfMissing);
        return opt;
    }

    public List<Alojamiento> getAllAlojamientos() {
        List<Alojamiento> list = clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alojamiento)
                .map(c -> (Alojamiento) c)
                .toList();
        list.forEach(this::populateCountryDataIfMissing);
        return list;
    }

    // Métodos específicos para Alimentacion
    public Optional<Alimentacion> getAlimentacionById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof Alimentacion)
                .map(c -> (Alimentacion) c);
    }

    public List<Alimentacion> getAllAlimentaciones() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alimentacion)
                .map(c -> (Alimentacion) c)
                .toList();
    }

    // Métodos específicos para Transporte
    public Optional<Transporte> getTransporteById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof Transporte)
                .map(c -> (Transporte) c);
    }

    public List<Transporte> getAllTransportes() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Transporte)
                .map(c -> (Transporte) c)
                .toList();
    }

    // Métodos específicos para PaseosEcologicos
    public Optional<PaseosEcologicos> getPaseosEcologicosById(Long id) {
        return clasificacionRepository.findById(id)
                .filter(c -> c instanceof PaseosEcologicos)
                .map(c -> (PaseosEcologicos) c);
    }

    public List<PaseosEcologicos> getAllPaseosEcologicos() {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof PaseosEcologicos)
                .map(c -> (PaseosEcologicos) c)
                .toList();
    }

    // Métodos para buscar por usuario
    public List<Clasificacion> getClasificacionesByUsuario(String usuarioId) {
        List<Clasificacion> list = clasificacionRepository.findAll().stream()
                .filter(c -> usuarioId.equals(c.getUsuarioId()))
                .toList();
        list.forEach(this::populateCountryDataIfMissing);
        return list;
    }

    public List<Alojamiento> getAlojamientosByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alojamiento && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Alojamiento) c)
                .toList();
    }

    public List<Alimentacion> getAlimentacionesByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Alimentacion && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Alimentacion) c)
                .toList();
    }

    public List<Transporte> getTransportesByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof Transporte && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (Transporte) c)
                .toList();
    }

    public List<PaseosEcologicos> getPaseosEcologicosByUsuario(String usuarioId) {
        return clasificacionRepository.findAll().stream()
                .filter(c -> c instanceof PaseosEcologicos && usuarioId.equals(c.getUsuarioId()))
                .map(c -> (PaseosEcologicos) c)
                .toList();
    }

    // Métodos para obtener clasificaciones del usuario actual (desde JWT)
    public List<Clasificacion> getMyClasificaciones() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getClasificacionesByUsuario(currentUserId);
    }

    public List<Alojamiento> getMyAlojamientos() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getAlojamientosByUsuario(currentUserId);
    }

    public List<Alimentacion> getMyAlimentaciones() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getAlimentacionesByUsuario(currentUserId);
    }

    public List<Transporte> getMyTransportes() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getTransportesByUsuario(currentUserId);
    }

    public List<PaseosEcologicos> getMyPaseosEcologicos() {
        String currentUserId = jwtSecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return getPaseosEcologicosByUsuario(currentUserId);
    }
}