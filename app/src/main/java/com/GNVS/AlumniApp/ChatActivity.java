package com.GNVS.AlumniApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    ArrayList<MessageList> messageList;
    ImageButton mSend, back;
    EditText messageText;
    FirebaseUser currentUser;
    String key,id,user, thisUser;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference ref;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageList = new ArrayList<>();
        Intent i = getIntent();
        if(i.hasExtra("key")){
        key = i.getStringExtra("key");}
        if(i.hasExtra("user")){
        id = i.getStringExtra("user");}
        if(i.hasExtra("name")){
        user = i.getStringExtra("name");}
        key = user + ":" + id;
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        database =  FirebaseDatabase.getInstance();
        ref = database.getReference().child("users");
        mSend = findViewById(R.id.button_gchat_send);
        messageText = findViewById(R.id.edit_gchat_message);
        mMessageRecycler = findViewById(R.id.recycler_gchat);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        mMessageAdapter = new MessageAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        sharedPreferences = getSharedPreferences("ThisUser", Context.MODE_PRIVATE);
        thisUser = sharedPreferences.getString("UserName", "");
        thisUser = thisUser + ":" + currentUser.getUid();
        back = findViewById(R.id.chatBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
            }
        });
        queryData();
    }

    public void queryData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users").child(currentUser.getUid()).child("messages").child(key);
        //Log.i("Path", ref.toString());
        Query q = ref.orderByChild("dateTime");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageList m = dataSnapshot.getValue(MessageList.class);
                    //Log.i("Display",m.getMessageText());
                    messageList.add(m);
                    //Log.i("Size", Integer.toString(messageList.size()));
                }
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void sendMessage(){
        String newMessage = messageText.getText().toString();
        messageText.setText("");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String strTime = timeFormat.format(calendar.getTime());
        String strDate = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date());
        String dateTime = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date());
        dateTime = dateTime + " " + strTime;
        MessageList mMessage = new MessageList(newMessage, currentUser.getEmail(), strTime, strDate, dateTime, 0);
        DatabaseReference mRef = ref.child(currentUser.getUid()).child("messages").child(key).push();
        mRef.setValue(mMessage);
        DatabaseReference sRef = ref.child(id).child("messages").child(thisUser).push();
        sRef.setValue(mMessage);
    }
}