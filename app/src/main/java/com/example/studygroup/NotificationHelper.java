package com.example.studygroup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static void showGroupCreationNotification(Context context, String groupName) {
        // Create a notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "group_creation_channel";
            CharSequence channelName = "Group Creation Notifications";
            String channelDescription = "Notifications for newly created groups";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Set importance to high for heads-up notification
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // Show on lock screen
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "group_creation_channel")
                .setSmallIcon(R.drawable.ic_group) // Replace with your app's icon
                .setContentTitle("New Group Created")
                .setContentText("Group " + groupName + " has been successfully created.")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to high for heads-up notification
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
                .setAutoCancel(false); // Keep notification in the drawer after it's dismissed

        // Show the notification with a unique ID
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}


