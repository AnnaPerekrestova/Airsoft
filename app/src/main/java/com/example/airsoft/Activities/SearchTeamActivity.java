package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.airsoft.Adapters.TeamAdapter;
import com.example.airsoft.Classes.TeamClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class SearchTeamActivity extends AppCompatActivity {
    TeamAdapter teamAdapter;
    List<TeamClass> teamsList= new ArrayList<>();
    FirebaseData fbData = FirebaseData.getInstance();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);
        recyclerView = findViewById(R.id.teams_list_recycler);

//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        teamAdapter = new TeamAdapter(teamsList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(teamAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TeamClass selectedTeam = teamsList.get(position);
                String teamKey = selectedTeam.getTeamKey();
                Intent intent = new Intent(".TeamInfoActivity");
                intent.putExtra("teamKey", teamKey);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));
        updateRecycler();
        addToTeamRecycler();
        addListenerOnButton();
        onRequestApprove();

    }
    public void updateRecycler(){

        fbData.getTeamsList(new FirebaseData.teamsListCallback() {

            @Override
            public void onTeamsListChanged(String teamKey, String teamName, String teamCity, String teamYear) { }

            @Override
            public void onTeamsListChanged() {
                teamsList.clear();
                addToTeamRecycler();
                teamAdapter.notifyDataSetChanged();
            }
        });
    }

//------Обрабатываем нажатие кнопки назад на устройстве---------
    @Override
    public void onBackPressed() {
        Intent i = new Intent(".ConnectingToTeam");
        finish();
        startActivity(i);
    }

    public void searchTeamName(){
        SearchView searchTeamName = findViewById(R.id.searchTeamName);
        searchTeamName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                teamAdapter.getFilter().filter(newText);
                return false;
            }
        });

        SearchView searchTeamCity = findViewById(R.id.searchTeamCity);
        searchTeamCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                teamAdapter.getTeamCityFilter().filter(newText);
                return false;
            }
        });
    }
    public void addToTeamRecycler(){
        fbData.getTeamsList(new FirebaseData.teamsListCallback() {
            @Override
            public void onTeamsListChanged(String teamKey, String teamName, String teamCity, String teamYear) {
                int teamAge= getCurrentYear()-Integer.parseInt(teamYear) ;
                addRow(teamKey,teamName,teamCity,String.valueOf(teamAge));
            }

            @Override
            public void onTeamsListChanged() {}
        });

    }
    public static int getCurrentYear()
    {
        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(java.util.Calendar.YEAR);
    }

    private void addRow(String teamKey,String teamName, String teamCity, String teamAge) {
        TeamClass team = new TeamClass( teamKey );
        team.setTeamKey(teamKey);
        team.setTeamName(teamName);
        team.setTeamCity(teamCity);
        team.setTeamAge(teamAge);
        teamsList.add(team);

        teamAdapter.notifyDataSetChanged();

        searchTeamName();
    }
    private void addListenerOnButton(){
        Button myRequests=  findViewById(R.id.my_requests);
        myRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".MyRequests");
                startActivity(i);
            }
        });
    }
    private void onRequestApprove(){
        //---если появилась одобренная заявка - открываем mainAct------------------
        fbData.onRequestApprove(new FirebaseData.onRequestApproveCallback() {

            @Override
            public void onRequestApprove() {
                Intent i = new Intent(".MainActivity");
                startActivity(i);
                finish();
            }
        });
    }

//    public void addNewMember(View view) {
//        Intent i = new Intent(".NewMemberActivity");
//        startActivity(i);
//        finish();
//    }

}
