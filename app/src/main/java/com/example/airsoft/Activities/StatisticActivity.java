package com.example.airsoft.Activities;

import android.mtp.MtpConstants;
import android.os.Bundle;

import com.example.airsoft.Adapters.MembersAdapter;
import com.example.airsoft.Adapters.StatsAdapter;
import com.example.airsoft.Classes.MembersClass;
import com.example.airsoft.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {
    private List<MembersClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StatsAdapter statsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        recyclerView = (RecyclerView) findViewById(R.id.stats_recycler);
        statsAdapter = new StatsAdapter(membersList);
        RecyclerView.LayoutManager statsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(statsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(statsAdapter);
        add_to_members_table();


    }



    public void add_to_members_table(){
        final List<String> membersNicksList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    if (postSnapShot.child("Actually").getValue().toString().equals("1")) {
                        membersNicksList.add(postSnapShot.getKey());
                    }
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
                    int played = Integer.parseInt  (dataSnapshot2.child("Played").getValue().toString()) ;
                    int won = Integer.parseInt  (dataSnapshot2.child("Won").getValue().toString()) ;
                    double percent = Math.round(((double)won/(double) played)*100) ;
                    int percent_int = (int) percent;
                    String per_str = Integer.toString(percent_int);
                    addRow(nick,per_str);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }
    private void addRow(String nick_from_base, String percent_str ) {
        MembersClass member = new MembersClass(nick_from_base );
        member.setStatistic(percent_str);
        membersList.add(member);

        statsAdapter.notifyDataSetChanged();
    }

}
