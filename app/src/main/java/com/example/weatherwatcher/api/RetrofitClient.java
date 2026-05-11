package com.example.weatherwatcher.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "b766a2838fed6cf59324152658f23400";
    private static RetrofitClient instance;
    private final WeatherApi apiService;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        apiService = retrofit.create(WeatherApi.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }

        return instance;
    }

    public WeatherApi getApiService() {
        return apiService;
    }

    public String getApiKey() {
        return API_KEY;
    }
}
