package com.example.weatherwatcher;

import android.os.Bundle;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String savedUser = prefs.getString("username", null);
        boolean isAdmin = prefs.getBoolean("is_admin", false);

        if (savedUser != null) {
            startActivity(LandingPageActivity.makeIntent(this, savedUser, isAdmin));
            finish();

            return;
        }

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(v -> startActivity(LoginActivity.makeIntent(this)));
        findViewById(R.id.btn_create_account).setOnClickListener(v -> startActivity(CreateAccountActivity.makeIntent(this)));
    }
}