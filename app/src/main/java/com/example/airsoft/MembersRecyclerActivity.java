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

                String id_member = (String) selected_member.getMember_id();
                Intent intent = new Intent(".MemberInfo");
                intent.putExtra("id_m", id_member.toString());
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
        final List<String> membersIdList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    membersIdList.add(postSnapShot.getKey());
                }
                SetData(databaseRef, membersIdList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userIdList) {
        for (final String id: userIdList){
            databaseRef.child("members_id").child(id).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {

                    if(dataSnapshot2==null)return;
                    String fio =  (String)dataSnapshot2.child("FIO").getValue();
                    String nick = (String)dataSnapshot2.child("Nickname").getValue();
                    addRow(id,fio,nick);
                }



                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }

    private void addRow(String id_from_base, String fio_from_base, String nick_from_base ) {
        MembersClass member = new MembersClass(id_from_base, fio_from_base, nick_from_base);
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
