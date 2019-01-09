package com.example.divak.authentication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecyclerViewActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ArrayList<String> teacherIdList =new ArrayList<>();
    ArrayList<String> teachersNameList =new ArrayList<>();
    ArrayList<Listitem> listItems=new ArrayList<>();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    Context context;
    TnAdapter.ClickListner clickListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        firebaseAuth=FirebaseAuth.getInstance();
        context=RecyclerViewActivity.this;
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clickListner=new TnAdapter.ClickListner() {
            @Override
            public void onChatClick(int position) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("teacherId", teacherIdList.get(position));
                startActivity(intent);
            }
        };

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
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        teachersNameList.clear();
//        teacherIdList.clear();
//        teachermsg.clear();
//        listItems.clear();
//        Log.d("msg","Department in rec on start:"+getSharedPreferences("mydata",MODE_PRIVATE).getString("Department",""));
//        reference.child("groups").child(getSharedPreferences("mydata",MODE_PRIVATE).getString("Department","")).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                int j=0;
//                for (DataSnapshot data: dataSnapshot.getChildren()){
//                    String key=data.getKey();
//                    teacherIdList.add(key);
//
//                    Iterator iterator=dataSnapshot.child(data.getKey()).getChildren().iterator();
//                    ArrayList<String> messages=new ArrayList<>();
//                    while (iterator.hasNext()){
//                        messages.add((String) ((DataSnapshot)iterator.next()).child("msg").getValue());
//                    }
//                    ModelForMessage modelForMessage=new ModelForMessage();
//                    modelForMessage.setMsg(messages);
//                    teachermsg.add(modelForMessage);
//
//                    if (!key.equals("")){
//                        Log.d("msg","in if");
//                        Query userQuery = FirebaseDatabase.getInstance().getReference("users")
//                                .child(key);
//                        userQuery.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Log.d("msg","getting name: "+dataSnapshot.child("name").getValue(String.class));
//                                teachersNameList.add(dataSnapshot.child("name").getValue(String.class));
//                                Log.d("msg","names: "+teachersNameList);
//                                if(teachersNameList.size()==teacherIdList.size()){
//                                    for(int i=0;i<=teacherIdList.size()-1;i++){
//
//                                        Listitem listItem = new Listitem(
//                                                teachersNameList.get(i), teachermsg.get(i).getMsg().get(teachermsg.get(i).getMsg().size()-1)
//                                        );
//                                        listItems.add(listItem);
//                                        adapter = new TnAdapter(listItems,context,clickListner);
//                                        recyclerView.setAdapter(adapter);
//                                    }
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                    j++;
//
//                    Log.d("msg","j value:"+j);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    @Override
    protected void onStart() {
        super.onStart();
        teacherIdList.clear();
        teachersNameList.clear();
        listItems.clear();
        reference.child("groups").child(getSharedPreferences("mydata",MODE_PRIVATE).getString("Department",""))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String teacherName= data.child("teacherName").getValue(String.class);
                    String teacherId=data.child("teacherId").getValue(String.class);
                    ModelForMessage modelForMessage=data.getValue(ModelForMessage.class);
                    Log.d("msg","data:"+modelForMessage.getMsg().get(0).getMsg());
                    teacherIdList.add(teacherId);
                    teachersNameList.add(teacherName);
                    Listitem listItem = new Listitem(
                            teacherName,"");
                    listItems.add(listItem);
                }
                adapter = new TnAdapter(listItems,context,clickListner);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
