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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.GNVS.AlumniApp.CircleTransform;

import java.util.ArrayList;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private final ArrayList<PostList> posts;
    Context c;
    private final ItemClickListener itemClickListener;

    public HomeAdapter(Context c, ArrayList<PostList> posts, ItemClickListener i) {
        this.posts = posts;
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
        PostList p = posts.get(position);
        if((posts.get(position).getLikeId()).contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.like.setImageResource(R.drawable.heart_clicked);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)holder.like.getTag() == 0) {
                    holder.like.setImageResource(R.drawable.heart_clicked);
                    holder.like.setTag(1);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
                }
                else {
                    holder.like.setImageResource(R.drawable.heart_unclicked);
                    holder.like.setTag(0);
                    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef2()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    r = FirebaseDatabase.getInstance().getReferenceFromUrl(p.getRef1()).child("likeId");
                    r.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("users").child(p.getUserId()).child("propic")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!Objects.equals(snapshot.getValue(String.class), "")){
                            Picasso.get().load(snapshot.getValue(String.class)).transform(new CircleTransform()).into(holder.profileicon);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.bind(p.getName(), p.getDescription(), p.getImage(), p.getLikeId().size(), p.getLikeId());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected static class HomeHolder extends RecyclerView.ViewHolder {
        TextView name, description, likeCount;
        ImageView imageView, like, comment, profileicon;
        ItemClickListener i;
        public HomeHolder(@NonNull View itemView, ItemClickListener i) {
            super(itemView);
            name = itemView.findViewById(R.id.image_title);
            likeCount = itemView.findViewById(R.id.likeCountTxt);
            description = itemView.findViewById(R.id.postname);
            imageView = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.likeBtn);
            comment = itemView.findViewById(R.id.commentBtn);
            profileicon = itemView.findViewById(R.id.profileIcon);
            this.i = i;
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.onClick(getAdapterPosition(), "Profile");
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.onClick(getAdapterPosition(), "Comment");
                }
            });
        }

        public void bind(String title, String des, String img, int lc, ArrayList<String> likes){
            name.setText(title);
            description.setText(des);
            likeCount.setText(Integer.toString(lc));
            if(likes.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                like.setTag(1);
                like.setImageResource(R.drawable.heart_clicked);
            }
            else{
                like.setTag(0);
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
