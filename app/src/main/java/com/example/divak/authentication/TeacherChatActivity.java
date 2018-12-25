package com.example.divak.authentication;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeacherChatActivity extends AppCompatActivity {

    Button sendButton;
    EditText message;
    LinearLayout teacherChatSection;

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
            }
        });

    }
}
