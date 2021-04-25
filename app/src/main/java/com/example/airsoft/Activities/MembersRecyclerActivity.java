package com.example.airsoft.Activities;


import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.MembersAdapter;
import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MembersRecyclerActivity extends AppCompatActivity {
    private List<PlayerClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    FirebaseData fbData = FirebaseData.getInstance();
    String thisTeamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_members_recycler);

        //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        if (intent.getStringExtra("teamKey")==null){
            fbData.getTeamKey(new FirebaseData.teamCallback() {
                @Override
                public void onTeamIdChanged(String teamKey) {
                    thisTeamKey=teamKey;
                    addToMembersRecycler();
                }

                @Override
                public void onTeamNameChanged(String teamName) {}
            });
        }
        else {
            thisTeamKey = intent.getStringExtra("teamKey");
            addToMembersRecycler();
        }

        recyclerView = findViewById(R.id.recycler_view_members);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        mAdapter = new MembersAdapter(membersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlayerClass selectedMember = membersList.get(position);
//                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();

                String personUID = (String) selectedMember.getPlayerUID();
//                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(".PlayerInfo");
                intent.putExtra("playerID", personUID);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }
    public void addToMembersRecycler(){
        fbData.getTeamMembersData(new FirebaseData.teamMembersDataCallback(){

            @Override
            public void onTeamMemberDataChanged(String playerUID, String nickname, String fio) {
                addRow(playerUID, nickname, fio);
            }

        },thisTeamKey);
    }

    private void addRow(String playerUID, String nick, String fio ) {
        PlayerClass player = new PlayerClass( playerUID );
        player.setPlayerUID(playerUID);
        player.setNickname(nick);
        player.setFio(fio);
        membersList.add(player);
        mAdapter.notifyDataSetChanged();
    }
    public void addNewMember(View view) {
        Intent i = new Intent(".NewMemberActivity");
        startActivity(i);
        finish();
    }

    public void requestsToMyTeam(View view){
        Intent i = new Intent(".RequestsToMyTeam");
        startActivity(i);
    }

}
