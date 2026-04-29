package com.example.weatherwatcher.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

    @Dao
    public interface UserDAO {

        @Insert
        void insert(User user);

        @Delete
        void delete(User user);

        @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
        User login(String username, String password);

        @Query("SELECT * FROM users")
        List<User> getAllUsers();

        @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
        User getUserByUsername(String username);

   }
