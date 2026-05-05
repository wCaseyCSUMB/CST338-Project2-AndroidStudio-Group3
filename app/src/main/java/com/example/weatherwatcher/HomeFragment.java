package com.example.weatherwatcher;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherwatcher.api.RetrofitClient;
import com.example.weatherwatcher.model.WeatherRespondingClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String ARG_USERNAME = "USERNAME";
    private static final String ARG_IS_ADMIN = "IS_ADMIN";
    private FusedLocationProviderClient fusedLocationClient;

    public HomeFragment() { // still don't know why this works
    }

    public static HomeFragment newInstance(String username, boolean isAdmin) {
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putBoolean(ARG_IS_ADMIN, isAdmin);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textWelcome = view.findViewById(R.id.text_welcome);
        TextView textAdminBadge = view.findViewById(R.id.text_admin_badge);

        TextView textHomeCity = view.findViewById(R.id.text_home_city);
        TextView textHomeDescription = view.findViewById(R.id.text_home_description);
        TextView textHomeTemperature = view.findViewById(R.id.text_home_temperature);
        TextView textHomeFeelsLike = view.findViewById(R.id.text_home_feels_like);
        TextView textHomeHighLow = view.findViewById(R.id.text_home_high_low);
        TextView textHomeDetails = view.findViewById(R.id.text_home_details);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        String username = "user";
        boolean isAdmin = false;

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "user");
            isAdmin = getArguments().getBoolean(ARG_IS_ADMIN, false);
        }

        textWelcome.setText("Welcome, " + username + " \uD83D\uDC4B");

        if (isAdmin) { // show the admin badge if the user is an admin
            textAdminBadge.setVisibility(View.VISIBLE);
        }

        loadCurrentWeather(textHomeCity, textHomeDescription, textHomeTemperature, textHomeFeelsLike, textHomeHighLow, textHomeDetails);

        return view;
    }

    // automatically detects your city, and as a result loads the weather
    private void loadCurrentWeather(TextView textCity, TextView textDescription, TextView textTemperature, TextView textFeelsLike, TextView textHighLow, TextView textDetails) {

        // i put a safety net here in case the user for whatever reason just doesn't give location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            textCity.setText("Location permission required.");
            textDescription.setText("");
            textTemperature.setText("");
            textFeelsLike.setText("");
            textHighLow.setText("");
            textDetails.setText("");

            return;
        }

        // just some temporary text for now as it loads the weather
        textCity.setText("Loading...");
        textDescription.setText("");
        textTemperature.setText("");
        textFeelsLike.setText("");
        textHighLow.setText("");
        textDetails.setText("");

        // as the city loads, we ask android for the current PRECISE location (latitude and longitude), no cancellation
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {

            if (location == null) { // another safety net, what if the location cannot be grabbed?
                textCity.setText("Unable to get your location :(");
                textDescription.setText("");
                textTemperature.setText("");
                textFeelsLike.setText("");
                textHighLow.setText("");
                textDetails.setText("");

                return;
            }

            RetrofitClient.getInstance().getApiService().getWeatherByLocation(location.getLatitude(), location.getLongitude(),
                    RetrofitClient.getInstance().getApiKey(), "imperial").enqueue(new Callback<WeatherRespondingClass>() {
                @Override
                public void onResponse(Call<WeatherRespondingClass> call, Response<WeatherRespondingClass> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        WeatherRespondingClass weather = response.body();
                        String description = "N/A";

                        if (weather.weather != null && !weather.weather.isEmpty()) {
                            description = weather.weather.get(0).description;
                        }

                        if (description.length() > 0) {
                            description = description.substring(0, 1).toUpperCase() + description.substring(1);
                        }

                        textCity.setText(weather.cityName);
                        textDescription.setText(description);
                        textTemperature.setText(Math.round(weather.main.temp) + "°F");
                        textFeelsLike.setText("Feels like " + Math.round(weather.main.feelsLike) + "°F");

                        textHighLow.setText("High " + Math.round(weather.main.tempMax) + "°   •   Low " + Math.round(weather.main.tempMin) + "°");

                        String windText = "Wind: N/A";

                        if (weather.wind != null) {
                            windText = "Wind: " + weather.wind.speed + " mph";
                        }

                        textDetails.setText("Humidity: " + weather.main.humidity + "%\n" + windText);
                    }

                    else {
                        textCity.setText("Something went wrong.");
                        textDescription.setText("Please try again.");
                        textTemperature.setText("");
                        textFeelsLike.setText("");
                        textHighLow.setText("");
                        textDetails.setText("");
                    }
                }

                @Override
                public void onFailure(Call<WeatherRespondingClass> call, Throwable t) {
                    textCity.setText("Network error.");
                    textDescription.setText(t.getMessage());
                    textTemperature.setText("");
                    textFeelsLike.setText("");
                    textHighLow.setText("");
                    textDetails.setText("");
                }
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            View view = getView();

            TextView textHomeCity = view.findViewById(R.id.text_home_city);
            TextView textHomeDescription = view.findViewById(R.id.text_home_description);
            TextView textHomeTemperature = view.findViewById(R.id.text_home_temperature);
            TextView textHomeFeelsLike = view.findViewById(R.id.text_home_feels_like);
            TextView textHomeHighLow = view.findViewById(R.id.text_home_high_low);
            TextView textHomeDetails = view.findViewById(R.id.text_home_details);

            loadCurrentWeather(textHomeCity, textHomeDescription, textHomeTemperature, textHomeFeelsLike, textHomeHighLow, textHomeDetails);
        }
    }
}