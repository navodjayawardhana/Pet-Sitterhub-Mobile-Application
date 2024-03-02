package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    FloatingActionButton fab,fab1,fab3;
    RecyclerView recyclerView;
    List<DataClass> datalist;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter1 adapter; // Updated adapter class

    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        fab = findViewById(R.id.fab);
        fab1=findViewById(R.id.fab1);
        fab3=findViewById(R.id.fab3);
        recyclerView = findViewById(R.id.recyclerView);
        nameView = findViewById(R.id.nameView);


        String nameFromIntent = getIntent().getStringExtra("name");
        nameView.setText(nameFromIntent);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity1.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        datalist = new ArrayList<>();
        adapter = new MyAdapter1(MainActivity1.this, datalist);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("pet details");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                String nameToDisplay = nameView.getText().toString();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());

                    if (dataClass.getDataTitle().equals(nameToDisplay)) {
                        datalist.add(dataClass);
                    }
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }

        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                Intent intent = new Intent(MainActivity1.this, UploadActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                Intent intent = new Intent(MainActivity1.this, confurm.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();



                    String nameUser = intent.getStringExtra("name");
                    String emailUser = intent.getStringExtra("email");
                    String usernameUser = intent.getStringExtra("username");
                    String password = intent.getStringExtra("password");

                    // Pass user details to ProfileActivity
                    Intent profileIntent = new Intent(MainActivity1.this, ProfileActivity1.class);
                    profileIntent.putExtra("name", nameUser);
                    profileIntent.putExtra("email", emailUser);
                    profileIntent.putExtra("username", usernameUser);
                    profileIntent.putExtra("password", password);
                    startActivity(profileIntent);
                    finish();}

        });
    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : datalist) {
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }

    }
}
