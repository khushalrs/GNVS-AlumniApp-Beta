package com.GNVS.AlumniApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{

    private ArrayList<String> mChats;
    private ItemClickListener itemClickListener;
    private ArrayList<String> userList;
    public ChatAdapter(){}

    public ChatAdapter(Context context, ArrayList<String> nameList, ArrayList<String> userList, ItemClickListener listener){
        mChats = nameList;
        this.userList = userList;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatAdapter.ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position) {
        String name = mChats.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userList.get(position)).child("propic");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!Objects.equals(snapshot.getValue(String.class), "")) {
                    Picasso.get().load(snapshot.getValue(String.class)).transform(new CircleTransform()).into(holder.propic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.bind(name);
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

    protected static class ChatHolder extends RecyclerView.ViewHolder{
        TextView chatName;
        ImageView propic;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            propic = itemView.findViewById(R.id.chatProfile);
            chatName = itemView.findViewById(R.id.chatName);
        }
        public void bind(String name){
            chatName.setText(name);
        }
    }
}
