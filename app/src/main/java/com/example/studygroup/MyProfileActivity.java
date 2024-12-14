package com.example.studygroup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText full_name, bio, my_email, my_uname, address;
    private Button btnSave;
    private Uri imageUri;
     private ImageView edit;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(MyProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        profileImageView.setImageURI(selectedImageUri);
                        imageUri = selectedImageUri;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        profileImageView = findViewById(R.id.profile_image);
        full_name = findViewById(R.id.name_input);
        my_email = findViewById(R.id.email_input);
        bio = findViewById(R.id.bio_input);
        my_uname = findViewById(R.id.uname_input);
        edit=findViewById(R.id.passedit);
        address = findViewById(R.id.address_input);
        btnSave = findViewById(R.id.save_button);

        btnSave.setOnClickListener(v -> saveProfile());

        profileImageView.setOnClickListener(v -> changeProfilePicture());

        // Load profile data passed from HomePage
        loadUserProfile();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyProfileActivity.this, ChangepasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadUserProfile() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String fullName = intent.getStringExtra("full_name");
        String bioText = intent.getStringExtra("bio");
        String email = intent.getStringExtra("email");

        my_uname.setText(username);
        full_name.setText(fullName);
        bio.setText(bioText);
        my_email.setText(email);

        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String profileImageUrl = document.getString("profileImageUrl");
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.squareui) // Placeholder for loading state
                                .error(R.drawable.squareui) // Error placeholder if image fails to load
                                .into(profileImageView);
                    } else {
                        // Load default profile image if no custom image is set
                        Glide.with(this).load(R.drawable.squareui).into(profileImageView);
                    }
                } else {
                    Toast.makeText(MyProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MyProfileActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        String newUsername = my_uname.getText().toString();
        String newBio = bio.getText().toString();
        String newFullname = full_name.getText().toString();
        String newEmail = my_email.getText().toString();
        String newAddress = address.getText().toString();

        // Upload the profile image first
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();

                        // Save profile details including the image URL to Firestore
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put("username", newUsername);
                        userUpdates.put("bio", newBio);
                        userUpdates.put("profileImageUrl", downloadUrl);
                        userUpdates.put("fname", newFullname);
                        userUpdates.put("email", newEmail);
                        userUpdates.put("address", newAddress);

                        db.collection("users").document(userId).update(userUpdates)
                                .addOnSuccessListener(aVoid -> Toast.makeText(MyProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(MyProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(MyProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // Save profile details to Firestore without image URL
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("username", newUsername);
            userUpdates.put("bio", newBio);
            userUpdates.put("fname", newFullname);
            userUpdates.put("email", newEmail);
            userUpdates.put("address", newAddress);

            db.collection("users").document(userId).update(userUpdates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(MyProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(MyProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        Intent intent = new Intent(MyProfileActivity.this, groupdetials.class);
        intent.putExtra("fullName", newFullname); // Pass the full name to GroupDetailsActivity
// Add more data if needed
        startActivity(intent);
    }


    private void changeProfilePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(galleryIntent);
    }

}
