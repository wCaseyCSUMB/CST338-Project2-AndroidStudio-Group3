package com.example.weatherwatcher.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SearchHistoryDAO {

    @Insert
    void insert(SearchHistory search);

    @Query("SELECT * FROM search_history WHERE username = :username ORDER BY timestamp DESC")
    List<SearchHistory> getHistoryForUser(String username);

    @Query("DELETE FROM search_history WHERE username = :username")
    void clearHistoryForUser(String username);
}
