package com.example.airsoft;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    private List<MemberTeamClass> member_team_List = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemberTeamAdapter mtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//------Вывод RecyclerView ------------------------------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_game);

        mtAdapter = new MemberTeamAdapter(member_team_List);
        RecyclerView.LayoutManager mtLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mtLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mtAdapter);


// ----------------------------------------------------------------------------------------------------------
        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        setInitialDateTime();

//-----------Выпадающий список для выбора команды-победителя -------------------------------------------------------
        spinnerTeams = (Spinner) findViewById(R.id.teams);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        adapterTeams = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getTeamsList());
        // Определяем разметку для использования при выборе элемента
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeams.setAdapter(adapterTeams);

//-----------Выпадающий список для выбора карты --------------------------------------------------------------------
        spinnerMaps = (Spinner) findViewById(R.id.maps);
        adapterMaps = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getMapsList());
        adapterMaps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaps.setAdapter(adapterMaps);

        AdapterView.OnItemSelectedListener teamSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                selectedTeam = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        //spinnerTeams.setOnItemSelectedListener(teamSelectedListe);

        AdapterView.OnItemSelectedListener mapSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                Toast.makeText(getApplicationContext(),position+ "", Toast.LENGTH_LONG).show();
                String item = (String)parent.getItemAtPosition(position);
                selectedMap = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerMaps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),position+ "", Toast.LENGTH_LONG).show();
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
//----------Создание списка команд для выпадающего списка ----------------------------------------------------------
    public List<String> getTeamsList(){
        final List<String> teamsList= new ArrayList<String>();

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Teams");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    teamsList.add(postSnapShot.getValue().toString());
                }
                adapterTeams.notifyDataSetChanged();//обновление адаптера
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
        return teamsList;
    }
//----------Создание списка карт для выпадающего списка ----------------------------------------------------------
    public List<String> getMapsList(){
        final List<String> mapsList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Maps");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
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
                    addRow(postSnapShot.getKey());
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
                    }
                }

        );
        button_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        add_to_db();
                    }
                }

        );
    }
//----------при нажатии кнопки сохранить добавление введенных данных в БД---------------------------------------------
    public void add_to_db() {
        final String gameDateTime = ((TextView) findViewById(R.id.currentDateTime)).getText().toString();
        final String winnerTeam = spinnerTeams.getSelectedItem().toString();
        //Toast.makeText(this, gameDateTime+ " is selected!", Toast.LENGTH_SHORT).show();
        final String gameMap = ((Spinner) findViewById(R.id.maps)).getSelectedItem().toString();
        //final String personArsenal = ((EditText)findViewById(R.id.arsenal)).getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_gameDateTime;
        DatabaseReference db_winnerTeam;
        DatabaseReference db_gameMap;
        DatabaseReference db_member_team;
        DatabaseReference db_member_team_id;
        DatabaseReference db_personPlayed;
        DatabaseReference db_personWon;

        String new_game_id = database.getReference("quiz").push().getKey();

        db_gameDateTime = database.getReference("Games/game_id/" + new_game_id + "/DateTime");
        db_winnerTeam = database.getReference("Games/game_id/" + new_game_id + "/WinnerTeam");
        db_gameMap = database.getReference("Games/game_id/" + new_game_id + "/Map");
        //db_personArsenal = database.getReference("Games/games_id/"+key+"/DateTime");
        db_gameDateTime.setValue(gameDateTime);
        db_winnerTeam.setValue(winnerTeam);
        db_gameMap.setValue(gameMap);


        String new_member_team_list_id = database.getReference("quiz").push().getKey();
        for (MemberTeamClass i:member_team_List) {
            String mem = i.getMember();
            String team = i.getTeam();
            Toast.makeText(this, team+ " is selected!", Toast.LENGTH_SHORT).show();
            db_member_team = database.getReference("MembersTeams/id/"+new_member_team_list_id+"/"+mem);
            db_member_team.setValue(team);

            db_personPlayed = database.getReference("Members/members_nicknames/"+mem+"/Played");
            db_personWon = database.getReference("Members/members_nicknames/"+mem+"/Won");

            db_personPlayed.setValue(GetValues()[0]+1);
            if (winnerTeam.equals(team)){
                db_personPlayed.setValue(GetValues()[1]+1);
            }

        }
        db_member_team_id = database.getReference("Games/game_id/" + new_game_id + "/MemberTeamID");
        db_member_team_id.setValue(new_member_team_list_id);
        }
    public Integer[] GetValues(){
        final List<String> membersNicksList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    membersNicksList.add(postSnapShot.getKey());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
        return (GetData(databaseRef, membersNicksList));
    }
    private Integer[] GetData(DatabaseReference databaseRef, List<String> userNicksList) {
        final Integer[] statistic = new Integer[1];
        for (final String nick: userNicksList){
            databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    if(dataSnapshot2==null)return;
                    statistic[0] =  (Integer) dataSnapshot2.child("Played").getValue();
                    statistic[1] =  (Integer) dataSnapshot2.child("Won").getValue();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
        return statistic;
    }

    }




