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

public class CreateAccountActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText editUsername = findViewById(R.id.edit_username);
        EditText editPassword = findViewById(R.id.edit_password);
        TextView textError    = findViewById(R.id.text_error);

        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                textError.setText("Username and password cannot be empty");
                textError.setVisibility(View.VISIBLE);
                return;
            }

            // Check if username already exists
            AppDatabase db = AppDatabase.getInstance(this);
            User existing = db.userDAO().getUserByUsername(username);
            if (existing != null) {
                textError.setText("That username is already in use, please input a different one");
                textError.setVisibility(View.VISIBLE);
                return;
            }

            // Create and insert new user
            User newUser = new User();
            newUser.username = username;
            newUser.password = password;
            newUser.isAdmin  = false;
            db.userDAO().insert(newUser);

            // Auto-login after account creation
            SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
            prefs.edit()
                    .putString("username", username)
                    .putBoolean("is_admin", false)
                    .apply();

            startActivity(LandingPageActivity.makeIntent(this, username, false));
            finish();
        });
    }
}
