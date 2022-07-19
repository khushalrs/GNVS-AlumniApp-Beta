package com.example.mymessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BatchAdapter extends RecyclerView.Adapter{
    private ArrayList<String> batches;

    public BatchAdapter(){}

    public BatchAdapter(ArrayList<String> batches){
        this.batches = batches;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BatchAdapter.BatchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = batches.get(position);
        ((BatchAdapter.BatchHolder) holder).bind(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
            }
        });
    }

    @Override
    public int getItemCount() {
        return batches.size();
    }

    private static class BatchHolder extends RecyclerView.ViewHolder{
        TextView batchname;
        public BatchHolder(@NonNull View itemView) {
            super(itemView);
            batchname = itemView.findViewById(R.id.batchname);
        }
        public void bind(String name){
            batchname.setText(name);
        }
    }

}
