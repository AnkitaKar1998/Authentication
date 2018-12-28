package com.example.divak.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //declaration
    Button btnSignIn,btnReset;
    //Button btnSignUp;
    EditText etEmail,etPassword;
    RadioGroup loginAs;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference RootRef;
    private DatabaseReference tearef;
    String CurrentUserId,department;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;




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

        sharedPreferences=getSharedPreferences("mydata",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        //firebase path
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
                            CurrentUserId =firebaseAuth.getCurrentUser().getUid();
                            Log.d("msg","currentUser:"+ CurrentUserId);
                            Query userQuery = RootRef.child("users").child(CurrentUserId);
                            userQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String type = dataSnapshot.child("type").getValue(String.class);
                                    Log.d("msg","type="+type);
                                    if(type.equals("teacher")){
                                        //Go to TeacherMemberActivity
                                        Intent i=new Intent(MainActivity.this,TeacherActivity.class);
                                        startActivity(i);
                                    } else {
                                        //Go to NormalMemberActivity
                                        department=dataSnapshot.child("department").getValue(String.class);
                                        Intent i=new Intent(MainActivity.this,ChatActivity.class);
                                        i.putExtra("Department",department);
                                        startActivity(i);
                                    }
                                    editor.putString("type",type);
                                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
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
            if(sharedPreferences.getString("type","").equals("teacher")){
                //Go to TeacherMemberActivity
                Intent i=new Intent(MainActivity.this,TeacherActivity.class);
                startActivity(i);
            } else {
                //Go to NormalMemberActivity
                Intent i=new Intent(MainActivity.this,ChatActivity.class);
                i.putExtra("Department",department);
                startActivity(i);
            }
            finish();
        }
    }
}
