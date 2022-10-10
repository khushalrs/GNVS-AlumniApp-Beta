package com.GNVS.AlumniApp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CommentFragment extends Fragment {
    String ref1, ref2;
    DatabaseReference data1, data2;
    RecyclerView commentRecycler;
    EditText commentText;
    ImageButton commentPost;
    ArrayList<Comments> commentList = new ArrayList<>();
    Context c;
    CommentAdapter mCommentAdapter;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ref1 = getArguments().getString("ref1");
            ref2 = getArguments().getString("ref2");
        }
        data1 = FirebaseDatabase.getInstance().getReferenceFromUrl(ref1);
        data2 = FirebaseDatabase.getInstance().getReferenceFromUrl(ref2);
        getComment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommentAdapter = new CommentAdapter(commentList);
        commentRecycler.setLayoutManager(new LinearLayoutManager(c));
        commentRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        c = container.getContext();
        View v = inflater.inflate(R.layout.fragment_comment, container, false);
        commentRecycler = v.findViewById(R.id.commentRecycler);
        commentText = v.findViewById(R.id.edit_comment_message);
        commentPost = v.findViewById(R.id.button_comment_send);
        commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
        ImageButton back = v.findViewById(R.id.commentBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressed();
            }
        });
        return v;
    }

    public void getComment(){
        data1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("comment")){
                    for(DataSnapshot dataSnapshot:snapshot.child("comment").getChildren()){
                        Comments c = dataSnapshot.getValue(Comments.class);
                        commentList.add(c);
                    }
                }
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void postComment(){
        String comment = commentText.getText().toString();
        Log.i("Comment Text", comment);
        if(!comment.isEmpty()){
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference d1 = data1.child("comment").push();
            DatabaseReference d2 = data2.child("comment").push();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String strTime = timeFormat.format(calendar.getTime());
            String strDate = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date());
            String dateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dateTime = dateTime + " " + strTime;
            Comments comments = new Comments(u.getUid(),
                    getActivity().getSharedPreferences("ThisUser", Context.MODE_PRIVATE).getString("UserName", ""),
                    comment, strDate, strTime, dateTime);
            d1.setValue(comments);
            d2.setValue(comments);
            commentText.setText("");
            Toast.makeText(c, "Comment Posted", Toast.LENGTH_SHORT).show();
        }
    }

    public void backPressed(){
        getParentFragmentManager().popBackStack();
    }

}