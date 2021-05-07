package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class PlayingTeamsActivity extends AppCompatActivity {

    FirebaseData fbData = FirebaseData.getInstance();
    String  gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_teams);

        Intent intent = getIntent();
        gameID=intent.getStringExtra("gameID");

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
    }
}