package com.example.weatherwatcher;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

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

    public HomeFragment() {
        // Required empty public constructor
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
        Button btnAdminPanel = view.findViewById(R.id.btn_admin_panel);
        TextView textCurrentWeather = view.findViewById(R.id.text_current_weather);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        String username = "user";
        boolean isAdmin = false;

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "user");
            isAdmin = getArguments().getBoolean(ARG_IS_ADMIN, false);
        }

        textWelcome.setText("Welcome, " + username + "!");

        if (isAdmin) {
            textAdminBadge.setVisibility(View.VISIBLE);
            btnAdminPanel.setVisibility(View.VISIBLE);
        }

        btnAdminPanel.setOnClickListener(v -> {
            // TODO: navigate to AdminActivity
        });

        loadCurrentWeather(textCurrentWeather);

        btnLogout.setOnClickListener(v -> {requireActivity().getSharedPreferences("session", requireActivity().MODE_PRIVATE).edit().clear().apply();
            startActivity(MainActivity.makeIntent(requireContext()));
            requireActivity().finish();
        });

        return view;
    }

    private void loadCurrentWeather(TextView textView) { // automatically detects your city, and as a result loads the weather

        // i put a safety net here in case the user for whatever reason just doesn't give location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            textView.setText("Hi, location permission is required to show your weather.");

            return;
        }

        textView.setText("Loading your city's weather..."); // just some temporary text for now as it loads the weather

        // as the city loads, we ask android for the current PRECISE location (latitude and longitude), no cancellation
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {
            if (location == null) { // another safety net, what if the location cannot be grabbed?
                textView.setText("Uh oh, seems like we can't get your location :(");
                return;
            }

            RetrofitClient.getInstance().getApiService().getWeatherByLocation(location.getLatitude(), location.getLongitude(),
                    RetrofitClient.getInstance().getApiKey(), "imperial").enqueue(new Callback<WeatherRespondingClass>() {
                @Override
                public void onResponse(Call<WeatherRespondingClass> call, Response<WeatherRespondingClass> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        WeatherRespondingClass weather = response.body();

                        textView.setText(weather.cityName + "\n" + Math.round(weather.main.temp) + "°F\n" + weather.weather.get(0).description);
                    }

                    else {
                        textView.setText("Something went wrong. Please try again.");
                    }
                }

                @Override
                public void onFailure(Call<WeatherRespondingClass> call, Throwable t) {
                    textView.setText("Network error: " + t.getMessage());
                    textView.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            View view = getView();

            if (view != null) {
                TextView textCurrentWeather = view.findViewById(R.id.text_current_weather);
                loadCurrentWeather(textCurrentWeather);
            }
        }
    }
}