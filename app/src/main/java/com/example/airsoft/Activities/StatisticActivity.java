package com.example.airsoft.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.airsoft.Adapters.StatsAdapter;
import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    FirebaseData fbData = FirebaseData.getInstance();
    String thisTeamKey;
    int allGames;
    double winPercent;
    TextView games;
    TextView percent;

//    private List<PlayerClass> membersList = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private StatsAdapter statsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

//        recyclerView = (RecyclerView) findViewById(R.id.stats_recycler);
//        statsAdapter = new StatsAdapter(membersList);
//        RecyclerView.LayoutManager statsLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(statsLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(statsAdapter);
//
//        add_to_members_table();

        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                thisTeamKey=teamKey;
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        },fbData.getUserUID());

        getStatistic(thisTeamKey);


    }

    public void getStatistic(String teamKey){
        fbData.countTeamStatistic(new FirebaseData.countTeamStatisticCallback() {
            @Override
            public void countTeamGames(int count) {
                allGames = count;
            }

            @Override
            public void percentOfTeamWins(double percent) {
                winPercent = percent;
            }
        },teamKey);
        fillStatistic(allGames, winPercent);
    }

    @SuppressLint("SetTextI18n")
    private void fillStatistic(int allGames, double winPercent) {
        games = (TextView) findViewById(R.id.statistic_games);
        percent = (TextView) findViewById(R.id.statistic_games_prc);

        games.setText(String.valueOf(allGames));
        percent.setText(String.valueOf(winPercent));
    }


//    public void add_to_members_table(){
//        final List<String> membersNicksList = new ArrayList<>();
//        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
//        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot==null)return;
//                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
//                    if (postSnapShot.child("Actually").getValue().toString().equals("1")) {
//                        membersNicksList.add(postSnapShot.getKey());
//                    }
//                }
//                SetData(databaseRef, membersNicksList);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Error
//                Log.d("Error", "databaseError");
//            }
//        });
//    }
//
//    private void SetData(DatabaseReference databaseRef, List<String> userNicksList) {
//        for (final String nick: userNicksList){
//            databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot2) {
//
//                    if(dataSnapshot2==null)return;
//                    int played = Integer.parseInt  (dataSnapshot2.child("Played").getValue().toString()) ;
//                    String games = Integer.toString(played);
//                    addRow(nick, games);
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Error
//                    Log.d("Error", "databaseError");
//                }
//            });
//        }
//    }
//    private void addRow(String nick_from_base, String percent_str ) {
//        PlayerClass member = new PlayerClass(nick_from_base );
//        member.setStatistic(percent_str);
//        membersList.add(member);
//
//        statsAdapter.notifyDataSetChanged();
//    }

}
