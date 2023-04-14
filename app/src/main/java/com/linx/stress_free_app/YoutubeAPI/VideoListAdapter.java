package com.linx.stress_free_app.YoutubeAPI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private List<VideoItem> videoList;
    private OnVideoItemClickListener onVideoItemClickListener;

    public interface OnVideoItemClickListener {
        void onVideoItemClick(VideoItem videoItem);
    }

    public VideoListAdapter(List<VideoItem> videoList, OnVideoItemClickListener onVideoItemClickListener) {
        this.videoList = videoList;
        this.onVideoItemClickListener = onVideoItemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoList.get(position);
        holder.bind(videoItem);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumbnail;
        private TextView videoTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
            videoTitle = itemView.findViewById(R.id.video_title);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onVideoItemClickListener.onVideoItemClick(videoList.get(position));
                }
            });
        }

        public void bind(VideoItem videoItem) {
            videoTitle.setText(videoItem.getTitle());
            Picasso.get().load(videoItem.getThumbnailUrl()).into(videoThumbnail);
        }
    }
}

