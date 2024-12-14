package com.example.studygroup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity {
    ImageView grouimg;
    EditText gname, gfocus, gadmin;
    Button creatgroup, cancelg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        gname = findViewById(R.id.groupNameInput);
        gfocus = findViewById(R.id.groupFocusInput);
        gadmin = findViewById(R.id.admin);
        creatgroup = findViewById(R.id.createGroupButton);
        cancelg = findViewById(R.id.cancelbtn);



        creatgroup.setOnClickListener(v -> createGroup());
        cancelg.setOnClickListener(v -> finish());
    }

    // Method to create a group and store it in Firebase
    public void createGroup() {
        String groupName = gname.getText().toString().trim();
        String groupFocus = gfocus.getText().toString().trim();
        String groupAdmin = gadmin.getText().toString().trim();

        // Create a CreatGroup object
        CreatGroup group = new CreatGroup(groupName, groupFocus, groupAdmin);
        Toast.makeText(this, "Successfully created", Toast.LENGTH_SHORT).show();

        // Assume you have a Firebase reference
        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        String groupId = groupsRef.push().getKey(); // Generate a unique key for the group

        // Store the group in Firebase
        groupsRef.child(groupId).setValue(group);

        // Show a notification
        showNotification(groupName);
    }

    private void showNotification(String groupName) {
        // Create a notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "group_creation_channel";
            CharSequence channelName = "Group Creation Notifications";
            String channelDescription = "Notifications for newly created groups";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "group_creation_channel")
                .setSmallIcon(R.drawable.ic_group) // Replace with your app's icon
                .setContentTitle("New Group Created")
                .setContentText("Group " + groupName + " has been successfully created.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification with a unique ID
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
