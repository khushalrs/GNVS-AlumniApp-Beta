package com.GNVS.AlumniApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class Edit_Profile1 extends Fragment {
    private User user;
    private EditText emailEdit1, passwordEdit1, nameEdit1, companyEdit1, jobEdit1, batchEdit1, phoneEdit1;
    private Button update;
    String email, password, name, job, company, batch, phone;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    public Edit_Profile1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit__profile1, container, false);
        emailEdit1 = view.findViewById(R.id.email_edit1);
        passwordEdit1 = view.findViewById(R.id.password_edit1);
        nameEdit1 = view.findViewById(R.id.name_edit1);
        companyEdit1 = view.findViewById(R.id.company_edit1);
        jobEdit1 = view.findViewById(R.id.job_edit1);
        batchEdit1 = view.findViewById(R.id.batch_edit1);
        phoneEdit1 = view.findViewById(R.id.phone_edit1);
        update = view.findViewById(R.id.edit_submit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryData();
    }

    public void queryData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Edit profile", snapshot.getKey());
                user = snapshot.getValue(User.class);
                //setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setData(){
        emailEdit1.setText(user.getEmail());
        passwordEdit1.setText(user.getPassword());
        nameEdit1.setText(user.getName());
        jobEdit1.setText(user.getJob());
        companyEdit1.setText(user.getCompany());
        batchEdit1.setText(user.getBatch());
        phoneEdit1.setText(user.getPhone());
    }

    public void updateData(){
        getInputData();
        User u = new User(email, password, name, job, company, batch, phone);
        ref.setValue(u);
        getParentFragmentManager().popBackStack();
    }

    public void getInputData() {
        email = emailEdit1.getText().toString();
        password = passwordEdit1.getText().toString();
        name = nameEdit1.getText().toString();
        job = jobEdit1.getText().toString();
        company = companyEdit1.getText().toString();
        batch = batchEdit1.getText().toString();
        phone = phoneEdit1.getText().toString();

    }
}