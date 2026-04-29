package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherwatcher.database.AppDatabase;
import com.example.weatherwatcher.database.User;

public class LoginActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editUsername = findViewById(R.id.edit_username);
        EditText editPassword = findViewById(R.id.edit_password);
        TextView textError    = findViewById(R.id.text_error);

        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Query the database
            AppDatabase db = AppDatabase.getInstance(this);
            User user = db.userDAO().login(username, password);

            if (user != null) {
                // Save session to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
                prefs.edit()
                        .putString("username", user.username)
                        .putBoolean("is_admin", user.isAdmin)
                        .apply();

                // Navigate to landing page
                startActivity(LandingPageActivity.makeIntent(
                        this, user.username, user.isAdmin));
                finish();
            } else {
                textError.setVisibility(View.VISIBLE);
            }
        });
    }
}
