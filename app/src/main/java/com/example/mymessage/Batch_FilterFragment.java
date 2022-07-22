package com.example.mymessage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Batch_FilterFragment extends Fragment {
    RecyclerView batch_recycler;
    Context context;
    FirebaseDatabase database;
    DatabaseReference data;
    ArrayList<String> batches, user;
    ArrayList<OldUser> students;
    BatchAdapter batchAdapter;
    Query ref;
    Spinner dropDown;
    ArrayAdapter<String> dropAdapter;
    ProgressBar progressBar;
    LinearLayout layout;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public Batch_FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        data = database.getReference().child("Old Data");
        ref = data.orderByChild("Graduate");
        batches = new ArrayList<>();
        students = new ArrayList<>();
        user = new ArrayList<>();
        batches.add("Select the graduation year");
        dropAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, batches);
        dropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchAdapter = new BatchAdapter(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = Objects.requireNonNull(container).getContext();
        View v = inflater.inflate(R.layout.fragment_batch__filter, container, false);
        batch_recycler = v.findViewById(R.id.batchRecycler);
        dropDown = v.findViewById(R.id.dropDown);
        progressBar = v.findViewById(R.id.batchProgress);
        progressBar.setVisibility(View.VISIBLE);
        layout = v.findViewById(R.id.batch_layout);
        layout.setVisibility(View.GONE);
        addBatchData();
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    addUserData(batches.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        batch_recycler.setLayoutManager(layoutManager);
        batch_recycler.setAdapter(batchAdapter);
        dropDown.setAdapter(dropAdapter);
    }

    public void addBatchData(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot snapshot:datasnapshot.getChildren()){
                            OldUser olduser = snapshot.getValue(OldUser.class);
                            students.add(olduser);
                            assert user != null;
                            if(!batches.contains(String.valueOf(olduser.getGraduate()))){
                                batches.add(String.valueOf(olduser.getGraduate()));}
                        }
                        dropAdapter.notifyDataSetChanged();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void addUserData(String year){
        if(!Objects.equals(year, "Select the graduation year")){
            for(OldUser oldUser: students){
                if(String.valueOf(oldUser.getGraduate()).equals(year)){
                    user.add(String.valueOf(oldUser.getName()));
                }
            }
            batchAdapter.notifyDataSetChanged();
        }
    }
}