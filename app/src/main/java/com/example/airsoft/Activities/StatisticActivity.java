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
    int allGames;
    double winPercent;
    TextView games;
    TextView percent;
    String memberGames;
    String memberPercent;

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


        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                getStatistic(teamKey);
                addToMembersTable(teamKey);
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        },fbData.getUserUID());

    }

    public void getStatistic(String teamKey){
        fbData.countTeamStatistic(new FirebaseData.countTeamStatisticCallback() {
            @Override
            public void countTeamGames(int games) {
                allGames = games;
                fillStatistic(allGames, winPercent);
            }

            @Override
            public void countPercentOfTeamWins(double percent) {
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


    public void addToMembersTable(final String teamKey){
        fbData.getTeamMembersData(new FirebaseData.teamMembersDataCallback() {
            @Override
            public void onTeamMemberDataChanged(final String playerUID, final String nickname, String fio) {
                fbData.countMemberStatistic(new FirebaseData.countMemberStatisticCallback() {

                    @Override
                    public void countMemberPercent(String games, String percent) {
                        addRow(playerUID,nickname,games,percent);

                    }
                },teamKey,playerUID);
            }
        },teamKey);


    }

    private void addRow(String playerUID, String fio,String statsGames,String statsPercent) {
        for (PlayerClass el: membersList){
            if (el.getPlayerUID().equals(playerUID)){
                membersList.remove(el);
            }
        }

        PlayerClass member = new PlayerClass(playerUID );
        member.setPlayerUID(playerUID);
        member.setNickname(fio);
        member.setStatisticGames(statsGames);
        member.setStatisticPercent(statsPercent);
        membersList.add(member);
        statsAdapter.notifyDataSetChanged();
    }

}
