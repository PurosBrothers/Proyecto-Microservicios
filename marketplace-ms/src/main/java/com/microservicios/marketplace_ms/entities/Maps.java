package com.microservicios.marketplace_ms.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Maps {

    private String googleMaps;
    private String openStreetMaps;

    public Maps() {
    }

    public Maps(String googleMaps, String openStreetMaps) {
        this.googleMaps = googleMaps;
        this.openStreetMaps = openStreetMaps;
    }

    public String getGoogleMaps() {
        return googleMaps;
    }

    public void setGoogleMaps(String googleMaps) {
        this.googleMaps = googleMaps;
    }

    public String getOpenStreetMaps() {
        return openStreetMaps;
    }

    public void setOpenStreetMaps(String openStreetMaps) {
        this.openStreetMaps = openStreetMaps;
    }
}