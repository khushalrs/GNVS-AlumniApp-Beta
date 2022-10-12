package com.GNVS.AlumniApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.GNVS.AlumniApp.CircleTransform;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private ArrayList<Comments> comments;
    String propic;

    public CommentAdapter(ArrayList<Comments> comments){
        this.comments=comments;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        Comments c = comments.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(c.getUserId()).child("propic");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!Objects.equals(snapshot.getValue(String.class), "")) {
                    propic = snapshot.getValue(String.class);
                    Picasso.get().load(propic).transform(new CircleTransform()).into(holder.pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    protected static class CommentHolder extends RecyclerView.ViewHolder {
        TextView name, text, date;
        ImageView pic;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentName);
            text = itemView.findViewById(R.id.commentDescription);
            date = itemView.findViewById(R.id.commentDate);
            pic = itemView.findViewById(R.id.commentImage);
        }
        public void bind(Comments c){
            name.setText(c.getName());
            text.setText(c.getCommentText());
            date.setText(c.getDatetime());
        }
    }
}
