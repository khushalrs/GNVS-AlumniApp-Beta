package com.GNVS.AlumniApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView mHomeRecycler;
    private HomeAdapter mHomeAdapter;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Posts> postList;
    ArrayList<String>likeList;
    View v;
    View appbar;
    Context c;
    ImageButton messageButton;
    ProgressBar pb;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position, String value) {
            ProfileFragment pf = new ProfileFragment();
            Bundle b = new Bundle();
            Log.i("Position Adapter", postList.get(position).getPostList().getUserId());
            Log.i("Value Adapter", value);
            b.putString("userId", postList.get(position).getPostList().getUserId());
            pf.setArguments(b);
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, pf).commit();
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new ArrayList<>();
        likeList = new ArrayList<>();
        database =  FirebaseDatabase.getInstance();
        ref = database.getReference().child("posts");
        queryData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        c = container.getContext();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        pb = v.findViewById(R.id.homeProgress);
        pb.setVisibility(View.VISIBLE);
        mHomeRecycler = v.findViewById(R.id.recycler_home);
        mHomeRecycler.setVisibility(View.INVISIBLE);
        appbar = v.findViewById(R.id.appbar);
        messageButton = appbar.findViewById(R.id.messageBtn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(c, MainActivity.class));
            }
        });
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void queryData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("posts");
                //Log.i("Path", ref.toString());
                Query q = ref.orderByChild("dateTime");
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int t = 0;
                        postList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PostList m = dataSnapshot.getValue(PostList.class);
                            likeList.clear();
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.child("likeId").getChildren()){
                                String val = dataSnapshot1.getKey();
                                //Log.i("Val", val);
                                likeList.add(val);
                            }
                            //Log.i("Display",m.getMessageText());
                            Posts p = new Posts(m, likeList);
                            postList.add(p);
                            if(!postList.get(t).getLikeId().isEmpty()){
                            Log.i("LIkesssss", postList.get(t).getLikeId().get(0));}
                            t++;
                        }
                        mHomeAdapter = new HomeAdapter(c, postList, itemClickListener);
                        mHomeRecycler.setLayoutManager(new LinearLayoutManager(c));
                        mHomeRecycler.setAdapter(mHomeAdapter);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                mHomeRecycler.setVisibility(View.VISIBLE);
                            }
                        },5000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}