package com.microservicios.marketplace_ms.services;

import java.util.List;

public class ForecastResponse {
    private CurrentWeather current_weather;
    private Hourly hourly;

    public ForecastResponse() {}

    public CurrentWeather getCurrent_weather() {
        return current_weather;
    }

    public void setCurrent_weather(CurrentWeather current_weather) {
        this.current_weather = current_weather;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private int weathercode;

        public CurrentWeather() {}

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getWindspeed() {
            return windspeed;
        }

        public void setWindspeed(double windspeed) {
            this.windspeed = windspeed;
        }

        public int getWeathercode() {
            return weathercode;
        }

        public void setWeathercode(int weathercode) {
            this.weathercode = weathercode;
        }
    }

    public static class Hourly {
        private List<Double> rain;
        private List<Double> precipitation;
        private List<Integer> precipitation_probability;

        public Hourly() {}

        public List<Double> getRain() {
            return rain;
        }

        public void setRain(List<Double> rain) {
            this.rain = rain;
        }

        public List<Double> getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(List<Double> precipitation) {
            this.precipitation = precipitation;
        }

        public List<Integer> getPrecipitation_probability() {
            return precipitation_probability;
        }

        public void setPrecipitation_probability(List<Integer> precipitation_probability) {
            this.precipitation_probability = precipitation_probability;
        }
    }
}