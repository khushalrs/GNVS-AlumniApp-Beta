package com.GNVS.AlumniApp;

import android.content.Context;
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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder>{

    private ArrayList<PostList> postlist;
    Context c;
    boolean redirected;

    public ProfileAdapter(Context c, ArrayList<PostList> postList, boolean r) {
        postlist = postList;
        this.c = c;
        redirected = r;
    }

    @NonNull
    @Override
    public ProfileAdapter.ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdapter.ProfileHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileHolder holder, int position) {
        PostList p = postlist.get(position);
        if (p.getLikeId().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.like.setImageResource(R.drawable.heart_clicked);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getDrawable().getConstantState() == c.getDrawable(R.drawable.heart_unclicked).getConstantState()) {
                    holder.like.setImageResource(R.drawable.heart_clicked);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                }
                else {
                    holder.like.setImageResource(R.drawable.heart_unclicked);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
        if(!redirected){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef2());
                    r.removeValue();
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef1());
                    r.removeValue();
                }
            });
        }
        holder.bind(p.getName(), p.getDescription(), p.getImage(), p.getLikeId().size(), p.getLikeId());
    }

    @Override
    public int getItemCount() {
        if(postlist.isEmpty()){
            return 0;
        }
        else{
            return postlist.size();
        }
    }

    protected static class ProfileHolder extends RecyclerView.ViewHolder{
        TextView name, description, likeCount, delete;
        ImageView imageView, like, comment;
        ItemClickListener i;
        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.image_title);
            likeCount = itemView.findViewById(R.id.likeCountTxt);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.likeBtn);
            comment = itemView.findViewById(R.id.commentBtn);
            delete = itemView.findViewById(R.id.deletePost);
        }

        public void bind(String title, String des, String img, int lc, ArrayList<String> likes){
            name.setText(title);
            description.setText(des);
            likeCount.setText(Integer.toString(lc));
            if(likes.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                like.setImageResource(R.drawable.heart_clicked);
            }
            if(!Objects.equals(img, "")) {
                Picasso.get().load(img).into(imageView);
            }
            else{
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
