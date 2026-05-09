package com.example.weatherwatcher.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_history")
public class SearchHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "city_name")
    public String cityName;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "timestamp")
    public long timestamp;
}
