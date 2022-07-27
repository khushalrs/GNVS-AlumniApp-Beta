package com.example.mymessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView mHomeRecycler;
    private HomeAdapter mHomeAdapter;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<PostList> PostList;
    View v;
    View appbar;
    ImageButton messageButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostList = new ArrayList<>();
        database =  FirebaseDatabase.getInstance();
        ref = database.getReference().child("posts");
        queryData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context c = container.getContext();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeRecycler = v.findViewById(R.id.recycler_home);
        appbar = v.findViewById(R.id.appbar);
        messageButton = appbar.findViewById(R.id.messageBtn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(c, MainActivity.class));
            }
        });
        mHomeAdapter = new HomeAdapter(c, PostList);
        mHomeRecycler.setLayoutManager(new LinearLayoutManager(c));
        mHomeRecycler.setAdapter(mHomeAdapter);
        return v;

    }
    public void queryData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("posts");
        //Log.i("Path", ref.toString());
        Query q = ref.orderByChild("dateTime");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostList m = dataSnapshot.getValue(PostList.class);
                    //Log.i("Display",m.getMessageText());
                    PostList.add(m);
                    Log.i("Size", Integer.toString(PostList.size()));
                }
                mHomeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}