package com.example.divak.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //declaration
    Button btnSignIn,btnReset;
    //Button btnSignUp;
    EditText etEmail,etPassword;
    RadioGroup loginAs;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference RootRef;
    String currentUserId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Binding
        btnSignIn =(Button)findViewById(R.id.btnSignIn);
        //btnSignUp =(Button)findViewById(R.id.btnSignUp);
        btnReset =(Button)findViewById(R.id.btnReset);
        etEmail =(EditText)findViewById(R.id.etEmail);
        etPassword =(EditText)findViewById(R.id.etPassword);
        loginAs = (RadioGroup) findViewById(R.id.rg_loginAs);

        //firebase part
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        RootRef = FirebaseDatabase.getInstance().getReference();
     /*   btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });*/

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ResetActivity.class);
                startActivity(i);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int selectedId = loginAs.getCheckedRadioButtonId();
                String e=etEmail.getText().toString();
                String p=etPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            currentUserId =firebaseAuth.getCurrentUser().getUid();
                            RootRef.child("users").child(currentUserId).setValue("");
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            if(selectedId == R.id.rb_teacher) {
                                Intent i=new Intent(MainActivity.this,TeacherChatActivity.class);
                                startActivity(i);
                            } else {
                                Intent i=new Intent(MainActivity.this,StudentActivity.class);
                                startActivity(i);
                            }
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Issue: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if((user!=null))
        {
            Intent i=new Intent(MainActivity.this,TeacherActivity.class);
            startActivity(i);
            finish();
        }
    }
}
