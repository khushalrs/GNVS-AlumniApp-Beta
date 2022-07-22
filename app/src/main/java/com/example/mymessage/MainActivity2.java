package com.example.mymessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new HomeFragment());
        messageButton = findViewById(R.id.messageBtn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        Log.i("Case","Home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                        break;
                    case R.id.Events:
                        Log.i("Case","Events");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new EventsFragment()).commit();
                        break;
                    case R.id.NewPost:
                        Log.i("Case","NewPost");
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                                .replace(R.id.frame_layout, new NewPostFragment()).commit();
                        break;
                    case R.id.BatchFilter:
                        Log.i("Case","Batch Filter");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Batch_FilterFragment()).commit();
                        break;
                    case R.id.Person:
                        Log.i("Case","Person");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}