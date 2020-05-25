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
import android.widget.TextView;

import com.example.airsoft.Adapters.GamesAdapter;
import com.example.airsoft.Adapters.MemberTeamGameAdapter;
import com.example.airsoft.Classes.GamesClass;
import com.example.airsoft.Classes.MemberTeamClass;
import com.example.airsoft.Classes.MembersClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerViewDecorator;
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
    String member_team_id;
    String game_id;
    String used_teams_string;
    public MemberTeamGameAdapter adapter;
    public List<MemberTeamClass> member_team_list =new  ArrayList<>();
    List<List<String>> list_of_teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        rv = findViewById(R.id.game_info_teams);
        intent = getIntent();
        //-----Получаем значения переданные через intent----------------------------------------------------------------
        game_id= intent.getStringExtra("game_id");
        member_team_id = intent.getStringExtra("member_team_id");
        used_teams_string = intent.getStringExtra("used_teams");
//--------Заполняем текст вью соответствующими значениями---------------------------------------------------------------
        String datetime = intent.getStringExtra("date_time");
        TextView dt =findViewById(R.id.GameDataTime);
        dt.setText(datetime);
        String map = intent.getStringExtra("map");
        TextView m =findViewById(R.id.game_map);
        m.setText(map);
        String winner = intent.getStringExtra("winner");
        TextView w =findViewById(R.id.game_winner);
        w.setText(winner);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        rv.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

//-------Создаем адаптер для RecyclerView вида GridLayout с двумя колонками (list_of_teams заполняется в функции GetMembersOfUsedTeams)---------------------------------------------------------------------------
        adapter = new MemberTeamGameAdapter(list_of_teams);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL,false);;
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
//------Заполняем список списков строк (распределение по командам) для адаптера ---------------------------------------------
        GetMembersOfUsedTeams(member_team_id);
    }
//----Получаем список списков строк для получения информации о том, кто в какой команде-------------------------------------------
    private void GetMembersOfUsedTeams(String member_team_id) {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MembersTeams");
        databaseRef.child("id").child(member_team_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) return;
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //String team = postSnapShot.getKey();
                    String members = postSnapShot.getValue().toString();
                    String[] s =members.split("[,\\]\\[]");
                    List<String> list = new ArrayList<>();
                    for (String i: s){
                        if (!i.equals("")){
                            list.add(i);
                        }
                    }

                    list_of_teams.add(list);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });

    }


}
