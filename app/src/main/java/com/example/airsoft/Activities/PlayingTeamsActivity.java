package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.airsoft.Adapters.RequestsToGameAdapter;
import com.example.airsoft.Adapters.RequestsToMyTeamAdapter;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.Classes.RequestToGameClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class PlayingTeamsActivity extends AppCompatActivity {


    RequestsToGameAdapter requestsToGameAdapter;
    List<RequestToGameClass> requestsToGameList= new ArrayList<>();
    FirebaseData fbData = FirebaseData.getInstance();
    String gameID;
    String orgcomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_teams);

        Intent intent = getIntent();
        gameID=intent.getStringExtra("gameID");
        orgcomID=intent.getStringExtra("orgcomID");

        fbData.getOrgFlag(new FirebaseData.orgFlagCallback() {
            @Override
            public void onOrgFlagChanged(boolean orgFlag) {
                if (orgFlag){
//                    Если пользователь является организатором игр
                    findViewById(R.id.playing_teams_new_requsts).setVisibility(View.VISIBLE);
                }
                if (!orgFlag){
                    findViewById(R.id.playing_teams_requst_to_play).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onOrgFlagNull(String no_info) {

            }
        });
        RecyclerView recyclerView = findViewById(R.id.playing_teams_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        requestsToGameAdapter = new RequestsToGameAdapter(requestsToGameList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestsToGameAdapter);


        addAllToRecycler();
        updateRecycler();

        addListenerOnButton();
    }

    public void updateRecycler(){
        fbData.getRequestsToGameListIfChanged(new FirebaseData.requestsToGameListIfChangedCallback() {
            @Override
            public void onRequestsToGameListChanged() {
                requestsToGameList.clear();
                addAllToRecycler();
                requestsToGameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onApproveRequestsToGameListChanged(String requestKey, String teamName, boolean payment, String status) {

            }

            @Override
            public void onNewRequestsToMyTeamListChanged(String requestKey, String teamName, boolean payment, String status) {

            }
        },gameID);
    }

    public void addAllToRecycler(){
        fbData.getRequestsToGameListIfChanged(new FirebaseData.requestsToGameListIfChangedCallback() {
            @Override
            public void onRequestsToGameListChanged() {

            }

            @Override
            public void onApproveRequestsToGameListChanged(String requestKey, String teamName, boolean payment, String status) {
                addRow(requestKey, teamName, payment, status);
            }

            @Override
            public void onNewRequestsToMyTeamListChanged(String requestKey, String teamName, boolean payment, String status) {

            }
        }, gameID);
    }

    private void addRow(final String requestKey, final String teamName,final boolean payment, final String status) {
        fbData.getRequestToGameInfo(new FirebaseData.requestToGameInfoCallback() {
            @Override
            public void onRequestToGameInfoChanged(String gameID, String orgcomID, final boolean payment, final String playersCount, final String description, final String status, String teamID, String side) {
                fbData.getGameInfo(new FirebaseData.gameInfoCallback() {
                    @Override
                    public void onGameInfoChanged(String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner, String gameSides) {
                        RequestToGameClass request = new RequestToGameClass(requestKey);
                        request.setRequestKey(requestKey);
                        request.setDescription(description);
                        request.setGameName(gameName);
                        request.setPayment(payment);
                        request.setPlayersCount(playersCount);
                        request.setStatus(status);
                        request.setTeamName(teamName);

                        requestsToGameList.add(request);

                        requestsToGameAdapter.notifyDataSetChanged();
                    }
                },gameID);
            }
        },requestKey);


    }

    public void addListenerOnButton() {
        Button createReq = findViewById(R.id.playing_teams_requst_to_play);
        Button checkReqs = findViewById(R.id.playing_teams_new_requsts);

        createReq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CreatingRequestToGame");
                        i.putExtra("gameID", gameID);
                        i.putExtra("orgcomID", orgcomID);
                        startActivity(i);
                    }
                }

        );
        checkReqs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".Activities.RequestsToGame");
                        i.putExtra("gameID", gameID);
                        startActivity(i);
                    }
                }

        );
    }
}