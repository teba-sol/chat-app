package com.example.studygroup;

public class Message {
    private String userId;
    private String text;
    private long timestamp;
    private String userEmail; // New field for sender's email
    private String userImage; // Assuming userImage URL field

    // Constructors, getters, and setters
    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String userId, String text,  String userEmail,long timestamp, String userImage) {
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
