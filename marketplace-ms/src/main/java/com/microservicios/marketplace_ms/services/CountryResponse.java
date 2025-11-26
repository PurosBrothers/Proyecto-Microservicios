package com.microservicios.marketplace_ms.services;

import java.util.Map;

public class CountryResponse {
    private String flag;
    private Map<String, String> maps;
    private long population;
    private Map<String, Double> gini;
    private String fifa;

    // Getters and Setters
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Map<String, String> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public Map<String, Double> getGini() {
        return gini;
    }

    public void setGini(Map<String, Double> gini) {
        this.gini = gini;
    }

    public String getFifa() {
        return fifa;
    }

    public void setFifa(String fifa) {
        this.fifa = fifa;
    }
}