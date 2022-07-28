package com.example.mymessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private EventAdapter eventAdapter;
    ArrayList<PostList> PostList = new ArrayList<>();
    private RecyclerView recycler;
    FirebaseDatabase database;
    DatabaseReference ref;
    Context thisContext;
    View appbar;
    ImageButton messageButton;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("users").child("hBVmq138IecWdstkYKcG0cgLwHp1").child("posts");
        addData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recycler = view.findViewById(R.id.event);
        appbar = view.findViewById(R.id.appbar);
        messageButton = appbar.findViewById(R.id.messageBtn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(thisContext, MainActivity.class));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(thisContext);
        recycler.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(thisContext, PostList);
        recycler.setAdapter(eventAdapter);
        return view;
    }

    public void addData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("EventPath", dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("EventPath", snapshot.getKey());
                    PostList p = snapshot.getValue(PostList.class);
                    PostList.add(p);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

