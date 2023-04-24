package com.linx.stress_free_app.NotificationSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.linx.stress_free_app.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = intent.getStringExtra("channelId");

        String contentTitle;
        String contentText;
        int notificationId;
        int imageResourceId;

        if ("channel1".equals(channelId)) {
            contentTitle = "Reminder";
            contentText = "There are Breathing Exercises, Yoga and some music to be listened should come back on the app.";
            notificationId = 1;
            imageResourceId = R.drawable.lotus; // Replace with your yoga image resource ID
        } else {
            contentTitle = "Tasks";
            contentText = "There are some tasks to be done.";
            notificationId = 2;
            imageResourceId = R.drawable.clock; // Replace with your clock image resource ID
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(imageResourceId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}

