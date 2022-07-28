package com.GNVS.AlumniApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference().child("users");
    DatabaseReference ref = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
    private FirebaseAuth mAuth;
    Context context;
    Button signout;
    View appbar;
    ImageButton messageButton;
    TextView nameText, emailText, batchText;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        queryData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_profile, container, false);
        signout = V.findViewById(R.id.SignOutbtn);
        nameText = V.findViewById(R.id.nameText);
        emailText = V.findViewById(R.id.emailText);
        batchText = V.findViewById(R.id.batchText);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
        appbar = V.findViewById(R.id.appbar);
        messageButton = appbar.findViewById(R.id.messageBtn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });
        // Inflate the layout for this fragment
        context = Objects.requireNonNull(container).getContext();
        return V;


    }


    public void buttonClicked(){


        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(context,SignInActivity.class));
    }

    public void queryData() {


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.i("Profile Path", snapshot.getKey());
                User user = snapshot.getValue(User.class);
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
                batchText.setText(user.getBatch());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}