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

public class ProfileFragment extends Fragment {

    private static final String ARG_USERNAME = "USERNAME";
    private static final String ARG_IS_ADMIN = "IS_ADMIN";

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String username, boolean isAdmin) {
        ProfileFragment fragment = new ProfileFragment();

        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putBoolean(ARG_IS_ADMIN, isAdmin);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textUsername = view.findViewById(R.id.text_profile_username);
        TextView textRole = view.findViewById(R.id.text_profile_role);
        Button btnAdminPanel = view.findViewById(R.id.btn_admin_panel);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        String username = "user";
        boolean isAdmin = false;

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "user");
            isAdmin = getArguments().getBoolean(ARG_IS_ADMIN, false);
        }

        textUsername.setText(username);

        if (isAdmin) {
            textRole.setText("Admin Account");
            btnAdminPanel.setVisibility(View.VISIBLE);
        } else {
            textRole.setText("Standard Account");
        }

        btnAdminPanel.setOnClickListener(v -> {
            startActivity(AdminActivity.makeIntent(requireContext()));
        });


        btnLogout.setOnClickListener(v -> {
            requireActivity().getSharedPreferences("session", requireActivity().MODE_PRIVATE).edit().clear().apply();

            startActivity(MainActivity.makeIntent(requireContext()));
            requireActivity().finish();
        });

        return view;
    }
}