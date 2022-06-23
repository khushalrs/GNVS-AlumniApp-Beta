package com.example.mymessage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    ArrayList<MessageList> messageList;
    Button mSend;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageList = new ArrayList<>();
        setContentView(R.layout.activity_main);
        mSend = findViewById(R.id.button_gchat_send);
        messageText = findViewById(R.id.edit_gchat_message);
        mMessageRecycler = findViewById(R.id.recycler_gchat);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        mMessageAdapter = new MessageAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        addData();
    }

    public void addData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.i("Flow","addData function");
        DatabaseReference myRef = database.getReference().child("messages");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                //Log.i("Flow", Long.toString(dataSnapshot.getChildrenCount()));
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //Log.i("Flow", Long.toString(snapshot.getChildrenCount()));
                    MessageList newMessage = snapshot.getValue(MessageList.class);
                    //Log.i("Flow", newMessage.getMessageText());
                    messageList.add(newMessage);
                    Log.i("Size", Integer.toString(messageList.size()));
                }
                mMessageAdapter.notifyDataSetChanged();
                //mMessageRecycler.setLayoutManager(new LinearLayoutManager(this))

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void sendMessage(){
        String newMessage = messageText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        MessageList mMessage = new MessageList(newMessage, "User3", strDate, 1);
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("messages").child("message 9");
        mRef.setValue(mMessage);
    }
}