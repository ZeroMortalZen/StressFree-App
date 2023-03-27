package com.linx.stress_free_app.AppUsageController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.R;

import java.util.List;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.ViewHolder> {
    private final List<AppUsage> appUsages;

    public AppUsageAdapter(List<AppUsage> appUsages) {
        this.appUsages = appUsages;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_usage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        AppUsage appUsage = appUsages.get(position);
        holder.appIcon.setImageDrawable(appUsage.getIcon());
        holder.appName.setText(appUsage.getName());
        holder.appUsage.setText(appUsage.getUsage());
    }

    @Override
    public int getItemCount() {
        return appUsages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView appUsage;

        public ViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            appUsage = itemView.findViewById(R.id.appUsage);
        }
    }
}