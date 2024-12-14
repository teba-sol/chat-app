package com.example.studygroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SettingActivity extends AppCompatActivity {
    TextView log_out, exit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // Enable edge-to-edge display if necessary
        EdgeToEdge.enable(this);

        log_out = findViewById(R.id.logout);
        exit_btn = findViewById(R.id.exit);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
                redirectToLogin();
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApplication();
            }
        });
    }

    private void logoutUser() {
        // Your logout logic, such as clearing user session, preferences, etc.
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // If using Firebase Authentication:
        // FirebaseAuth.getInstance().signOut();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back to it
    }

    private void exitApplication() {
        // Exit the application
        ActivityCompat.finishAffinity(this); // Finish all activities in the stack
        System.exit(0); // Ensure the application process is terminated
    }
}
