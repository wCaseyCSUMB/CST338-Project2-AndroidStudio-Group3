package com.example.weatherwatcher.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

    @Database(entities = {User.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {

        public abstract UserDAO userDAO();

        private static AppDatabase instance;

        public static AppDatabase getInstance(Context context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                "weatherapp_db")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                // Seed the two required users
                                UserDAO DAO = getInstance(context).userDAO();

                                User testUser = new User();
                                testUser.username = "testuser1";
                                testUser.password = "password1";
                                testUser.isAdmin = false;

                                User adminUser = new User();
                                adminUser.username = "admin2";
                                adminUser.password = "adminpass";
                                adminUser.isAdmin = true;

                                DAO.insert(testUser);
                                DAO.insert(adminUser);
                            }
                        })
                        .build();
            }
            return instance;
        }
    }
