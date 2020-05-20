package com.example.airsoft;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MembersRecyclerActivity extends AppCompatActivity {
    private List<MembersClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_members_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_content);

        mAdapter = new MembersAdapter(membersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        add_to_members_table();
        //addListenerOnButton();
    }
    public void add_to_members_table(){
        final List<String> userIdList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Team");
        databaseRef.child("members_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    userIdList.add(postSnapShot.getKey());
                }
                SetData(databaseRef, userIdList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userIdList) {
        for (String i: userIdList){
            databaseRef.child("members_id").child(i).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {

                    if(dataSnapshot2==null)return;
                    String fio =  (String)dataSnapshot2.child("FIO").getValue();
                    String nick = (String)dataSnapshot2.child("Nickname").getValue();

                    addRow(fio,nick);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }

    private void addRow(String fio_from_base, String nick_from_base ) {
        MembersClass member = new MembersClass(fio_from_base, nick_from_base);
        membersList.add(member);

        mAdapter.notifyDataSetChanged();
    }
//    public void addListenerOnButton() {
//        Button buttonAddPerson = findViewById(R.id.members_fab);
//        buttonAddPerson.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(".PersonActivity");
//                        startActivity(i);
//                        finish();
//                    }
//                }
//        );
//    }


    public void addNewMember(View view) {
        Intent i = new Intent(".PersonActivity");
        startActivity(i);
        //finish();
    }
}
