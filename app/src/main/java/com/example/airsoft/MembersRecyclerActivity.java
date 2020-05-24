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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MembersRecyclerActivity extends AppCompatActivity {
    private List<MembersClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_members_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_members);

        mAdapter = new MembersAdapter(membersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MembersClass selected_member = membersList.get(position);
                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();

                String id_member = (String) selected_member.getNickname();
                Intent intent = new Intent(".MemberInfo");
                intent.putExtra("id_m", id_member);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        add_to_members_table();
        //addListenerOnButton();
    }
    public void add_to_members_table(){
        final List<String> membersNicksList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    membersNicksList.add(postSnapShot.getKey());
                }
                SetData(databaseRef, membersNicksList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userNicksList) {
        for (final String nick: userNicksList){
            databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {

                    if(dataSnapshot2==null)return;
                    String fio =  (String)dataSnapshot2.child("FIO").getValue();
                    addRow(nick,fio);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }
    private void addRow(String nick_from_base, String fio_from_base ) {
        MembersClass member = new MembersClass(nick_from_base, fio_from_base);
        membersList.add(member);

        mAdapter.notifyDataSetChanged();
    }
    public void addNewMember(View view) {
        Intent i = new Intent(".PersonActivity");
        startActivity(i);
        finish();
    }


}
