package com.linx.stress_free_app.ExercisePlayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.linx.stress_free_app.R;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private List<ImageData> images;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ImageData imageData, int position);
    }

    public ImagesAdapter(List<ImageData> images, OnItemClickListener onItemClickListener) {
        this.images = images;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(images.get(position), position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumbnail;
        TextView imageName;
        OnItemClickListener onItemClickListener;

        public ImageViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.image_thumbnail);
            imageName = itemView.findViewById(R.id.image_name);
            this.onItemClickListener = onItemClickListener;
        }

        public void bind(ImageData imageData, int position) {
            imageName.setText(imageData.getName());
            Glide.with(itemView)
                    .asGif()
                    .load(imageData.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageThumbnail);

            itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(imageData, position);
            });
        }
    }
}
