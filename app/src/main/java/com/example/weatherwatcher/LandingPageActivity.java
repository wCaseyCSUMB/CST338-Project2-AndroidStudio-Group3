package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LandingPageActivity extends AppCompatActivity {

    private static final String EXTRA_USERNAME = "USERNAME";
    private static final String EXTRA_IS_ADMIN = "IS_ADMIN";

    public static Intent makeIntent(Context context, String username, boolean isAdmin) {
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_IS_ADMIN, isAdmin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        boolean isAdmin = getIntent().getBooleanExtra(EXTRA_IS_ADMIN, false);

        TextView textWelcome    = findViewById(R.id.text_welcome);
        TextView textAdminBadge = findViewById(R.id.text_admin_badge);
        Button   btnAdminPanel  = findViewById(R.id.btn_admin_panel);
        Button   btnLogout      = findViewById(R.id.btn_logout);
        Button   btnWeather     = findViewById(R.id.btn_view_weather);
        Button   btnFavorites   = findViewById(R.id.btn_favorites);
        Button   btnHistory     = findViewById(R.id.btn_history);

        textWelcome.setText("Welcome, " + username + "!");

        if (isAdmin) {
            textAdminBadge.setVisibility(View.VISIBLE);
            btnAdminPanel.setVisibility(View.VISIBLE);
        }

        btnAdminPanel.setOnClickListener(v -> {
            // TODO: navigate to AdminActivity
        });

        btnWeather.setOnClickListener(v -> {
            // TODO: navigate to WeatherActivity
        });

        btnFavorites.setOnClickListener(v -> {
            // TODO: navigate to FavoritesActivity
        });

        btnHistory.setOnClickListener(v -> {
            // TODO: navigate to SearchHistoryActivity
        });

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("session", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            startActivity(MainActivity.makeIntent(this));
            finish();
        });
    }
}