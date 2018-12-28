package com.example.divak.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RecyclerViewActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Listitem> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        firebaseAuth=FirebaseAuth.getInstance();
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
            Intent i=new Intent(RecyclerViewActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        for(int i=0;i<=10;i++){
            Listitem listItem = new Listitem(
                    "Heading" + i+1,
                    "Dummy text"
            );
            listItems.add(listItem);
        }
        adapter = new TnAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
    }
}
