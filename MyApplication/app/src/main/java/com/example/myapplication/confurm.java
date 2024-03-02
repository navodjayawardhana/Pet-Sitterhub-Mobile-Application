package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class confurm extends AppCompatActivity {

    FloatingActionButton fab,fab1,fab4;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    TextView nameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        nameView = findViewById(R.id.nameView);
        String nameFromIntent = getIntent().getStringExtra("name");
        nameView.setText(nameFromIntent);
        fab=findViewById(R.id.fab3);
        fab1=findViewById(R.id.fab);
        fab4=findViewById(R.id.fab4);
        recyclerView=findViewById(R.id.recyclerView);





        GridLayoutManager gridLayoutManager =new GridLayoutManager(confurm.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);


        AlertDialog.Builder builder = new AlertDialog.Builder(confurm.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();



        dataList=new ArrayList<>();

        ConfurmAdapter adaptercon=new ConfurmAdapter(confurm.this,dataList);
        recyclerView.setAdapter(adaptercon);

        databaseReference= FirebaseDatabase.getInstance().getReference("confurm_table");
        dialog.show();



        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                String nameToDisplay=nameView.getText().toString();
                for (DataSnapshot itemSnapshot:snapshot.getChildren()){
                    DataClass dataClass=itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());


                    if(dataClass.getDataTitle().equals(nameToDisplay)){
                        dataList.add(dataClass);
                    }

                }
                adaptercon.notifyDataSetChanged();
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
                Intent intent = new Intent(confurm.this, ProfileActivity1.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                Intent intent = new Intent(confurm.this, UploadActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                Intent intent = new Intent(confurm.this, MainActivity1.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });



    }
}