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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeacherActivity extends AppCompatActivity {
   // Button btnLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView recyclerview;
    String[] i = {"IT","comps","EXTC"};

    TextView itdep,comps,extc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

       // btnLogout = (Button)findViewById(R.id.btnLogout);
        //tvInfo =(TextView)findViewById(R.id.tvInfo);
        firebaseAuth=FirebaseAuth.getInstance();

//        itdep=findViewById(R.id.it_tv);
//        comps=findViewById(R.id.comps_tv);
//        extc=findViewById(R.id.extc_tv);


        RecyclerViewAdapter.ClickListner clickListner=new RecyclerViewAdapter.ClickListner() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getBaseContext(),ChatActivity.class);
                intent.putExtra("Department",i[position]);
                startActivity(intent);
            }
        };

        recyclerview = (RecyclerView) findViewById(R.id.rcv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new RecyclerViewAdapter(i,this, clickListner));

        String email=firebaseAuth.getCurrentUser().getEmail();
        //tvInfo.setText("Welcome" +email);

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

        if (item.getItemId() == R.id.menuLogout) {
            firebaseAuth.signOut();
            Intent i=new Intent(TeacherActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
