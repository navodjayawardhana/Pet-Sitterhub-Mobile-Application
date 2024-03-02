package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



Button fab1,fab2;
    RecyclerView recyclerView1;
    List<DataClass> datalist;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter adapter;
    SearchView searchView;

    TextView nameView;



    private boolean shouldLaunchProfile = false;

    private void saveCareName(String nameToBeSaved) {
        DatabaseReference careNameRef = FirebaseDatabase.getInstance().getReference("CareNames");

        String uniqueKey = careNameRef.push().getKey();

        careNameRef.child(uniqueKey).child("careName").setValue(nameToBeSaved);

        Toast.makeText(this, "Username saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameView=findViewById(R.id.nameView2);
        fab1=findViewById(R.id.fab1p);
        recyclerView1=findViewById(R.id.recyclerView1);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        String nameFromIntent = getIntent().getStringExtra("name");
        nameView.setText(nameFromIntent);





        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            String username = intent.getStringExtra("username");

            adapter = new MyAdapter(MainActivity.this, datalist, username);
            recyclerView1.setAdapter(adapter);
        }

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();



                String nameUser = intent.getStringExtra("name");
                String emailUser = intent.getStringExtra("email");
                String usernameUser = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");

                // Pass user details to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("name", nameUser);
                profileIntent.putExtra("email", emailUser);
                profileIntent.putExtra("username", usernameUser);
                profileIntent.putExtra("password", password);
                startActivity(profileIntent);
                finish();
            }
        });






        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(MainActivity.this, 1);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        datalist = new ArrayList<>();
        adapter = new MyAdapter(MainActivity.this, datalist, "name");
        recyclerView1.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("pet details");
        dialog.show();

        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                String nameToDisplay = nameView.getText().toString();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    datalist.add(dataClass);

                    if (dataClass.getDataTitle().equals(nameToDisplay)){
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });




    }
    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: datalist){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }



}