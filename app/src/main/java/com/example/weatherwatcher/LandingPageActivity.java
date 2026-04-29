package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LandingPageActivity extends AppCompatActivity {
//WARNING THIS IS NOT DONE WARNING THIS IS NOT DONE
    public static Intent makeIntent(Context context, String username, boolean isAdmin) {
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("IS_ADMIN", isAdmin);
        return intent;
    }
//WARNING THIS IS NOT DONE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // WARNING THIS IS NOT DONE WARNING THIS IS NOT DONE
    }
}