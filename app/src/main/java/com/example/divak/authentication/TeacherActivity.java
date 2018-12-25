package com.example.divak.authentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeacherActivity extends AppCompatActivity {
   // Button btnLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView recyclerview;
    String[] i = {"Class 1","Class 2","Class3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

       // btnLogout = (Button)findViewById(R.id.btnLogout);
        firebaseAuth=FirebaseAuth.getInstance();


        String email=firebaseAuth.getCurrentUser().getEmail();

        recyclerview = (RecyclerView) findViewById(R.id.rcv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new RecyclerViewAdapter(this,i));

    }
    //Inflate The Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dot, menu);
        return true;
    }

    //Menu Item Selected Listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuCreateGroup) {
            Toast.makeText(this, "Developed By Divakar Pandey", Toast.LENGTH_SHORT).show();
            RequestNewGroup();
        }

        if (item.getItemId() == R.id.menuLogout) {
            firebaseAuth.signOut();
            Intent i=new Intent(TeacherActivity.this,MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherActivity.this,R.style.AlertDialog);
        builder.setTitle("Create a New Group");
        final EditText groupNameField = new EditText(TeacherActivity.this);
        groupNameField.setHint("eg. Friends");
        builder.setView(groupNameField);
        builder.setCancelable(false);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(TeacherActivity.this, "Enter Message First ...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad=builder.create();
        ad.show();
    }






    private void CreateNewGroup() {

    }
}
