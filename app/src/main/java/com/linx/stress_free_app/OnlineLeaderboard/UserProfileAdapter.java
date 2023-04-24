package com.linx.stress_free_app.OnlineLeaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linx.stress_free_app.R;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> userProfiles;

    public UserProfileAdapter(Context context, List<UserProfile> userProfiles) {
        this.context = context;
        this.userProfiles = userProfiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile userProfile = userProfiles.get(position);
        Glide.with(context).load(userProfile.getProfileImageUrl()).into(holder.profileImage);
        holder.profileEmail.setText(userProfile.getEmail());
        holder.profileRank.setText("Rank: " + userProfile.getRank());
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView profileEmail, profileRank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            profileEmail = itemView.findViewById(R.id.profile_email);
            profileRank = itemView.findViewById(R.id.profile_rank);
        }
    }
}
