package com.example.weatherwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwatcher.database.SavedLocation;

import java.util.List;

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(SavedLocation location);
    }

    private List<SavedLocation> locations;
    private final OnDeleteClickListener listener;

    public SavedLocationAdapter(List<SavedLocation> locations, OnDeleteClickListener listener) {
        this.locations = locations;
        this.listener  = listener;
    }

    public void setLocations(List<SavedLocation> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedLocation location = locations.get(position);
        holder.textCityName.setText(location.cityName);
        holder.textDetails.setText(location.temperature + " • " + location.description);
        holder.btnRemove.setOnClickListener(v -> listener.onDeleteClick(location));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCityName;
        TextView textDetails;
        Button btnRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCityName = itemView.findViewById(R.id.text_city_name);
            textDetails  = itemView.findViewById(R.id.text_details);
            btnRemove    = itemView.findViewById(R.id.btn_remove);
        }
    }
}
