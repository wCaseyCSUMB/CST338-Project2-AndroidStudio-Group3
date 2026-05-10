package com.example.weatherwatcher.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SavedLocationDAO {

    @Insert
    void insert(SavedLocation location);

    @Delete
    void delete(SavedLocation location);

    @Query("SELECT * FROM saved_locations WHERE username = :username ORDER BY timestamp DESC")
    List<SavedLocation> getLocationsForUser(String username);

    @Query("SELECT * FROM saved_locations WHERE username = :username AND city_name = :cityName LIMIT 1")
    SavedLocation getLocationByUsernameAndCity(String username, String cityName);
}
