package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.GamesAdapter;
import com.example.airsoft.Adapters.PolygonsAdapter;
import com.example.airsoft.Classes.GameClass;
import com.example.airsoft.Classes.PolygonClass;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class GamesListActivity extends AppCompatActivity {
    private List<GameClass> gamesList = new ArrayList<>();
    private GamesAdapter gamesAdapter;
    FirebaseData fbData = FirebaseData.getInstance();
    String listType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        Intent intent = getIntent();
        listType=intent.getStringExtra("listType");


        RecyclerView recyclerView = findViewById(R.id.games_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        gamesAdapter = new GamesAdapter(gamesList);
        RecyclerView.LayoutManager polygonsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(polygonsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gamesAdapter);


        addToGamesRecycler();
        updateRecycler();


//        if (listType.equals("orgcomPlaning")){
//
//        }
    }
    public void updateRecycler(){

        fbData.gamesOfOrgcom(new FirebaseData.gamesOfOrgcomCallback() {
            @Override
            public void onOpeningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) { }
            @Override
            public void onClosedGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) { }
            @Override
            public void onRunningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) { }
            @Override
            public void onHappensGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner) { }
            @Override
            public void onCancelledGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) { }

            @Override
            public void onOpeningGamesOfOrgcomChanged() {
                if (listType.equals("orgcomPlaning")){
                    gamesList.clear();
                    addToGamesRecycler();
                    gamesAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onClosedGamesOfOrgcomChanged() {

            }

            @Override
            public void onRunningGamesOfOrgcomChanged() {

            }

            @Override
            public void onHappensGamesOfOrgcomChanged() {

            }

            @Override
            public void onCancelledGamesOfOrgcomChanged() {

            }
        });
    }
    public void addToGamesRecycler(){
        fbData.gamesOfOrgcom(new FirebaseData.gamesOfOrgcomCallback() {
            @Override
            public void onOpeningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) {
                if (listType.equals("orgcomPlaning")){
                    addRow(gameKey, orgcomID, gameName, gameDate, polygonID, gameStatus, gameDescription,null);
                }
            }

            @Override
            public void onClosedGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) {

            }

            @Override
            public void onRunningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) {

            }

            @Override
            public void onHappensGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner) {

            }

            @Override
            public void onCancelledGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription) {

            }

            @Override
            public void onOpeningGamesOfOrgcomChanged() {

            }

            @Override
            public void onClosedGamesOfOrgcomChanged() {

            }

            @Override
            public void onRunningGamesOfOrgcomChanged() {

            }

            @Override
            public void onHappensGamesOfOrgcomChanged() {

            }

            @Override
            public void onCancelledGamesOfOrgcomChanged() {

            }
        });
    }

    private void addRow(String gameKey, String orgcomID, String gameName, String gameDate,
                        String polygonID, String gameStatus, String gameDescription, String gameWinner) {
        GameClass game = new GameClass(gameKey);
        game.setGameName(gameName);
        game.setGameOrgcomID(orgcomID);
        game.setGamePolygonID(polygonID);
        game.setGameStatus(gameStatus);
        game.setGameDateTime(gameDate);
        game.setGameDescription(gameDescription);
        game.setGameWinner(gameWinner);
        gamesList.add(game);
        gamesAdapter.notifyDataSetChanged();
    }
}