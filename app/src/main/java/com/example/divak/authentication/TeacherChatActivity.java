package com.example.divak.authentication;

import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.ScrollView;
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
    String Gname;
    private DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat);

        sendButton = findViewById(R.id.send_button);
        message = findViewById(R.id.et_message);
        teacherChatSection = findViewById(R.id.ll_teacher_chat_section);
        if (getIntent().hasExtra("Department")) {
            Intent intent = getIntent();
            Gname = intent.getStringExtra("Department");
        }



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                message.getText().clear();
                TextView textView = new TextView(TeacherChatActivity.this);
                textView.setText(msg);

                setDesign(textView);

                teacherChatSection.addView(textView);

                String id=mDatabase.push().getKey();
                mDatabase.child("groups").child(Gname).child(id).setValue(msg);
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("groups").child(Gname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherChatSection.removeAllViews();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String msg=data.getValue(String.class);
                    Log.d("msg","messages in the activity from firebase: "+msg);
                    TextView textView = new TextView(TeacherChatActivity.this);
                    textView.setText(msg);

                    setDesign(textView);

                    teacherChatSection.addView(textView);
                    final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setDesign(TextView textView) {
        LinearLayout.LayoutParams txtlayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtlayParams.gravity = Gravity.CENTER;
        txtlayParams.setMargins(50,20,50,20);
        textView.setLayoutParams(txtlayParams);
        textView.setPadding(20,20,20,20);
        textView.setTextSize(20);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setBackgroundColor(Color.parseColor("#FFCDDEFF"));
    }
}
