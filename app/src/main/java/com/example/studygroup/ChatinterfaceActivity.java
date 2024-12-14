package com.example.studygroup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatinterfaceActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private ImageView buttonSend;
    private ListView listViewMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatinterface);

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextMessage = findViewById(R.id.messageInput);
        buttonSend = findViewById(R.id.sendButton);
        listViewMessages = findViewById(R.id.messagesList);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUser.getUid());
        listViewMessages.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        loadMessages();
    }

    private void loadMessages() {
        firestore.collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(ChatinterfaceActivity.this, "Error loading messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        messageList.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Message message = doc.toObject(Message.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                        listViewMessages.smoothScrollToPosition(messageList.size() - 1); // Scroll to the last message
                    }
                });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please type a message.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create a Message object
        Message message = new Message(currentUser.getUid(), messageText, currentUser.getEmail(), timestamp, null);

        // Save message to Firestore
        firestore.collection("messages")
                .document()
                .set(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        editTextMessage.setText(""); // Clear the message input field after sending
                        Toast.makeText(ChatinterfaceActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatinterfaceActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
