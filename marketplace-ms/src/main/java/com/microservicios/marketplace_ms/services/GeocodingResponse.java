package com.microservicios.marketplace_ms.services;

import java.util.List;

public class GeocodingResponse {
    private List<GeocodingResult> results;

    public GeocodingResponse() {}

    public List<GeocodingResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodingResult> results) {
        this.results = results;
    }

    public static class GeocodingResult {
        private double latitude;
        private double longitude;

        public GeocodingResult() {}

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}