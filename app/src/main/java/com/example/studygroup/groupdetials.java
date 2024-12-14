package com.example.studygroup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class groupdetials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_detail);
        String groupId = getIntent().getStringExtra("groupId");
        String groupName = getIntent().getStringExtra("groupName");
        String groupFocus = getIntent().getStringExtra("groupFocus");
        String groupAdmin = getIntent().getStringExtra("groupAdmin");

        TextView textViewGroupName = findViewById(R.id.textViewGroupName);
        TextView textViewGroupFocus = findViewById(R.id.textViewGroupFocus);
        TextView textViewGroupAdmin = findViewById(R.id.textViewGroupAdmin);

        textViewGroupName.setText("Group Name: " + groupName);
        textViewGroupFocus.setText("Group focus "+groupFocus);
        textViewGroupAdmin.setText("Admin " + groupAdmin);

    }
}
