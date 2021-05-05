package com.example.airsoft.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.MemberTeamAdapter;
import com.example.airsoft.Classes.MemberTeamClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerViewDecorator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewGameRecyclerActivity extends AppCompatActivity {
    TextView currentDateTime;
    Calendar dateAndTime = Calendar.getInstance();
    Spinner spinnerTeams;
    Spinner spinnerMaps;
    String selectedTeam;
    String selectedMap;
    ArrayAdapter<String> adapterTeams;
    ArrayAdapter<String> adapterMaps;
    //int stat_played;
    //int stat_won;

    String gameDateTime ;
    String winnerTeam;
    //Toast.makeText(this, gameDateTime+ " is selected!", Toast.LENGTH_SHORT).show();
    String gameMap;
    //final String personArsenal = ((EditText)findViewById(R.id.arsenal)).getText().toString();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db_gameDateTime;
    DatabaseReference db_winnerTeam;
    DatabaseReference db_gameMap;
    DatabaseReference db_member_team;
    DatabaseReference db_member_team_id;
    DatabaseReference db_personPlayed;
    DatabaseReference db_personWon;
    DatabaseReference db_usedTeams;
    List<String> used_teams = new ArrayList<String>();

    private List<MemberTeamClass> member_team_List = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemberTeamAdapter mtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_recycler);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//------Вывод RecyclerView ------------------------------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_game);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        mtAdapter = new MemberTeamAdapter(member_team_List);
        RecyclerView.LayoutManager mtLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mtLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mtAdapter);


// ----------------------------------------------------------------------------------------------------------
        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        setInitialDateTime();



//-----------Выпадающий список для выбора карты --------------------------------------------------------------------
        spinnerMaps = (Spinner) findViewById(R.id.polygons_spinner);
        adapterMaps = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getMapsList());
        adapterMaps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaps.setAdapter(adapterMaps);


        AdapterView.OnItemSelectedListener mapSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_LONG).show();
                String item = (String) parent.getItemAtPosition(position);
                selectedMap = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerMaps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMaps.setOnItemSelectedListener(mapSelectedListener);

//-----------Вызовы функций--------------------------------------------------------------------------------------
        add_to_recycler_view();
        addListenerOnButton();
    }


    //----------Создание списка карт для выпадающего списка ----------------------------------------------------------
    public List<String> getMapsList() {
        final List<String> mapsList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Maps");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) return;
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    mapsList.add(postSnapShot.getValue().toString());
                }
                adapterMaps.notifyDataSetChanged();//обновление адаптера
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
        return mapsList;
    }

    //----------отображаем диалоговое окно для выбора даты---------------------------------------------------------
    public void setDate(View v) {
        new DatePickerDialog(NewGameRecyclerActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    //---------отображаем диалоговое окно для выбора времени-----------------------------------------------------
    public void setTime(View v) {
        new TimePickerDialog(NewGameRecyclerActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    //------------установка начальных даты и времени----------------------------------------------------------------
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    //-----------установка обработчика выбора времени----------------------------------------------------------
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
    //-----------установка обработчика выбора даты---------------------------------------------------------------
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    //-----------заполнение recycler_view --------------------------------------------------------------------------------
    public void add_to_recycler_view() {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) return;
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    if (postSnapShot.child("Actually").getValue().toString().equals("1")) {
                        addRow(postSnapShot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void addRow(String fio_from_base) {
        MemberTeamClass member = new MemberTeamClass(fio_from_base);
        member_team_List.add(member);
        mtAdapter.notifyDataSetChanged();
    }

    //------------слушаем нажатие на кнопки--------------------------------------------------------------------------------
    public void addListenerOnButton() {
        Button button_cancel = findViewById(R.id.game_cancel);
        Button button_save = findViewById(R.id.game_save);
        button_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent i = new Intent(".GamesRecyclerActivity");
                        startActivity(i);
                    }
                }

        );
        button_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        add_to_db();
                        finish();
                        Intent i = new Intent(".GamesRecyclerActivity");
                        startActivity(i);

                    }
                }

        );
    }

    //----------при нажатии кнопки сохранить добавление введенных данных в БД---------------------------------------------
//    public void add_to_db() {
//        Log.i("Played_add_to_db","add");
//        gameDateTime = ((TextView) findViewById(R.id.currentDateTime)).getText().toString();
//        winnerTeam = spinnerTeams.getSelectedItem().toString();
//        gameMap = ((Spinner) findViewById(R.id.maps)).getSelectedItem().toString();
//
//        String new_game_id = database.getReference("quiz").push().getKey();
//
//        db_gameDateTime = database.getReference("Games/game_id/" + new_game_id + "/DateTime");
//        db_winnerTeam = database.getReference("Games/game_id/" + new_game_id + "/WinnerTeam");
//        db_gameMap = database.getReference("Games/game_id/" + new_game_id + "/Map");
//        db_usedTeams = database.getReference("Games/game_id/"+new_game_id+"/UsedTeams");
//
//        db_gameDateTime.setValue(gameDateTime);
//        db_winnerTeam.setValue(winnerTeam);
//        db_gameMap.setValue(gameMap);
//
//
//        String new_member_team_list_id = database.getReference("quiz").push().getKey();
//
//        for (MemberTeamClass i : member_team_List) {
//            Log.i("Played_new_member","new in list");
//            String mem = i.getMember();
////            String team = i.getTeam();
////            if (!team.equals("Не участвовал")){
////                if (used_teams.isEmpty()){
////                    used_teams.add(team);
////                }
////                else {
////                    if (!used_teams.contains(team)){
////                        used_teams.add(team);
////                    }
////                }
////
////            }
//            DistributionByTeams(new_member_team_list_id);
//
////            db_member_team = database.getReference("MembersTeams/id/" + new_member_team_list_id + "/" + mem);
////            db_member_team.setValue(team);
//
//            //Наращивем значения Учавствовал и Выиграл для игроков
//            IncreaseStats_GetValues(mem,team);
//        }
//        Log.i("Played_used_teams",used_teams.toString()+ " ");
//        db_usedTeams.setValue(used_teams.toString());
//        db_member_team_id = database.getReference("Games/game_id/" + new_game_id + "/MemberTeamID");
//        db_member_team_id.setValue(new_member_team_list_id);
//
//    }
//
//    public void DistributionByTeams(String id){
//        for (String i:used_teams){
//            List<String> i_members = new ArrayList<>();
//            i_members.add(i);
//            for (MemberTeamClass m : member_team_List) {
//                String member = m.getMember();
//                String team = m.getTeam();
//                if (team.equals(i)){
//                    i_members.add(member);
//                }
//            }
//            db_member_team = database.getReference("MembersTeams/id/" + id + "/" + i);
//            db_member_team.setValue(i_members.toString());
//        }
//
//
//    }


//----------Получаем старые значения статистики и наращиваем------------------------------------------------------------------------
    public void IncreaseStats_GetValues(final String nick, final String team) {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) return;
                int played = Integer.parseInt(dataSnapshot.child("Played").getValue().toString()) ;
                int won = Integer.parseInt(dataSnapshot.child("Won").getValue().toString()) ;

                IncreaseStats(played,won,nick,team);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {// вызывается, когда пользователь не имеет доступа к этой ссылке по правилам базы данных.
                // Error
                Log.d("Error", "databaseError");
            }
        });
        //Log.i("Played_betw",""+ stat_played);

    }
//----------Наращивем значения Учавствовал и Выиграл для игроков-----------------------------------------------
    private void IncreaseStats(int played, int won, String nick, String team) {
        db_personPlayed = database.getReference("Members/members_nicknames/" + nick + "/Played");
        db_personWon = database.getReference("Members/members_nicknames/" + nick + "/Won");

        if (!team.equals("Не участвовал")){
            int new_played = played+1;
            db_personPlayed.setValue(new_played);

            if (team.equals(winnerTeam)) {
                Log.d("Played_ok" , "ok");

                int new_won = won+1;
                db_personWon.setValue(new_won);
            }
        }
//        Log.i("Played_func",""+ stat_played);
    }


}











