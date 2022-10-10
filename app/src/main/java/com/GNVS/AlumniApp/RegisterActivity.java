package com.GNVS.AlumniApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit, nameEdit, companyEdit, jobEdit, batchEdit, phoneEdit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        emailEdit = findViewById(R.id.email_edit);
        passwordEdit = findViewById(R.id.password_edit);
        nameEdit = findViewById(R.id.name_edit);
        companyEdit = findViewById(R.id.company_edit);
        jobEdit = findViewById(R.id.job_edit);
        batchEdit = findViewById(R.id.batch_edit);
        phoneEdit = findViewById(R.id.phone_edit);
    }

    public void registerNewUser(View view) {
        String email, password, name, job, company, batch, phone;
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        name = nameEdit.getText().toString();
        job = jobEdit.getText().toString();
        company = companyEdit.getText().toString();
        batch = batchEdit.getText().toString();
        phone = phoneEdit.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),"Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),"Please enter name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(job)) {
            Toast.makeText(getApplicationContext(),"Please enter job title!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(company)) {
            Toast.makeText(getApplicationContext(),"Please enter company name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(batch)) {
            Toast.makeText(getApplicationContext(),"Please enter batch!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(),"Please enter phone number!!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    User newUser = new User(email, password, name, job, company, batch, phone, "");
                    ref.setValue(newUser);
                    Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}