package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.airsoft.Adapters.TeamAdapter;
import com.example.airsoft.Classes.TeamClass;
import com.example.data.FirebaseData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SearchTeamActivity extends AppCompatActivity {
    TeamAdapter teamAdapter;
    List<TeamClass> teamsList= new ArrayList<>();
    FirebaseData fbData = new FirebaseData().getInstance();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);
        recyclerView = (RecyclerView) findViewById(R.id.teams_list_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        teamAdapter = new TeamAdapter(teamsList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(teamAdapter);

//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                PlayerClass selectedMember = teamsList.get(position);
////                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();
//
//                String personUID = (String) selectedMember.getPlayerUID();
////                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(".PlayerInfo");
//                intent.putExtra("playerID", personUID);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }

//            @Override
//            public void onLongClick(View view, int position) {
//                MembersClass selected_member = membersList.get(position);
//                final String id_member_to_del = (String) selected_member.getNickname();
//                AlertDialog.Builder builder = new AlertDialog.Builder(MembersRecyclerActivity.this);
//                builder.setTitle("Удаление игрока");
//                builder.setMessage("Вы действительно хотите удалить выбранного игрока?");
//                builder.setCancelable(false);
//                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { // Кнопка Удалить
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), id_member_to_del + " Удален из команды", Toast.LENGTH_SHORT).show();
//                        DatabaseReference db_actually;
//                        db_actually = database.getReference("Members/members_nicknames/" + id_member_to_del + "/Actually");
//                        db_actually.setValue(0);
//                        dialog.dismiss();
//                        Intent i = new Intent(".MembersRecyclerActivity");
//                        startActivity(i);
//                        finish();
//                        // Отпускает диалоговое окно
//                    }
//
//                });
//                builder.setNegativeButton("Оставить", new DialogInterface.OnClickListener() { // Кнопка Оставить
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss(); // Отпускает диалоговое окно
//                    }
//
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        }));
        addToTeamRecycler();

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
//    public void addNewMember(View view) {
//        Intent i = new Intent(".NewMemberActivity");
//        startActivity(i);
//        finish();
//    }

}
