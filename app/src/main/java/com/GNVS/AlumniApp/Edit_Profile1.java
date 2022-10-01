package com.GNVS.AlumniApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_Profile1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_Profile1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private EditText emailEdit1, passwordEdit1, nameEdit1, companyEdit1, jobEdit1, batchEdit1, phoneEdit1;

    public Edit_Profile1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_Profile1.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_Profile1 newInstance(String param1, String param2) {
        Edit_Profile1 fragment = new Edit_Profile1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emailEdit1 = findViewById(R.id.email_edit1);
        passwordEdit1 = findViewById(R.id.password_edit1);
        nameEdit1 = findViewById(R.id.name_edit1);
        companyEdit1 = findViewById(R.id.company_edit1);
        jobEdit1= findViewById(R.id.job_edit1);
        batchEdit1 = findViewById(R.id.batch_edit1);
        phoneEdit1 = findViewById(R.id.phone_edit1);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit__profile1, container, false);
        queryData();
        return view;


    }

    public void queryData() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         user = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
}
    public void bind() {
        String email, password, name, job, company, batch, phone;
        email = emailEdit1.getText().toString();
        password = passwordEdit1.getText().toString();
        name = nameEdit1.getText().toString();
        job = jobEdit1.getText().toString();
        company = companyEdit1.getText().toString();
        batch = batchEdit1.getText().toString();
        phone = phoneEdit1.getText().toString();

    }


}