package com.linx.stress_free_app.OnlineLeaderboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> userList;

    public LeaderboardAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.emailTextView.setText(user.getEmail());

        // Load the profile picture using GlideApp and FirebaseStorageLoader
        if (user.getProfilePicRef() != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getProfilePicRef());

            GlideApp.with(holder.profileImageView.getContext())
                    .load(user.getProfilePicUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.error_image);
        }

        // Set the rank and the corresponding colors for the top 3 positions
        int rank = position + 1;
        holder.rankTextView.setText(String.valueOf(rank));
        switch (rank) {
            case 1:
                holder.rankTextView.setTextColor(Color.parseColor("#FFD700")); // Gold
                break;
            case 2:
                holder.rankTextView.setTextColor(Color.parseColor("#C0C0C0")); // Silver
                break;
            case 3:
                holder.rankTextView.setTextColor(Color.parseColor("#CD7F32")); // Bronze
                break;
            default:
                holder.rankTextView.setTextColor(Color.BLACK); // Default color for other positions
                break;
        }
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        public TextView emailTextView;
        public TextView rankTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            emailTextView = itemView.findViewById(R.id.email_text);
            rankTextView = itemView.findViewById(R.id.rank_text);
        }
    }
}
