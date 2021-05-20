package com.example.airsoft.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.airsoft.Adapters.StatisticAdapter;
import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<PlayerClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StatisticAdapter statsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        recyclerView = findViewById(R.id.stats_recycler);
        statsAdapter = new StatisticAdapter(membersList);
        RecyclerView.LayoutManager statsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(statsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(statsAdapter);

        addToMembersTable();

        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                thisTeamKey=teamKey;
                getStatistic(thisTeamKey);
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        },fbData.getUserUID());




    }

    public void getStatistic(String teamKey){
        fbData.countTeamStatistic(new FirebaseData.countTeamStatisticCallback() {
            @Override
            public void countTeamGames(int count) {
                allGames = count;
                fillStatistic(allGames, winPercent);
            }

            @Override
            public void percentOfTeamWins(double percent) {
                winPercent = percent;
                fillStatistic(allGames, winPercent);
            }
        },teamKey);

    }

    @SuppressLint("SetTextI18n")
    private void fillStatistic(int allGames, double winPercent) {
        games = (TextView) findViewById(R.id.statistic_games);
        percent = (TextView) findViewById(R.id.statistic_games_prc);

        winPercent = winPercent*100;
        int percent100 = (int) winPercent;

        games.setText(String.valueOf(allGames));
        percent.setText(String.valueOf(percent100)+"%");
    }


    public void addToMembersTable(){
        fbData.
    }

    private void addRow(String playerUID, String fio,String statsGames,String statsPercent) {
        PlayerClass member = new PlayerClass(playerUID );
        member.setFio(fio);
        member.setStatisticGames(statsGames);
        member.setStatisticPercent(statsPercent);
        membersList.add(member);

        statsAdapter.notifyDataSetChanged();
    }

}
