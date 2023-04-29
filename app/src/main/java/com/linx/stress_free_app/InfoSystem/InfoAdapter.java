package com.linx.stress_free_app.InfoSystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    private Context context;
    private List<InfoItem> infoItems;

    public InfoAdapter(Context context, List<InfoItem> infoItems) {
        this.context = context;
        this.infoItems = infoItems;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_info, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        InfoItem currentItem = infoItems.get(position);
        holder.itemIcon.setImageResource(currentItem.getIcon());
        holder.itemTitle.setText(currentItem.getTitle());
        holder.itemDescription.setText(currentItem.getDescription());
        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getUrl()));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return infoItems.size();
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemTitle;
        TextView itemDescription;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.itemIcon);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDescription = itemView.findViewById(R.id.itemDescription);
        }
    }
}


