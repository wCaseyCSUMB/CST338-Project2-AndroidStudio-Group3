package com.example.weatherwatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwatcher.database.AppDatabase;
import com.example.weatherwatcher.database.SavedLocation;

import java.util.List;

public class SavedLocationsFragment extends Fragment {

    private static final String ARG_USERNAME = "USERNAME";

    public static SavedLocationsFragment newInstance(String username) {
        SavedLocationsFragment fragment = new SavedLocationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_locations, container, false);
        String username = "";

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "");
        }

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_locations);
        TextView textEmpty = view.findViewById(R.id.text_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        AppDatabase db = AppDatabase.getInstance(requireContext());
        List<SavedLocation> locations = db.savedLocationDao().getLocationsForUser(username);

        if (locations.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            final String currentUsername = username;
            final SavedLocationAdapter[] adapter = {null};

            adapter[0] = new SavedLocationAdapter(locations, location -> {
                db.savedLocationDao().delete(location);
                List<SavedLocation> updated = db.savedLocationDao().getLocationsForUser(currentUsername);
                adapter[0].setLocations(updated);

                if (updated.isEmpty()) {
                    textEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });

            recyclerView.setAdapter(adapter[0]);
        }

        return view;
    }
}
