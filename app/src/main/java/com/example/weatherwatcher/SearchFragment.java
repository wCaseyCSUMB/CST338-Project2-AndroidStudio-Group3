package com.example.weatherwatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherwatcher.api.RetrofitClient;
import com.example.weatherwatcher.model.WeatherRespondingClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    public SearchFragment() { // idk how but including this but empty fixed it
        System.out.println("haha heyyy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_weather, container, false);

        EditText editCity = view.findViewById(R.id.edit_city);
        Button btnSearch = view.findViewById(R.id.btn_search);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView textError = view.findViewById(R.id.text_error);
        LinearLayout layoutResult = view.findViewById(R.id.layout_result);
        TextView textCityName = view.findViewById(R.id.text_city_name);
        TextView textTemperature = view.findViewById(R.id.text_temperature);
        TextView textDescription = view.findViewById(R.id.text_description);
        TextView textDetails = view.findViewById(R.id.text_details);

        btnSearch.setOnClickListener(v -> {
            String city = editCity.getText().toString().trim();

            if (city.isEmpty()) {
                textError.setText("Please enter a city name");
                textError.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            textError.setVisibility(View.GONE);
            layoutResult.setVisibility(View.GONE);
            btnSearch.setEnabled(false);

            RetrofitClient.getInstance().getApiService().getCurrentWeather(city, RetrofitClient.getInstance().getApiKey(), "imperial").enqueue(new Callback<WeatherRespondingClass>() {

                @Override
                        public void onResponse(Call<WeatherRespondingClass> call, Response<WeatherRespondingClass> response) {
                            progressBar.setVisibility(View.GONE);
                            btnSearch.setEnabled(true);

                            if (response.isSuccessful() && response.body() != null) {
                                WeatherRespondingClass weather = response.body();

                                textCityName.setText(weather.cityName);
                                textTemperature.setText(Math.round(weather.main.temp) + "°F");
                                textDescription.setText(weather.weather.get(0).description);

                                textDetails.setText(
                                        "Feels like: " + Math.round(weather.main.feelsLike) + "°F\n" +
                                                "Humidity: " + weather.main.humidity + "%\n" +
                                                "Wind: " + weather.wind.speed + " mph\n" +
                                                "High: " + Math.round(weather.main.tempMax) + "°F  " +
                                                "Low: " + Math.round(weather.main.tempMin) + "°F"
                                );

                                layoutResult.setVisibility(View.VISIBLE);

                            } else if (response.code() == 404) {
                                textError.setText("City not found. Please check the spelling.");
                                textError.setVisibility(View.VISIBLE);
                            }

                            else if (response.code() == 401) {
                                textError.setText("API key error. Please wait a few minutes and try again.");
                                textError.setVisibility(View.VISIBLE);
                            }

                            else {
                                textError.setText("Something went wrong. Please try again.");
                                textError.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherRespondingClass> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            btnSearch.setEnabled(true);
                            textError.setText("Network error: " + t.getMessage());
                            textError.setVisibility(View.VISIBLE);
                        }
                    });
        });

        return view;
    }
}