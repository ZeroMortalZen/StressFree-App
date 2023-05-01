package com.linx.stress_free_app.GoalSystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.R;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
    private int yogaProgress;
    private int meditationProgress;

    public ProgressAdapter(int yogaProgress, int meditationProgress) {
        this.yogaProgress = yogaProgress;
        this.meditationProgress = meditationProgress;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        if (position == 0) {
            holder.updateViews("Yoga Progress", yogaProgress);
        } else if (position == 1) {
            holder.updateViews("Meditation Progress", meditationProgress);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setYogaProgress(int progress) {
        this.yogaProgress = progress;
        notifyDataSetChanged();
    }

    public void setMeditationProgress(int progress) {
        this.meditationProgress = progress;
        notifyDataSetChanged();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;
        private ProgressBar progressBar;
        private ImageView icon;

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            progressBar = itemView.findViewById(R.id.progress_bar);
            icon = itemView.findViewById(R.id.icon);
        }

        void updateViews(String progressTitle, int progress) {
            title.setText(progressTitle);
            progressBar.setMax(100);

            if (progress == 1) {
                progressBar.setProgress(33);
                description.setText("You have completed 1/3 of the exercises");
                icon.setImageResource(R.drawable.check);
            } else if (progress == 2) {
                progressBar.setProgress(66);
                description.setText("You have completed 2/3 of the exercises");
                icon.setImageResource(R.drawable.cancel);
            } else if (progress == 3) {
                progressBar.setProgress(100);
                description.setText("You have completed 3/3 of the exercises");
                icon.setImageResource(R.drawable.check);
            } else {
                progressBar.setProgress(0);
                description.setText("You have not started any exercises");
                icon.setImageResource(R.drawable.cancel);
            }
        }
    }
}
