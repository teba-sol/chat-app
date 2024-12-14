package com.example.studygroup;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {


    private ListView listViewGroups;
    private SearchGroupAdapter adapter;
    private List<Group> groupsList;
    private SearchView searchView;
    private TextView noGroupsFoundView;

    private DatabaseReference groupsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchscreen);

        listViewGroups = findViewById(R.id.listViewGroups);
        searchView = findViewById(R.id.searchView);
        noGroupsFoundView = findViewById(R.id.no_groups_found);

        groupsList = new ArrayList<>();
        adapter = new SearchGroupAdapter(this, groupsList, noGroupsFoundView);
        listViewGroups.setAdapter(adapter);

        // Initialize Firebase
        groupsRef = FirebaseDatabase.getInstance().getReference().child("groups");

        // Load groups from Firebase
        loadGroupsFromFirebase();

        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter("");
                } else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void loadGroupsFromFirebase() {
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Group> fetchedGroups = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    fetchedGroups.add(group);
                }
                adapter.updateOriginalList(fetchedGroups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}