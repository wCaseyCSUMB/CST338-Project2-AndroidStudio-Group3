package com.example.weatherwatcher.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, SavedLocation.class, SearchHistory.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();
    public abstract SavedLocationDAO savedLocationDao();
    public abstract SearchHistoryDAO searchHistoryDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "weatherapp_db").allowMainThreadQueries().fallbackToDestructiveMigration().addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            db.execSQL("INSERT INTO users (username, password, is_admin) " + "VALUES ('testuser1', 'password1', 0)");
                            db.execSQL("INSERT INTO users (username, password, is_admin) " + "VALUES ('admin2', 'adminpass', 1)");
                        }
                    }).build();
        }

        return instance;
    }
}
