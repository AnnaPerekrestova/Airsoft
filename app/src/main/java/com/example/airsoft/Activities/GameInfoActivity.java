package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.airsoft.Adapters.GamesAdapter;
import com.example.airsoft.Adapters.MemberTeamGameAdapter;
import com.example.airsoft.Classes.GamesClass;
import com.example.airsoft.Classes.MemberTeamClass;
import com.example.airsoft.Classes.MembersClass;
import com.example.airsoft.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameInfoActivity extends AppCompatActivity {
    private RecyclerView rv;
    Intent intent;
//    String game_id = i.getStringExtra("game_id");
    String member_team_id;
    String game_id;
    String used_teams_string;
    String[] used_teams;
    public MemberTeamGameAdapter adapter;
    public List<MemberTeamClass> member_team_list =new  ArrayList<>();

    //String[] membersteam1 = { "player1", "player2", "player5"};
    //String[] membersteam2 = { "player3", "player4", "player6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        rv = findViewById(R.id.game_info_teams);
        intent = getIntent();
        game_id= intent.getStringExtra("game_id");
        member_team_id = intent.getStringExtra("member_team_id");
        used_teams_string = intent.getStringExtra("used_teams");
        used_teams = used_teams_string.split("[,\\]\\[]");


        adapter = new MemberTeamGameAdapter(used_teams, member_team_id);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL,false);;
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        //GetMembersOfUsedTeams();
        //Log.i("mt",""+member_team_list.toString());
    }




    private void FillList(String member, String team) {


        adapter.notifyDataSetChanged();
    }




}
