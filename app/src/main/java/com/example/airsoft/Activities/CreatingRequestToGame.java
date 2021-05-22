package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class CreatingRequestToGame extends AppCompatActivity {
    FirebaseData fbData = FirebaseData.getInstance();
    String orgcomID;
    String gameID;
    String teamID;
    String reqGameDescr;
    String playersCount;
    String reqGameStatus;
    boolean payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_request_to_game);

        Intent intent = getIntent();
        gameID=intent.getStringExtra("gameID");
        orgcomID=intent.getStringExtra("orgcomID");
        payment=false;
        reqGameStatus="рассматривается";

        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                teamID=teamKey;
            }

            @Override
            public void onTeamNameChanged(String teamName) {}
        },fbData.getUserUID());

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button createReq = findViewById(R.id.button_new_request_to_game);

        createReq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playersCount = ((EditText) findViewById(R.id.new_request_to_game_count)).getText().toString();
                        reqGameDescr = ((EditText) findViewById(R.id.new_request_to_game_description)).getText().toString();
                        if (!playersCount.equals("")&!reqGameDescr.equals("")){
                            fbData.requestToGame(orgcomID, gameID, teamID, reqGameDescr, playersCount, reqGameStatus, payment);
                            finish();
                        }
                        else {
                            Toast.makeText(CreatingRequestToGame.this, "Заполните поля",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
    }
}