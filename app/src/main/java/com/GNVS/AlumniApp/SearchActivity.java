package com.GNVS.AlumniApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    SearchView searchView;
    ArrayList<String> nameList;
    ArrayList<DataSnapshot> dataList;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        nameList = new ArrayList<>();
        dataList = new ArrayList<>();
        listView = findViewById(R.id.searchName);
        searchView = findViewById(R.id.searchText);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query q = ref.orderByChild("name");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    dataList.add(dataSnapshot);
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(Objects.equals(dataSnapshot1.getKey(), "name")){
                            nameList.add(dataSnapshot1.getValue(String.class));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (nameList.contains(s)) {
                    adapter.getFilter().filter(s);
                }
                else {
                    // Search query not found in List View
                    Toast.makeText(SearchActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(DataSnapshot snap:dataList){
                    for(DataSnapshot s:snap.getChildren()){
                        if(Objects.equals(s.getKey(), "name")){
                            if(Objects.equals(s.getValue(String.class), adapterView.getItemAtPosition(i))){
                                Intent intent = new Intent(SearchActivity.this, ChatActivity.class);
                                intent.putExtra("user", snap.getKey());
                                intent.putExtra("name", s.getValue(String.class));
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }
}