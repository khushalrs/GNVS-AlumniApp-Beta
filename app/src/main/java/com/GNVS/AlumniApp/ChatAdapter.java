package com.GNVS.AlumniApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    private ArrayList<String> mChats;
    private ItemClickListener itemClickListener;
    public ChatAdapter(){}

    public ChatAdapter(Context context, ArrayList<String> nameList, ItemClickListener listener){
        mChats = nameList;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = mChats.get(position);
        ((ChatAdapter.ChatHolder) holder).bind(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                itemClickListener.onClick(position, mChats.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private static class ChatHolder extends RecyclerView.ViewHolder{
        TextView chatName;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            chatName = itemView.findViewById(R.id.chatName);
        }
        public void bind(String name){
            chatName.setText(name);
        }
    }
}
