package com.microservicios.marketplace_ms.dtos;

public class MapsDTO {

    private String googleMaps;
    private String openStreetMaps;

    public MapsDTO() {
    }

    public MapsDTO(String googleMaps, String openStreetMaps) {
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