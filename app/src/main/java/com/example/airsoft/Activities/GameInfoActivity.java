package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInfoActivity extends AppCompatActivity {

    FirebaseData fbData = new FirebaseData().getInstance();
    String GameID;
    String PolygonID;
    String OrgcomID;
    String GameStatus;

    Spinner spinnerStatuses;
    Spinner winnerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        spinnerStatuses = findViewById(R.id.game_status_spinner);
        winnerSpinner = findViewById(R.id.game_winner_spinner);
        Intent intent = getIntent();
        GameID = intent.getStringExtra("gameID");

        addListenerOnButton();
        getData();

    }

    public void getData(){
        fbData.getGameInfo(new FirebaseData.gameInfoCallback() {
            @Override
            public void onGameInfoChanged(String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner, String gameSides) {
                fillGameInfo(gameName, gameDate, gameDescription, gameSides);
                PolygonID = polygonID;
                OrgcomID = orgcomID;
                GameStatus = gameStatus;
                if (gameStatus.equals("открыт набор на игру")){
                    String[] statusesList= {"открыт набор на игру","набор на игру закрыт","игра отменена","игра идет"};
                    ArrayAdapter<String> adapterStatuses = new ArrayAdapter<String>(GameInfoActivity.this, android.R.layout.simple_spinner_item, statusesList);
                    adapterStatuses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatuses.setAdapter(adapterStatuses);
                    statusSpinnerListener();
                }
                if (gameStatus.equals("игра идет")){
                    String[] statusesList= {"игра идет","игра прошла"};
                    ArrayAdapter<String> adapterStatuses = new ArrayAdapter<String>(GameInfoActivity.this, android.R.layout.simple_spinner_item, statusesList);
                    adapterStatuses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatuses.setAdapter(adapterStatuses);
                    statusSpinnerListener();
                }
                if (gameStatus.equals("игра прошла")){
//                    String[] sidesList= gameSides.split(",");
//                    ArrayAdapter<String> adapterWinner = new ArrayAdapter<String>(GameInfoActivity.this, android.R.layout.simple_spinner_item, sidesList);
//                    adapterWinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    winnerSpinner.setAdapter(adapterWinner);
//                    statusSpinnerListener();

                    String[] statusesList= {"игра прошла"};
                    ArrayAdapter<String> adapterStatuses = new ArrayAdapter<String>(GameInfoActivity.this, android.R.layout.simple_spinner_item, statusesList);
                    adapterStatuses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatuses.setAdapter(adapterStatuses);

                    winnerSpinner.setVisibility(View.VISIBLE);
                    winnerSpinnerListener();
                }

            }
        },GameID);
    }
    public void statusSpinnerListener(){
        spinnerStatuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String selectedStatus = (String) parent.getItemAtPosition(position);
                if (!GameStatus.equals(selectedStatus)){
                    if (selectedStatus.equals("набор на игру закрыт")){
                        //сменить статус в бд
                        fbData.changeGameStatus(GameID,"набор на игру закрыт");
                    }
                    if (selectedStatus.equals("игра отменена")) {
                        //сменить статус в бд
                        fbData.changeGameStatus(GameID,"игра отменена");
                    }
                    if (selectedStatus.equals("игра идет")) {
                        //сменить статус в бд, перерисовать активность
                        fbData.changeGameStatus(GameID,"игра идет");
                        finish();
                        startActivity(getIntent());

                    }
                    if (selectedStatus.equals("игра прошла")) {
                        //сменить статус в бд, перерисовать активность
                        fbData.changeGameStatus(GameID,"игра прошла");
                        finish();
                        startActivity(getIntent());
                        winnerSpinner.setVisibility(View.VISIBLE);
                        winnerSpinnerListener();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public  void winnerSpinnerListener(){
        winnerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String selectedWinner = (String) parent.getItemAtPosition(position);
                fbData.changeGameWinner(GameID,selectedWinner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addListenerOnButton() {
        Button buttonTeams = findViewById(R.id.Game_players_btn);
        Button buttonPolygon = findViewById(R.id.Game_polygon_btn);

        buttonTeams.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".Activities.PlayingTeamsActivity");
                        i.putExtra("gameID", GameID);
                        i.putExtra("orgcomID", OrgcomID);
                        startActivity(i);
                    }
                }

        );
        buttonPolygon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".PolygonInfoActivity");
                        i.putExtra("polygonID", PolygonID);
                        startActivity(i);
                    }
                }

        );
    }

    @SuppressLint("SetTextI18n")
    private void fillGameInfo(String name, String date, String descr, String gameSides){
        String[] allsides=gameSides.split(",");
        for (String side : allsides){
            Log.d("testsides", side);
        }
        TextView gname = (TextView) findViewById(R.id.game_info_name);
        TextView gdate = (TextView) findViewById(R.id.GameDataTime);
        TextView gdescr = (TextView) findViewById(R.id.Game_description);
        TextView gsides = (TextView) findViewById(R.id.Game_sides);

        gname.setText(name);
        gdate.setText(date);
        gdescr.setText(descr);
        gsides.setText(gameSides);

        //-----заполняем список для выбора победителя-------------
        String[] sidesList= gameSides.split(",");
        ArrayAdapter<String> adapterWinner = new ArrayAdapter<String>(GameInfoActivity.this, android.R.layout.simple_spinner_item, sidesList);
        adapterWinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winnerSpinner.setAdapter(adapterWinner);
        winnerSpinnerListener();
    }
}