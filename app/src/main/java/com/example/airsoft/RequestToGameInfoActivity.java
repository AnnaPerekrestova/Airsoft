package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.airsoft.Adapters.MembersGameAdapter;
import com.example.airsoft.Classes.PlayerClass;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class RequestToGameInfoActivity extends AppCompatActivity {
    private List<PlayerClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersGameAdapter mAdapter;
    FirebaseData fbData = FirebaseData.getInstance();
    String requestID;
    String gamename;
    String teamname;
    String pcount;
    String istatus;
    boolean paymentf;
    String descr;
    String teamkey;
    String[] sidesList;

    Spinner spinnerSides;
    TextView gname;
    TextView tname;
    TextView plcount;
    TextView instatus;
    TextView payments;
    TextView tdescr;

    Button team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_to_game_info);

        Intent intent = getIntent();
        requestID=intent.getStringExtra("requestID");


        getinfo();

        addListenerOnButton();
    }
    public void getinfo(){
        fbData.getRequestToGameInfo(new FirebaseData.requestToGameInfoCallback() {
            @Override
            public void onRequestToGameInfoChanged(String gameID, String orgcomID, boolean payment, String playersCount, String description, String status, final String teamID) {

                recyclerView = findViewById(R.id.recycler_registration);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
                recyclerView.addItemDecoration(new RecyclerViewDecorator(RequestToGameInfoActivity.this, LinearLayoutManager.VERTICAL, 16));

                mAdapter = new MembersGameAdapter(membersList,gameID);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);


                paymentf = payment;
                pcount = playersCount;
                descr = description;
                istatus = status;
                teamkey =teamID;
                fbData.getGameInfo(new FirebaseData.gameInfoCallback() {
                    @Override
                    public void onGameInfoChanged(String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner, String gameSides) {
                        gamename = gameName;
                        sidesList = gameSides.split(",");
                        fbData.getTeamInfo(new FirebaseData.teamInfoCallback() {
                            @Override
                            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription) {
                                teamname = teamName;
                                fillInfo(gamename, teamname, pcount, istatus, paymentf, descr);
                            }
                        }, teamID);


                    }
                },gameID);


                fbData.getTeamMembersData(new FirebaseData.teamMembersDataCallback() {
                    @Override
                    public void onTeamMemberDataChanged(String playerUID, String nickname, String fio) {
                        addRow(playerUID, nickname, fio);
                    }
                },teamID);

            }
        }, requestID);
    }

    @SuppressLint("SetTextI18n")
    public void fillInfo(String gamename, String teamname, String pcount, String istatus, boolean paymentf, String descr){
        gname = (TextView) findViewById(R.id.request_to_game_info_gamename);
        tname = (TextView) findViewById(R.id.request_to_game_info_teamname);
        plcount = (TextView) findViewById(R.id.request_to_game_info_players_count);
        instatus = (TextView) findViewById(R.id.request_to_game_info_status_spinner);
        payments = (TextView) findViewById(R.id.request_to_game_info_payment);
        tdescr = (TextView) findViewById(R.id.request_to_game_info_description);
        spinnerSides = findViewById(R.id.request_to_game_info_side_spinner);


        ArrayAdapter<String> adapterSide = new ArrayAdapter<String>(RequestToGameInfoActivity.this, android.R.layout.simple_spinner_item, sidesList);
        adapterSide.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSides.setAdapter(adapterSide);
        spinnerSidesListener();

        gname.setText(gamename);
        tname.setText(teamname);
        plcount.setText(pcount);
        instatus.setText(istatus);
        tdescr.setText(descr);
        if (paymentf){
            payments.setText("Оплачено");
        }
        if (!paymentf){
            payments.setText("Не оплачено");
        }

    }
    private void addRow(String playerUID, String nick, String fio ) {
        PlayerClass player = new PlayerClass( playerUID );
        player.setPlayerUID(playerUID);
        player.setNickname(nick);
        player.setFio(fio);
        membersList.add(player);
        mAdapter.notifyDataSetChanged();
    }

    public void addListenerOnButton(){
        team  = findViewById(R.id.team_info_btn);
        team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".TeamInfoActivity");
                        i.putExtra("teamKey", teamkey);
                        startActivity(i);
                    }
                });
    }

    public  void spinnerSidesListener(){
        spinnerSides.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String selectedSide = (String) parent.getItemAtPosition(position);
                fbData.changeRequestToGameSide(requestID,selectedSide);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}