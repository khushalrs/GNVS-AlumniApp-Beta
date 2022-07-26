package com.example.mymessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter {

    private ArrayList<PostList> postlist;

    public HomeAdapter(Context c, ArrayList<PostList> postList) {
        postlist = postList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostList p = postlist.get(position);
        ((HomeHolder) holder).bind(p.getName(), p.getDescription(), p.getImage());
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    private static class HomeHolder extends RecyclerView.ViewHolder{
        TextView name, description;
        ImageView imageView;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.image_title);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
        }
        public void bind(String title, String des, String img){
            name.setText(title);
            description.setText(des);
            Picasso.get().load(img).fit().centerCrop().into(imageView);
        }
    }
}
