package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.airsoft.Adapters.MemberTeamGameAdapter;
import com.example.airsoft.R;

public class GameInfoActivity extends AppCompatActivity {
    private RecyclerView rv;
    private MemberTeamGameAdapter adapter;
    String[] membersteam1 = { "player1", "player2", "player5"};
    String[] membersteam2 = { "player3", "player4", "player6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        rv = findViewById(R.id.game_info_teams);

    }



}
