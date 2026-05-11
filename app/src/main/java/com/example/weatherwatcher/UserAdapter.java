package com.example.weatherwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherwatcher.database.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(User user);
    }

    private List<User> users;
    private final OnDeleteClickListener listener;

    public UserAdapter(List<User> users, OnDeleteClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.textUsername.setText(user.username);
        holder.textRole.setText(user.isAdmin ? "Administrator" : "Standard User");
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textUsername;
        TextView textRole;
        Button btnDelete;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.text_username);
            textRole = itemView.findViewById(R.id.text_role);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
