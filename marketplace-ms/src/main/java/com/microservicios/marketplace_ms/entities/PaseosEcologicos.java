package com.microservicios.marketplace_ms.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class PaseosEcologicos extends Item {

    private Integer duracionHoras;
    private String nivelDificultad;
    private Boolean equipoIncluido;
    private Boolean guiaIncluido;
    private Integer edadMinima;
    private String puntoEncuentro;
    private String rutaEncuentro;

    @ManyToMany
    private List<RequisitosEspeciales> requisitosEspeciales = new ArrayList<>();

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

    public List<RequisitosEspeciales> getRequisitosEspeciales() {
        return requisitosEspeciales;
    }

    public void setRequisitosEspeciales(List<RequisitosEspeciales> requisitosEspeciales) {
        this.requisitosEspeciales = requisitosEspeciales;
    }

}