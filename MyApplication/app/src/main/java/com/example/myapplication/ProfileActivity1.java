package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity1 extends AppCompatActivity {
    TextView name,username,email,password;
    FloatingActionButton f1, f2, f3, f4;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);

        name=findViewById(R.id.titleName1);
        username=findViewById(R.id.titleUsername1);
        name=findViewById(R.id.profileName1);
        email=findViewById(R.id.profileEmail1);
        username=findViewById(R.id.profileUsername1);
        password=findViewById(R.id.profilePassword1);
        logout=findViewById(R.id.logout_button1);






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

        f1=findViewById(R.id.fab4p);
        f2=findViewById(R.id.fab1p);
        f3=findViewById(R.id.fabp);
        f4=findViewById(R.id.fab3p);

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                Intent intent = new Intent(ProfileActivity1.this, confurm.class);
                intent.putExtra("name", name1);
                startActivity(intent);
            }
        });

        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                Intent intent = new Intent(ProfileActivity1.this, UploadActivity.class);
                intent.putExtra("name", name1);
                startActivity(intent);
            }
        });
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String email1 = email.getText().toString();
                String usname = username.getText().toString();
                String pwd = password.getText().toString();
                Intent intent = new Intent(ProfileActivity1.this, MainActivity1.class);
                intent.putExtra("name", name1);
                intent.putExtra("email", email1);
                intent.putExtra("username", usname);
                intent.putExtra("password", pwd);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }





    }

