package com.microservicios.marketplace_ms.dtos;

import java.time.LocalDateTime;

public class TransporteDTO extends ClasificacionDTO {
    
    private String lugarDestino;
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegada;
    private String tipoTransporte;
    private Integer duracionViaje;
    private String rutaGps;

    // Constructors
    public TransporteDTO() {
        super();
    }

    // Getters and Setters
    public String getLugarDestino() {
        return lugarDestino;
    }

    public void setLugarDestino(String lugarDestino) {
        this.lugarDestino = lugarDestino;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalDateTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public Integer getDuracionViaje() {
        return duracionViaje;
    }

    public void setDuracionViaje(Integer duracionViaje) {
        this.duracionViaje = duracionViaje;
    }

    public String getRutaGps() {
        return rutaGps;
    }

    public void setRutaGps(String rutaGps) {
        this.rutaGps = rutaGps;
    }
}