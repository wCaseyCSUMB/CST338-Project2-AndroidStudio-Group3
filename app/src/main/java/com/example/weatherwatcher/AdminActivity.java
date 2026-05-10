package com.example.weatherwatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherwatcher.database.AppDatabase;
import com.example.weatherwatcher.database.User;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, AdminActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView recyclerUsers = findViewById(R.id.recycler_users);
        TextView textUserCount = findViewById(R.id.text_user_count);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(this);
        List<User> users = db.userDAO().getAllUsers();

        textUserCount.setText(users.size() + " users");

        final UserAdapter[] adapter = {null};
        adapter[0] = new UserAdapter(users, user -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete " + user.username + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.userDAO().delete(user);
                        List<User> updated = db.userDAO().getAllUsers();
                        adapter[0].setUsers(updated);
                        textUserCount.setText(updated.size() + " users");
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        recyclerUsers.setAdapter(adapter[0]);
    }
}