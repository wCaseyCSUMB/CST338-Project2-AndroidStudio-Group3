package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherwatcher.api.RetrofitClient;
import com.example.weatherwatcher.model.WeatherRespondingClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String EXTRA_USERNAME = "USERNAME";

    public static Intent makeIntent(Context context, String username) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        EditText editCity = findViewById(R.id.edit_city);
        Button btnSearch = findViewById(R.id.btn_search);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView textError = findViewById(R.id.text_error);
        LinearLayout layoutResult = findViewById(R.id.layout_result);
        TextView textCityName = findViewById(R.id.text_city_name);
        TextView textTemperature = findViewById(R.id.text_temperature);
        TextView textDescription = findViewById(R.id.text_description);
        TextView textDetails = findViewById(R.id.text_details);

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
                                textDetails.setText("Feels like: " + Math.round(weather.main.feelsLike) + "°F\n" + "Humidity: " + weather.main.humidity + "%\n" + "Wind: " + weather.wind.speed + " mph\n" + "High: " + Math.round(weather.main.tempMax) + "°F  " + "Low: " + Math.round(weather.main.tempMin) + "°F");
                                layoutResult.setVisibility(View.VISIBLE);

                            }

                            else if (response.code() == 404) {
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
    }
}