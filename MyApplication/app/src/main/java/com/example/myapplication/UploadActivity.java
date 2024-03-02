package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.Manifest;
import androidx.activity.result.contract.ActivityResultContracts;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton, feesButton;
    TextView uplodFees;
    EditText uploadTopic, uploadDesc, uploadLang, uploadPName, uploadPage, uploadCaddress, uploadStart, uploadEnd;
    RadioButton uploadBreedYes, uploadBreedNo, uploadSexMale, uploadSexFemale;
    String imageURL;
    Uri uri;

    private static final int FEES_ACTIVITY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplod);

        uploadImage = findViewById(R.id.uploadImage);
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadLang = findViewById(R.id.uploadLang);
        saveButton = findViewById(R.id.saveButton);
        feesButton = findViewById(R.id.feeButton);

        uploadPName = findViewById(R.id.uploadPname);
        uploadPage = findViewById(R.id.uploadPage);
        uploadCaddress = findViewById(R.id.uploadCaddress);
        uploadBreedYes = findViewById(R.id.UplodBreedyes);
        uploadBreedNo = findViewById(R.id.UplodBreedno);
        uploadSexFemale = findViewById(R.id.UplodSexfemale);
        uploadSexMale = findViewById(R.id.UplodSexmale);
        uplodFees = findViewById(R.id.FeesView);
        uploadStart = findViewById(R.id.uploadStart);
        uploadEnd = findViewById(R.id.uploadEnd);

        String nameFromIntent = getIntent().getStringExtra("name");
        uploadTopic.setText(nameFromIntent);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {

                        Toast.makeText(this, "Permission denied: CAMERA", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
  feesButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(UploadActivity.this, fees.class);
        intent.putExtra("uploadStart", uploadStart.getText().toString());
        intent.putExtra("uploadEnd", uploadEnd.getText().toString());
        intent.putExtra("feesView", uplodFees.getText().toString());
        startActivityForResult(intent, FEES_ACTIVITY_REQUEST_CODE);
    }
});

    }

    private void showImageSourceDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                // Check and request CAMERA permission before opening the camera
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            } else if (options[item].equals("Choose from Gallery")) {
                openGallery();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        activityResultLauncher.launch(photoPicker);
    }






    public void saveData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }

    public void uploadData() {
        String title = uploadTopic.getText().toString();
        String desc = uploadDesc.getText().toString();
        String lang = uploadLang.getText().toString();
        String address = uploadCaddress.getText().toString();
        String pname = uploadPName.getText().toString();
        String page = uploadPage.getText().toString();
        String start=uploadStart.getText().toString();
        String end=uploadEnd.getText().toString();
        String fees=uplodFees.getText().toString();

        String breed;
        if (uploadBreedYes.isChecked()) {
            breed = uploadBreedYes.getText().toString();
        } else if (uploadBreedNo.isChecked()) {
            breed = uploadBreedNo.getText().toString();
        } else {
            Toast.makeText(UploadActivity.this, "Please select breed", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex;
        if (uploadSexMale.isChecked()) {
            sex = uploadSexMale.getText().toString();
        } else if (uploadSexFemale.isChecked()) {
            sex = uploadSexFemale.getText().toString();
        } else {
            Toast.makeText(UploadActivity.this, "Please select breed", Toast.LENGTH_SHORT).show();
            return;
        }

        String entryKey = FirebaseDatabase.getInstance().getReference("pet details").push().getKey();



        DataClass dataClass = new DataClass(title, desc, lang, address, pname, page, start, end, fees,breed,sex, imageURL);

        FirebaseDatabase.getInstance().getReference("pet details").child(entryKey).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            uploadImage.setImageURI(uri);
        } else if (requestCode == FEES_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String uploadStartValue = data.getStringExtra("uploadStart");
                String uploadEndValue = data.getStringExtra("uploadEnd");
                String feesViewValue = data.getStringExtra("feesView");


                uploadStart.setText(uploadStartValue);
                uploadEnd.setText(uploadEndValue);
                uplodFees.setText(feesViewValue);
            }
        }
    }
}
