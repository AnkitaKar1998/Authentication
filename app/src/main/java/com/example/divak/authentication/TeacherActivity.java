package com.example.divak.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class TeacherActivity extends AppCompatActivity {
   // Button btnLogout;

    RecyclerView recyclerview;
    String[] i = {"Class 1","Class 2","Class3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        recyclerview = (RecyclerView) findViewById(R.id.rcv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new RecyclerViewAdapter(this,i));
    }
}
