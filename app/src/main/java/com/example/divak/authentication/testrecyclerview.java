package com.example.divak.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class testrecyclerview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrecyclerview);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("Department")){
           String x=getIntent().getStringExtra("Department");
            Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();
        }
    }
}
