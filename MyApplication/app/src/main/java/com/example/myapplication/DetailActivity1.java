package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity1 extends AppCompatActivity {

    TextView detailCname,detailCaddress,DetailPname,detailPage,detailbreed,detailSex,detailClour,detailDesc;
    ImageView detailImage;
    FloatingActionButton deliteButton,editButton;

    String Key="";
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);

        detailCname=findViewById(R.id.detailCname);
        detailCaddress=findViewById(R.id.detailAddress);
        DetailPname=findViewById(R.id.detailPname);
        detailPage=findViewById(R.id.detailPage);
        detailbreed=findViewById(R.id.detailBreed);
        detailSex=findViewById(R.id.detailSex);
        detailClour=findViewById(R.id.detailColour);
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        deliteButton =findViewById(R.id.deleteButton);
        editButton=findViewById(R.id.editButton);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailCname.setText(bundle.getString("Title"));
            detailCaddress.setText(bundle.getString("Address"));
            DetailPname.setText(bundle.getString("Pet Name"));
            detailPage.setText(bundle.getString("Pet Age"));
            detailbreed.setText(bundle.getString("Breed"));
            detailSex.setText(bundle.getString("sex"));
            detailClour.setText(bundle.getString("colour"));
            detailDesc.setText(bundle.getString("Description"));
            Key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");

            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pet details");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                        public void onSuccess(Void unused) {
                            reference.child(Key).removeValue();
                            Toast.makeText(DetailActivity1.this, "Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DetailActivity1.class));
                            finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity1.this,UpdateActivity.class)
                        .putExtra("Title",detailCname.getText().toString())
                        .putExtra("Address",detailCaddress.getText().toString())
                        .putExtra("Pet Name",DetailPname.getText().toString())
                        .putExtra("Pet Age",detailPage.getText().toString())
                        .putExtra("Breed",detailbreed.getText().toString())
                        .putExtra("sex",detailSex.getText().toString())
                        .putExtra("colour",detailClour.getText().toString())
                        .putExtra("Description",detailDesc.getText().toString())
                        .putExtra("Image",imageUrl)
                        .putExtra("Key",Key);
                startActivity(intent);

            }
        });

    }
}