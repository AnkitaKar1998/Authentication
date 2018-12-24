package com.example.divak.authentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {
   // Button btnLogout;
    TextView tvInfo;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

       // btnLogout = (Button)findViewById(R.id.btnLogout);
        tvInfo =(TextView)findViewById(R.id.tvInfo);
        firebaseAuth=FirebaseAuth.getInstance();


        String email=firebaseAuth.getCurrentUser().getEmail();
        tvInfo.setText("Welcome" +email);

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
            Intent i=new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(WelcomeActivity.this,R.style.AlertDialog);
        builder.setTitle("Create a New Group");
        final EditText groupNameField = new EditText(WelcomeActivity.this);
        groupNameField.setHint("eg. Friends");
        builder.setView(groupNameField);
        builder.setCancelable(false);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(WelcomeActivity.this, "Enter Message First ...", Toast.LENGTH_SHORT).show();
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
