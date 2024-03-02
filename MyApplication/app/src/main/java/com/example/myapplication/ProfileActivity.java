package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.EditProfile;
import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView name,username,email,password;
    private Button fab,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.titleName);
        username = findViewById(R.id.profileUsername);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        username = findViewById(R.id.profileUsername);
        password = findViewById(R.id.profilePassword);
        fab=findViewById(R.id.fab8p);
        logout=findViewById(R.id.logout_button);

        Intent intent = getIntent();
        if (intent.hasExtra("name") && intent.hasExtra("email") && intent.hasExtra("username") && intent.hasExtra("password")) {
            String nameUser = intent.getStringExtra("name");
            String emailUser = intent.getStringExtra("email");
            String usernameUser = intent.getStringExtra("username");
            String password1 = intent.getStringExtra("password");

            name.setText(nameUser);
            username.setText(usernameUser);
            email.setText(emailUser);
            password.setText(password1);

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();



                String nameUser = intent.getStringExtra("name");
                String emailUser = intent.getStringExtra("email");
                String usernameUser = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");

                // Pass user details to ProfileActivity
                Intent profileIntent = new Intent(ProfileActivity.this, MainActivity.class);
                profileIntent.putExtra("name", nameUser);
                profileIntent.putExtra("email", emailUser);
                profileIntent.putExtra("username", usernameUser);
                profileIntent.putExtra("password", password);
                startActivity(profileIntent);
                finish();
            }
        });

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        }


    }

