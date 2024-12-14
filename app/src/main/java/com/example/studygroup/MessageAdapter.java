package com.example.studygroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private List<Message> messages;
    private String currentUserId;
    private FirebaseFirestore firestore;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> objects, String currentUserId) {
        super(context, 0, objects);
        this.context = context;
        this.messages = objects;
        this.currentUserId = currentUserId;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Message message = getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_list_item, parent, false);
            holder = new ViewHolder();
            holder.senderImage = convertView.findViewById(R.id.senderImage);
            holder.senderName = convertView.findViewById(R.id.senderName);
            holder.messageText = convertView.findViewById(R.id.messageText);
            holder.messageTimestamp = convertView.findViewById(R.id.messageTimestamp);
            holder.messageContainer = convertView.findViewById(R.id.messageContainer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (message != null) {
            // Set message text
            holder.messageText.setText(message.getText());

            // Format and set the timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            holder.messageTimestamp.setText(sdf.format(new Date(message.getTimestamp())));

            if (message.getUserId() != null && message.getUserId().equals(currentUserId)) {
//                holder.messageContainer.setBackgroundResource(R.drawable.currentuser);
                holder.messageContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // Right to left layout for current user
            } else {
//                holder.messageContainer.setBackgroundResource(R.drawable.otheruser);
                holder.messageContainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // Left to right layout for other user
            }

            holder.senderImage.setVisibility(View.VISIBLE); // Show sender image for all messages
            holder.senderName.setVisibility(View.VISIBLE); // Show sender name for all messages

            // Load the sender's image using Glide (if available)
            if (message.getUserImage() != null && !message.getUserImage().isEmpty()) {
                Glide.with(context)
                        .load(message.getUserImage())
                        .placeholder(R.drawable.ui) // Placeholder image while loading
                        .into(holder.senderImage);
            } else {
                holder.senderImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.uimg));
            }

            // Retrieve and display the sender's name using email
            if (message.getUserEmail() != null && !message.getUserEmail().isEmpty()) {
                fetchUsername(message.getUserEmail(), holder);
            } else {
                holder.senderName.setText("Unknown User");
            }
        }

        return convertView;
    }

    private void fetchUsername(String userEmail, final ViewHolder holder) {
        Query query = firestore.collection("users").whereEqualTo("email", userEmail);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String username = document.getString("fname");
                    Log.d("MessageAdapter", "Username fetched: " + username); // Logging fetched username
                    if (username != null) {
                        holder.senderName.setText(username);
                    } else {
                        holder.senderName.setText("Unknown User");
                    }
                    break; // Assuming there's only one match
                }
            } else {
                Log.w("MessageAdapter", "No document found for user with email: " + userEmail);
                holder.senderName.setText("Unknown User");
            }
        }).addOnFailureListener(e -> {
            Log.e("MessageAdapter", "Failed to retrieve user data", e);
            holder.senderName.setText("Unknown User");
        });
    }

    private static class ViewHolder {
        ImageView senderImage;
        TextView senderName;
        TextView messageText;
        TextView messageTimestamp;
        LinearLayout messageContainer;
    }
}
