package com.example.weatherwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwatcher.database.SearchHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<SearchHistory> history;

    public SearchHistoryAdapter(List<SearchHistory> history) {
        this.history = history;
    }

    public void setHistory(List<SearchHistory> history) {
        this.history = history;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchHistory item = history.get(position);
        holder.textCityName.setText(item.cityName);
        holder.textDetails.setText(item.temperature + " • " + item.description);

        String date = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                .format(new Date(item.timestamp));
        holder.textTimestamp.setText(date);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCityName;
        TextView textDetails;
        TextView textTimestamp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCityName  = itemView.findViewById(R.id.text_city_name);
            textDetails   = itemView.findViewById(R.id.text_details);
            textTimestamp = itemView.findViewById(R.id.text_timestamp);
        }
    }
}
