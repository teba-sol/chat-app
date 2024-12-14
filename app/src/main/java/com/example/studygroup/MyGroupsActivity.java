package com.example.studygroup;

import android.os.Bundle;
import android.util.Log;
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

public class MyGroupsActivity extends AppCompatActivity {

    private ListView listViewMyGroups;
    private ArrayList<String> myGroupNamesList;
    private GroupNameAdapter adapter;
    private DatabaseReference userGroupsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygroups);

        listViewMyGroups = findViewById(R.id.mygrouplist);
        myGroupNamesList = new ArrayList<>();
        adapter = new GroupNameAdapter(this, myGroupNamesList);
        listViewMyGroups.setAdapter(adapter);

        // Fetch user's groups from Firebase
        fetchUserGroupsFromFirebase();
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

    private void fetchUserGroupsFromFirebase() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String currentUserEmail = encodeEmail(firebaseUser.getEmail());
            userGroupsRef = FirebaseDatabase.getInstance().getReference("user_groups").child(currentUserEmail);

            userGroupsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myGroupNamesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String groupName = snapshot.getKey();
                        Log.d("MyGroupsActivity", "Fetched group: " + groupName); // Log fetched group
                        if (groupName != null) {
                            myGroupNamesList.add(groupName);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MyGroupsActivity.this, "Error fetching groups: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }
}
