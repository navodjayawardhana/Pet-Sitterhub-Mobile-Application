package com.example.myapplication;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;
    private int selectedItemPosition = RecyclerView.NO_POSITION;
    private String loggedInUsername;

    public MyAdapter(Context context, List<DataClass> dataList, String loggedInUsername) {
        this.context = context;
        this.dataList = dataList;
        this.loggedInUsername = loggedInUsername;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recaddress.setText(dataList.get(position).getDataAddress());
        holder.recsex.setText(dataList.get(position).getDataSex());
        holder.recpage.setText(dataList.get(position).getDataPage());
        holder.recdess.setText(dataList.get(position).getDataDesc());
        holder.recstart.setText(dataList.get(position).getDataStart());
        holder.resend.setText(dataList.get(position).getDataEnd());
        holder.resfees.setText(dataList.get(position).getDataFees());
        holder.itemView.setSelected(selectedItemPosition == position);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClass selectedData = dataList.get(position);
                sharePost(selectedData);
            }
        });


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }


    private void sharePost(DataClass selectedData) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareMessage = "Check out this post:\n"
                + selectedData.getDataTitle() + "\n"
                + selectedData.getDataDesc() + "\n"
                + "Posted by: " + loggedInUsername;

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    public DataClass getSelectedData() {
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            return dataList.get(selectedItemPosition);
        }
        return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        Button confurm;
        TextView recaddress, recsex, recpage, recdess, recstart, resend, resfees, recTitle,share;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recsex = itemView.findViewById(R.id.recsex);
            recaddress = itemView.findViewById(R.id.recaddress);
            recImage = itemView.findViewById(R.id.recImage1);
            recCard = itemView.findViewById(R.id.recCard);
            recTitle = itemView.findViewById(R.id.recTitle);
            recpage = itemView.findViewById(R.id.recPage);
            recdess = itemView.findViewById(R.id.recDescription);
            recstart = itemView.findViewById(R.id.recstart);
            resend = itemView.findViewById(R.id.recend);
            resfees = itemView.findViewById(R.id.recfees);
            confurm = itemView.findViewById(R.id.confurm1);
            share=itemView.findViewById(R.id.chat);





            confurm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof AppCompatActivity) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        Intent intent = activity.getIntent();
                        if (intent.hasExtra("name")) {
                            String nameToBeSaved = intent.getStringExtra("name");
                            DataClass selectedData = dataList.get(getAdapterPosition());
                            saveCareName(selectedData, nameToBeSaved);
                        }
                    }
                }
            });

        }

        private void saveCareName(DataClass selectedData, String usernameToBeSaved) {
            DatabaseReference careNameRef = FirebaseDatabase.getInstance().getReference("pet details");

            DatabaseReference newEntryRef = careNameRef.child(selectedData.getKey());

            newEntryRef.child("careName").setValue(usernameToBeSaved);

            Toast.makeText(itemView.getContext(), "Username saved successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
