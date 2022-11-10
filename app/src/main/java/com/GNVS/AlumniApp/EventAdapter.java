package com.GNVS.AlumniApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

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
        TextView name, description, date, likeCount;
        ImageView imageView;
        LinearLayout event_bottom;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            event_bottom = itemView.findViewById(R.id.event_bottom);
            event_bottom.setVisibility(View.GONE);
            name = itemView.findViewById(R.id.image_title);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
            date = itemView.findViewById(R.id.eventDate);
            likeCount = itemView.findViewById(R.id.likeCountTxt);
        }
        public void bind(String title, String des, String img, String d){
            name.setText(title);
            description.setText(des);
            date.setText(d);
            //likeCount.setText(lc);
            //Log.i("Image Url", img);
            //imageView.setImageResource(R.drawable.ic_baseline_send_24);
            if(!Objects.equals(img, "")) {
                Picasso.get().load(img).into(imageView);
            }
        }
    }
}
