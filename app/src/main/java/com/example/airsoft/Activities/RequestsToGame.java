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
import com.example.airsoft.Classes.RequestToGameClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class RequestsToGame extends AppCompatActivity {

    RequestsToGameAdapter requestsToGameAdapter;
    List<RequestToGameClass> requestsToGameList= new ArrayList<>();
    FirebaseData fbData = FirebaseData.getInstance();
    String gameID;
    String orgcomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_to_game);

        Intent intent = getIntent();
        gameID=intent.getStringExtra("gameID");
        orgcomID=intent.getStringExtra("orgcomID");

        RecyclerView recyclerView = findViewById(R.id.requests_to_game_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        requestsToGameAdapter = new RequestsToGameAdapter(requestsToGameList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestsToGameAdapter);


        addNewToRecycler();
        updateRecycler();

    }

    public void updateRecycler(){
        fbData.getRequestsToGameListIfChanged(new FirebaseData.requestsToGameListIfChangedCallback() {
            @Override
            public void onRequestsToGameListChanged() {
                requestsToGameList.clear();
                addNewToRecycler();
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

    public void addNewToRecycler(){
        fbData.getRequestsToGameListIfChanged(new FirebaseData.requestsToGameListIfChangedCallback() {
            @Override
            public void onRequestsToGameListChanged() {

            }

            @Override
            public void onApproveRequestsToGameListChanged(String requestKey, String teamName, boolean payment, String status) {

            }

            @Override
            public void onNewRequestsToMyTeamListChanged(String requestKey, String teamName, boolean payment, String status) {
                addRow(requestKey, teamName, payment, status);
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
}