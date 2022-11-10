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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView mHomeRecycler;
    private HomeAdapter mHomeAdapter;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<PostList> postList;
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
            if (Objects.equals(value, "Profile")) {
                ProfileFragment pf = new ProfileFragment();
                Bundle b = new Bundle();
                b.putString("userId", postList.get(position).getUserId());
                pf.setArguments(b);
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, pf).commit();
            }
            else if(Objects.equals(value, "Comment")){
                CommentFragment cf = new CommentFragment();
                Bundle b = new Bundle();
                b.putString("ref1", postList.get(position).getRef1());
                b.putString("ref2", postList.get(position).getRef2());
                cf.setArguments(b);
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, cf).commit();
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new ArrayList<>();
        //likeList = new ArrayList<>();
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
        mHomeAdapter = new HomeAdapter(c, postList, itemClickListener);
        mHomeRecycler.setLayoutManager(new LinearLayoutManager(c));
        mHomeRecycler.setAdapter(mHomeAdapter);
    }

    public void queryData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("posts");
                //String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                Query q = ref.orderByChild("datetime");
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PostList m = dataSnapshot.getValue(PostList.class);
                            ArrayList<String> likeList = new ArrayList<>();
                            if(dataSnapshot.hasChild("likeId")){
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("likeId").getChildren()){
                                    String val = dataSnapshot1.getKey();
                                    likeList.add(val);
                                }
                            }
                            m.addLikeId(likeList);
                            postList.add(m);
                            Collections.reverse(postList);
                        }
                        mHomeAdapter.notifyDataSetChanged();
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