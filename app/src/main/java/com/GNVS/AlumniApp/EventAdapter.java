package com.GNVS.AlumniApp;

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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private ArrayList<PostList> postlist;
    public EventAdapter(Context c, ArrayList<PostList> postList){
        postlist = postList;
    }
    @NonNull
    @Override
    public EventAdapter.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventAdapter.EventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventHolder holder, int position) {
        PostList p = postlist.get(position);
        holder.bind(p.getName(), p.getDescription(), p.getImage(), p.getDate());
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    protected static class EventHolder extends RecyclerView.ViewHolder {
        TextView name, description, date;
        ImageView imageView;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.image_title);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
            date = itemView.findViewById(R.id.eventDate);
        }
        public void bind(String title, String des, String img, String d){
            name.setText(title);
            description.setText(des);
            date.setText(d);
            //Log.i("Image Url", img);
            //imageView.setImageResource(R.drawable.ic_baseline_send_24);
            Picasso.get().load(img).into(imageView);
        }
    }
}
