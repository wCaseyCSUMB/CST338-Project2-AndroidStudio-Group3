package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(username, isAdmin)).commit();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(username, isAdmin)).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SearchFragment.newInstance(username)).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ProfileFragment.newInstance(username, isAdmin)).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_saved) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                SavedLocationsFragment.newInstance(username))
                        .commit();
                return true;
            }

            return false;
        });
    }
}