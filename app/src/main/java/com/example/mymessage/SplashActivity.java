package com.example.mymessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        handler = new Handler();
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("User", String.valueOf(currentUser));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser!=null){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("User", currentUser);
                    startActivity(i);
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                }
            }
        }, 3000);

    }
}