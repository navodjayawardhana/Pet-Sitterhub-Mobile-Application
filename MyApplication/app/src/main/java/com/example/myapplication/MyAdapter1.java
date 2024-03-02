package com.example.myapplication;// MyAdapter1.java

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter1 extends RecyclerView.Adapter<MyViewHolder1> {
    private Context context;
    private List<DataClass> dataList;
    private String careName;

    public MyAdapter1(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item1, parent, false);
        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage1);
        holder.recstart1.setText(dataList.get(position).getDataStart());
        holder.resend1.setText(dataList.get(position).getDataEnd());
        holder.resfees1.setText(dataList.get(position).getDataFees());
        holder.recpname1.setText(dataList.get(position).getDataPname());
        holder.reccaregiver.setText(dataList.get(position).getCareName());

        holder.recCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity1.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Address",dataList.get(holder.getAdapterPosition()).getDataAddress());
                intent.putExtra("Pet Name",dataList.get(holder.getAdapterPosition()).getDataPname());
                intent.putExtra("Pet Age",dataList.get(holder.getAdapterPosition()).getDataPage());
                intent.putExtra("Breed",dataList.get(holder.getAdapterPosition()).getDataBreed());
                intent.putExtra("sex",dataList.get(holder.getAdapterPosition()).getDataSex());
                intent.putExtra("colour",dataList.get(holder.getAdapterPosition()).getDataLang());
                intent.putExtra("start",dataList.get(holder.getAdapterPosition()).getDataStart());
                intent.putExtra("end",dataList.get(holder.getAdapterPosition()).getDataEnd());
                intent.putExtra("fees",dataList.get(holder.getAdapterPosition()).getDataFees());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());


                context.startActivity(intent);
            }
        });

        String careName = dataList.get(position).getCareName();
        if (careName != null && !careName.isEmpty()) {
            holder.cofurm.setVisibility(View.VISIBLE);
            holder.delite.setVisibility(View.VISIBLE);
            holder.reccaregiveqr.setVisibility(View.VISIBLE);
        } else {
            holder.cofurm.setVisibility(View.GONE);
            holder.delite.setVisibility(View.GONE);
            holder.reccaregiveqr.setVisibility(View.GONE);
        }




        holder.cofurm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClass selectedData = dataList.get(holder.getAdapterPosition());
                moveDetailsToConfurmTable(selectedData);
            }
        });

        holder.delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClass selectedData = dataList.get(holder.getAdapterPosition());
                clearCareName(selectedData);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void moveDetailsToConfurmTable(DataClass selectedData) {
        DatabaseReference currentTableRef = FirebaseDatabase.getInstance().getReference("pet details");
        DatabaseReference confurmTableRef = FirebaseDatabase.getInstance().getReference("confurm_table");


        currentTableRef.child(selectedData.getKey()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String newKey = confurmTableRef.push().getKey();
                        selectedData.setKey(newKey);
                        confurmTableRef.child(newKey).setValue(selectedData);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to move details to confurm table", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearCareName(DataClass selectedData) {
        DatabaseReference currentTableRef = FirebaseDatabase.getInstance().getReference("pet details");

        currentTableRef.child(selectedData.getKey()).child("careName").setValue("")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        notifyDataSetChanged();
                        Toast.makeText(context, "CareName send successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to set CareName", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

class MyViewHolder1 extends RecyclerView.ViewHolder {
    ImageView recImage1;
    TextView recstart1, resend1, resfees1, recpname1,reccaregiver,reccaregiveqr;
    CardView recCard1;
    Button cofurm,delite,feedback;

    public MyViewHolder1(@NonNull View itemView) {
        super(itemView);
        recImage1 = itemView.findViewById(R.id.recImage1);
        recstart1 = itemView.findViewById(R.id.recstart1);
        resend1 = itemView.findViewById(R.id.recend1);
        resfees1 = itemView.findViewById(R.id.recfees1);
        recpname1 = itemView.findViewById(R.id.recpname1);
        recCard1 = itemView.findViewById(R.id.recCard1);
        reccaregiver=itemView.findViewById(R.id.reccaregiver);
        reccaregiveqr=itemView.findViewById(R.id.reccaregiveqr);
        cofurm=itemView.findViewById(R.id.recconfurm);
        delite=itemView.findViewById(R.id.recdelite);
    }
}
