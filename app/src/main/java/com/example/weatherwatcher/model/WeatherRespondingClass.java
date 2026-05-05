package com.example.weatherwatcher.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherRespondingClass {

    @SerializedName("name")
    public String cityName;

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("wind")
    public Wind wind;

    public static class Main {
        @SerializedName("temp")
        public double temp;

        @SerializedName("feels_like")
        public double feelsLike;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("temp_min")
        public double tempMin;

        @SerializedName("temp_max")
        public double tempMax;
    }

    public static class Weather {
        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;
    }

    public static class Wind {
        @SerializedName("speed")
        public double speed;
    }
}
