package com.example.studygroup;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableGroupsActivity extends AppCompatActivity {

    private ListView listViewGroups;
    private ArrayList<Group> groupsList;
    private GroupAdapter adapter;
    private DatabaseReference groupsRef;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaiable_groups);

        listViewGroups = findViewById(R.id.study_groups_list);
        groupsList = new ArrayList<>();
        adapter = new GroupAdapter(this, groupsList, getCurrentUserEmail(), this); // Pass 'this' as the last argument
        listViewGroups.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        groupsRef = database.getReference("groups");

        // Fetch groups from Firebase
        fetchGroupsFromFirebase();
    }

    private String getCurrentUserEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getEmail();
        } else {
            Toast.makeText(this, "No user signed in.", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
    }

    private void fetchGroupsFromFirebase() {
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    if (group != null) {
                        groupsList.add(group);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AvailableGroupsActivity.this, "Error fetching groups: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });}
        public void joinGroup(String groupName) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                String currentUserEmail = encodeEmail(firebaseUser.getEmail());
                DatabaseReference userGroupsRef = FirebaseDatabase.getInstance().getReference("user_groups").child(currentUserEmail);

                userGroupsRef.child(groupName).setValue(true).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AvailableGroupsActivity.this, "Joined group: " + groupName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AvailableGroupsActivity.this, "Failed to join group: " + groupName, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "No authenticated user found.", Toast.LENGTH_SHORT).show();
            }
        }

        private String encodeEmail(String email) {
            return email.replace(".", ",");
        }
    }


