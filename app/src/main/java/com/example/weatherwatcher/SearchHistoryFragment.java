package com.example.weatherwatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwatcher.database.AppDatabase;
import com.example.weatherwatcher.database.SearchHistory;

import java.util.List;

public class SearchHistoryFragment extends Fragment {

    private static final String ARG_USERNAME = "USERNAME";

    public static SearchHistoryFragment newInstance(String username) {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        String username = "";
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "");
        }

        final String currentUsername = username;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_search_history);
        TextView textEmpty        = view.findViewById(R.id.text_empty);
        Button btnClearHistory    = view.findViewById(R.id.btn_clear_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        AppDatabase db = AppDatabase.getInstance(requireContext());

        List<SearchHistory> history = db.searchHistoryDao()
                .getHistoryForUser(currentUsername);

        if (history.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnClearHistory.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnClearHistory.setVisibility(View.VISIBLE);

            final SearchHistoryAdapter[] adapter = {null};
            adapter[0] = new SearchHistoryAdapter(history);
            recyclerView.setAdapter(adapter[0]);

            // Clear all history button
            btnClearHistory.setOnClickListener(v -> {
                db.searchHistoryDao().clearHistoryForUser(currentUsername);
                adapter[0].setHistory(
                        db.searchHistoryDao().getHistoryForUser(currentUsername));
                textEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                btnClearHistory.setVisibility(View.GONE);
            });
        }

        return view;
    }
}
