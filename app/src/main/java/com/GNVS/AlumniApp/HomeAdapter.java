package com.GNVS.AlumniApp;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private ArrayList<Posts> posts;
    Context c;
    private ItemClickListener itemClickListener;

    public HomeAdapter(Context c, ArrayList<Posts> posts, ItemClickListener i) {
        this.posts = posts;
        if(!posts.isEmpty()){
        Log.i("Liesfjjvofpspfv", posts.get(3).getLikeId().get(0));}
        this.c = c;
        itemClickListener = i;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        Posts p = posts.get(position);
        Log.i("Position home", Integer.toString(position));
        Log.i("LikeId", posts.get(position).getLikeId().get(0));
        if((posts.get(position).getLikeId()).contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.like.setImageResource(R.drawable.heart_clicked);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getDrawable().getConstantState() == c.getDrawable(R.drawable.heart_unclicked).getConstantState()) {
                    holder.like.setImageResource(R.drawable.heart_clicked);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getPostList().getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getPostList().getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                }
                else {
                    holder.like.setImageResource(R.drawable.heart_unclicked);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getPostList().getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getPostList().getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
        holder.bind(p.getPostList().getName(), p.getPostList().getDescription(), p.getPostList().getImage(), p.getLikeId().size(), p.getLikeId());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected static class HomeHolder extends RecyclerView.ViewHolder {
        TextView name, description, likeCount;
        ImageView imageView, like, comment;
        ItemClickListener i;
        public HomeHolder(@NonNull View itemView, ItemClickListener i) {
            super(itemView);
            name = itemView.findViewById(R.id.image_title);
            likeCount = itemView.findViewById(R.id.likeCountTxt);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.likeBtn);
            comment = itemView.findViewById(R.id.commentBtn);
            this.i = i;
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.onClick(getAdapterPosition(), name.getText().toString());
                }
            });
        }

        public void bind(String title, String des, String img, int lc, ArrayList<String> likes){
            name.setText(title);
            description.setText(des);
            likeCount.setText(Integer.toString(lc));
            //Log.i("LikeId", likes.get(0));
            if(likes.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                like.setImageResource(R.drawable.heart_clicked);
            }
            //Log.i("Image Url", img);
            //imageView.setImageResource(R.drawable.ic_baseline_send_24);
            if(!Objects.equals(img, "")) {
                Picasso.get().load(img).into(imageView);
            }
            else{
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
