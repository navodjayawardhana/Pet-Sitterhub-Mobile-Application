package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    TextView updateFees;
    Button updateButton,feesButtonupdate;
    EditText updateTopic, updateDesc, updateLang,updatePName,updatePage,updateCaddress,updateStart, updateEnd;
    RadioButton updateBreedYes, updateBreedNo, updateSexMale, updateSexFemale;
    String key,oldimageURL,imageUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private static final int FEES_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateImage=findViewById(R.id.updateImage);
        updateButton=findViewById(R.id.updateButton);
        updateTopic=findViewById(R.id.updateTopic);
        updateDesc=findViewById(R.id.updateDesc);
        updateLang=findViewById(R.id.updateLang);
        updatePName=findViewById(R.id.updatePname);
        updatePage=findViewById(R.id.updatePage);
        updateCaddress=findViewById(R.id.updateCaddress);
        updateBreedYes=findViewById(R.id.updateBreedyes);
        updateBreedNo=findViewById(R.id.updateBreedno);
        updateSexMale=findViewById(R.id.updateSexmale);
        updateSexFemale=findViewById(R.id.updateSexfemale);
        updateFees=findViewById(R.id.updateFeesView);
        updateStart=findViewById(R.id.updateStart);
        updateEnd=findViewById(R.id.updateEnd);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateTopic.setText(bundle.getString("Title"));
            updateCaddress.setText(bundle.getString("Address"));
            updatePName.setText(bundle.getString("Pet Name"));
            updatePage.setText(bundle.getString("Pet Age"));
            updateDesc.setText(bundle.getString("Description"));
            updateLang.setText(bundle.getString("colour"));
            updateFees.setText(bundle.getString("fees"));
            updateStart.setText(bundle.getString("start"));
            updateEnd.setText(bundle.getString("end"));
            key = bundle.getString("Key");
            oldimageURL = bundle.getString("Image");

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("pet details").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void saveData(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });


    }

    public void updateData(){
        String title = updateTopic.getText().toString().trim();
        String desc = updateDesc.getText().toString().trim();
        String lang = updateLang.getText().toString().trim();
        String address = updateCaddress.getText().toString().trim();
        String pname = updatePName.getText().toString().trim();
        String page = updatePage.getText().toString().trim();
        String start = updateStart.getText().toString().trim();
        String end = updateEnd.getText().toString().trim();
        String fees = updateFees.getText().toString().trim();





        String breed;
        if (updateBreedYes.isChecked()){
            breed=updateBreedYes.getText().toString().trim();
        } else if (updateBreedNo.isChecked()) {
            breed=updateBreedNo.getText().toString().trim();
        }else {
            Toast.makeText(UpdateActivity.this, "Please select breed", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex;
        if (updateSexMale.isChecked()){
            sex=updateSexMale.getText().toString().trim();
        } else if (updateSexFemale.isChecked()) {
            sex=updateSexFemale.getText().toString().trim();
        }else {
            Toast.makeText(UpdateActivity.this, "Please select breed", Toast.LENGTH_SHORT).show();
            return;
        }
        DataClass dataClass = new DataClass(title, desc, lang, address, pname, page, start, end, fees,breed,sex, imageUrl);


        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
           public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                   StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldimageURL);
                    reference.delete();
                   Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
           }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}