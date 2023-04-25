package com.linx.stress_free_app.DiarySystem;

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

public class DiaryEntryAdapter extends RecyclerView.Adapter<DiaryEntryAdapter.DiaryEntryViewHolder> {
    private List<DiaryEntry> diaryEntries;
    private String userEmail;
    private String profilePicUrl;
    private Context context;

    public DiaryEntryAdapter(Context context, List<DiaryEntry> diaryEntries, String userEmail, String profilePicUrl) {
        this.context = context;
        this.diaryEntries = diaryEntries;
        this.userEmail = userEmail;
        this.profilePicUrl = profilePicUrl;
    }

    @NonNull
    @Override
    public DiaryEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary_entry, parent, false);
        return new DiaryEntryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DiaryEntryViewHolder holder, int position) {
        DiaryEntry diaryEntry = diaryEntries.get(position);

        holder.tvUserEmail.setText(userEmail);
        holder.tvDiaryTitle.setText(diaryEntry.getTitle());
        holder.tvDiaryContent.setText(diaryEntry.getContent());

        Glide.with(context)
                .load(profilePicUrl)
                .placeholder(R.drawable.placeholder_image)
                .circleCrop()
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return diaryEntries.size();
    }

    public class DiaryEntryViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserEmail;
        public TextView tvDiaryTitle;
        public TextView tvDiaryContent;

        public DiaryEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvDiaryTitle = itemView.findViewById(R.id.tvDiaryTitle);
            tvDiaryContent = itemView.findViewById(R.id.tvDiaryContent);
        }
    }
}
