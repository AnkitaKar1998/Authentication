package com.example.divak.authentication;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherChatActivity extends AppCompatActivity {

    Button sendButton;
    EditText message;
    LinearLayout teacherChatSection;
    private DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat);

        sendButton = findViewById(R.id.send_button);
        message = findViewById(R.id.et_message);
        teacherChatSection = findViewById(R.id.ll_teacher_chat_section);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                message.getText().clear();

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(TeacherChatActivity.this);
                textView.setText(msg);
                textView.setBackgroundColor(Color.parseColor("#f4ccc1"));
                params.setMargins(10,10,10,10);
                params.gravity = Gravity.CENTER;
                textView.setLayoutParams(params);
                teacherChatSection.addView(textView);
                String id=mDatabase.push().getKey();
                mDatabase.child("groups").child("IT").child(id).setValue(msg);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("groups").child("IT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherChatSection.removeAllViews();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String msg=data.getValue(String.class);
                    Log.d("msg","messages in the activity from firebase: "+msg);
                    TextView textView = new TextView(TeacherChatActivity.this);
                    textView.setText(msg);
                    textView.setBackgroundColor(Color.parseColor("#f4ccc1"));
                    textView.setGravity(Gravity.CENTER);
                    teacherChatSection.addView(textView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
