package com.GNVS.AlumniApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView chats;
    ChatAdapter mChatAdapter;
    ArrayList<String> userList, keyList, nameList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference().child("users");
    ItemClickListener itemClickListener;
    View appbar;
    ImageButton search;
    ImageView home;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        keyList = new ArrayList<>();
        nameList = new ArrayList<>();
        userList = new ArrayList<>();
        appbar = findViewById(R.id.appbar);
        search = appbar.findViewById(R.id.messageBtn);
        search.setImageResource(R.drawable.ic_baseline_search_24);
        home = appbar.findViewById(R.id.appbarLogo);
        home.setImageResource(R.drawable.ic_baseline_home_24);
        TypedValue v = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, v, true);
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, v.resourceId));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnHome();
            }
        });
        sharedPreferences = getSharedPreferences("ThisUser", Context.MODE_PRIVATE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchContacts();
            }
        });
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int position, String value) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("key", keyList.get(position));
                i.putExtra("user", userList.get(position));
                i.putExtra("name", nameList.get(position));
                startActivity(i);
            }
        };
        chats = findViewById(R.id.chatRecycler);
        mChatAdapter = new ChatAdapter(this, nameList, userList, itemClickListener);
        chats.setLayoutManager(new LinearLayoutManager(this));
        chats.setAdapter(mChatAdapter);
        addUser();
    }

    public void returnHome(){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    public void addUser(){
        DatabaseReference ref = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("messages");
        Query q = ref.orderByChild("time");
        keyList.clear();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(!keyList.contains(dataSnapshot.getKey())){
                    keyList.add(dataSnapshot.getKey());}
                }
                decode();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void decode(){
        for(String key:keyList){
            String[] s = key.split(":");
            nameList.add(s[0]);
            userList.add(s[1]);
        }
        chats.setAdapter(mChatAdapter);
    }

    public void searchContacts(){
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }

    @Override
    public void onBackPressed() {
        return;
    }
}