package com.example.divak.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    Button  btnSendLink;
    EditText etResetEmail;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        btnSendLink = (Button)findViewById(R.id.btnSendLink);
        etResetEmail = (EditText)findViewById(R.id.etResetEmail);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=etResetEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ResetActivity.this, "Check email", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(ResetActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ResetActivity.this, "Galat Baat", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
