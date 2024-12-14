//package com.example.studygroup;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class HomePage extends AppCompatActivity {
//
//    ImageView chathere, groupdetail, creategroup, mygroup, setting, search_img, myProfile, grouplist,logout;
//    FirebaseAuth mAuth;
//    FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.homepage);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        grouplist = findViewById(R.id.group_list);
//        chathere = findViewById(R.id.chat_here);
//        groupdetail = findViewById(R.id.group_detail);
//        creategroup = findViewById(R.id.create_group);
//        mygroup = findViewById(R.id.my_group);
//        setting = findViewById(R.id.setting);
//        myProfile = findViewById(R.id.my_profile);
//        search_img = findViewById(R.id.search);
//        logout=findViewById(R.id.logout_btn);
//
//        myProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadUserProfile();
//            }
//        });
//
//        grouplist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              Intent intent=new Intent(HomePage.this,AvailableGroupsActivity.class);
//              startActivity(intent);
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomePage.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        mygroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(HomePage.this, MyGroupsActivity.class);
//                startActivity(intent2);
//            }
//        });
//
//        creategroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(HomePage.this, CreateGroupActivity.class);
//                startActivity(intent3);
//            }
//        });
//
//        chathere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent4 = new Intent(HomePage.this, ChatinterfaceActivity.class);
//                startActivity(intent4);
//            }
//        });
//
////        search_img.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent i = new Intent(HomePage.this, Search.class);
////                startActivity(i);
////            }
////        });
//    }
//
//    private void loadUserProfile() {
//        String userId = mAuth.getCurrentUser().getUid();
//        DocumentReference docRef = db.collection("users").document(userId);
//        docRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    String username = document.getString("username");
//                    String fullName = document.getString("fname");
//                    String bio = document.getString("about");
//                    String email = document.getString("email");
//
//                    Intent intent = new Intent(HomePage.this, MyProfileActivity.class);
//                    intent.putExtra("username", username);
//                    intent.putExtra("full_name", fullName);
//                    intent.putExtra("bio", bio);
//                    intent.putExtra("email", email);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(HomePage.this, "User data not found", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(HomePage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
