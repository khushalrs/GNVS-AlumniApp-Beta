package com.GNVS.AlumniApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference().child("users");
    Context context;
    Button messageButton;
    View appbar;
    boolean redirected = false;
    RecyclerView profilePosts;
    ArrayList<PostList> postList = new ArrayList<>();
    ArrayList<String>likeList = new ArrayList<>();
    ProfileAdapter profileAdapter;
    String name, userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
    ImageButton signout;
    TextView nameText, emailText, batchText, jobText, companyText;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            redirected = true;
        }
        queryData();
        postsData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileAdapter = new ProfileAdapter(context, postList, redirected);
        profilePosts.setLayoutManager(new LinearLayoutManager(context));
        profilePosts.setAdapter(profileAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_profile, container, false);
        context = Objects.requireNonNull(container).getContext();
        appbar = V.findViewById(R.id.appbar);
        signout = appbar.findViewById(R.id.messageBtn);
        signout.setImageResource(R.drawable.ic_baseline_logout_24);
        nameText = V.findViewById(R.id.nameText);
        emailText = V.findViewById(R.id.emailText);
        batchText = V.findViewById(R.id.batchText);
        jobText = V.findViewById(R.id.job_Text);
        companyText = V.findViewById(R.id.company_Text);
        messageButton = V.findViewById(R.id.messageButton);
        profilePosts = V.findViewById(R.id.profile_recycler);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
        if(!Objects.equals(userId, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            messageButton.setText("Message");
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageCall();
                }
            });
        }
        else{
            messageButton.setText("Edit Profile");
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Edit_Profile1 ep = new Edit_Profile1();
                    getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, ep).commit();
                }
            });
        }
        return V;
    }

    public void messageCall(){
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra("user", userId);
        i.putExtra("name", name);
        startActivity(i);
    }


    public void buttonClicked(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(context,SignInActivity.class));
    }

    public void queryData() {
        users.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.i("Profile Path", snapshot.getKey());
                User user = snapshot.getValue(User.class);
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
                batchText.setText(user.getBatch());
                jobText.setText(user.getJob());
                companyText.setText(user.getCompany());
                name = user.getName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void postsData(){
        Query q = users.child(userId).child("posts").orderByChild("dateTime");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostList m = dataSnapshot.getValue(PostList.class);
                    likeList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("likeId").getChildren()) {
                        String val = dataSnapshot1.getKey();
                        likeList.add(val);
                    }
                    m.addLikeId(likeList);
                    postList.add(m);
                }
                profileAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}