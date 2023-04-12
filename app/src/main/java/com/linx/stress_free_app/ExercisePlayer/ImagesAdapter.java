package com.linx.stress_free_app.ExercisePlayer;

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

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private List<ImageData> images;
    private OnImageClickListener onImageClickListener;

    public interface OnImageClickListener {
        void onImageClick(String imageUrl);
    }

    public ImagesAdapter(List<ImageData> images, OnImageClickListener onImageClickListener) {
        this.images = images;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageViewHolder(view, onImageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumbnail;
        TextView imageName;
        OnImageClickListener onImageClickListener;

        public ImageViewHolder(@NonNull View itemView, OnImageClickListener onImageClickListener) {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.image_thumbnail);
            imageName = itemView.findViewById(R.id.image_name);
            this.onImageClickListener = onImageClickListener;
        }

        public void bind(ImageData imageData) {
            imageName.setText(imageData.getName());
            Glide.with(itemView)
                    .load(imageData.getImageUrl())
                    .placeholder(R.drawable.akwhitelogo) // Add a placeholder image if needed
                    .into(imageThumbnail);

            itemView.setOnClickListener(v -> onImageClickListener.onImageClick(imageData.getImageUrl()));
        }
    }
}

