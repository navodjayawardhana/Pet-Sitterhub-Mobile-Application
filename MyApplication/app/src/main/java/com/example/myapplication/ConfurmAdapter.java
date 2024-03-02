package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ConfurmAdapter extends RecyclerView.Adapter<ConfurmViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    private DatabaseReference databaseReference;


    public ConfurmAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ConfurmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_item, parent, false);
        return new ConfurmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfurmViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.conImage);
        holder.conCareGiven.setText(dataList.get(position).getCareName());
        holder.conPetname.setText(dataList.get(position).getDataPname());
        holder.conCname.setText(dataList.get(position).getDataTitle());

        holder.feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract relevant information
                String careGiven = dataList.get(position).getCareName();
                String conName = holder.conCname.getText().toString();
                String feedbackDescription = holder.feedbackdes.getText().toString();
                String feedbackRating = holder.ratenum.getText().toString();

                saveFeedbackToDatabase(careGiven, conName, feedbackDescription, feedbackRating);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void saveFeedbackToDatabase(String careGiven, String conName, String feedbackDescription, String feedbackRating) {

        DatabaseReference feedbackTableRef = databaseReference.child("feedback").child(careGiven);


        String feedbackKey = feedbackTableRef.push().getKey();


        DatabaseReference feedbackRef = feedbackTableRef.child(feedbackKey);

        feedbackRef.child("conName").setValue(conName);
        feedbackRef.child("feedbackDescription").setValue(feedbackDescription);
        feedbackRef.child("feedbackRating").setValue(feedbackRating);

        showToast("Feedback submitted successfully!");

    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}



    class ConfurmViewHolder extends RecyclerView.ViewHolder{

    ImageView conImage;
    TextView conCareGiven,conPetname,conCname;
    CardView conCard;
    Button feedbackbtn;
    EditText feedbackdes,ratenum;


    public ConfurmViewHolder(@NonNull View itemView) {
        super(itemView);

        conImage=itemView.findViewById(R.id.conImage1);
        conCareGiven=itemView.findViewById(R.id.conaregiver);
        conPetname=itemView.findViewById(R.id.conpname1);
        conCard=itemView.findViewById(R.id.conCard3);
        conCname=itemView.findViewById(R.id.concname1);
        feedbackbtn=itemView.findViewById(R.id.confeedback);
        feedbackdes=itemView.findViewById(R.id.feedback);
        ratenum=itemView.findViewById(R.id.feedbacknum);

    }
}

