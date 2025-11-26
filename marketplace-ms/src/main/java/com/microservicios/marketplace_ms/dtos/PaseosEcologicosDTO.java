package com.microservicios.marketplace_ms.dtos;

public class PaseosEcologicosDTO extends ClasificacionDTO {
    
    private Integer duracionHoras;
    private String nivelDificultad;
    private Boolean equipoIncluido;
    private Boolean guiaIncluido;
    private Integer edadMinima;
    private String puntoEncuentro;
    private String rutaEncuentro;

    // Constructors
    public PaseosEcologicosDTO() {
        super();
    }

    // Getters and Setters
    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public Boolean getEquipoIncluido() {
        return equipoIncluido;
    }

    public void setEquipoIncluido(Boolean equipoIncluido) {
        this.equipoIncluido = equipoIncluido;
    }

    public Boolean getGuiaIncluido() {
        return guiaIncluido;
    }

    public void setGuiaIncluido(Boolean guiaIncluido) {
        this.guiaIncluido = guiaIncluido;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public String getPuntoEncuentro() {
        return puntoEncuentro;
    }

    public void setPuntoEncuentro(String puntoEncuentro) {
        this.puntoEncuentro = puntoEncuentro;
    }

    public String getRutaEncuentro() {
        return rutaEncuentro;
    }

    public void setRutaEncuentro(String rutaEncuentro) {
        this.rutaEncuentro = rutaEncuentro;
    }
}