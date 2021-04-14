package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.data.FirebaseData;

public class TeamInfoActivity extends AppCompatActivity {

    String teamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        teamKey = intent.getStringExtra("teamKey");

        getData(teamKey);
    }

    public void getData(String teamKey){
        FirebaseData fbData = new FirebaseData().getInstance();
        fbData.getTeamInfo(new FirebaseData.teamInfoCallback()  {
            @Override
            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                ((TextView) findViewById(R.id.text_team_name)).setText(teamName);
                ((TextView) findViewById(R.id.team_info_age)).setText("Год основания команды: "+teamYear);
                ((TextView) findViewById(R.id.team_info_city)).setText("Город команды: "+teamCity);
            }
        },teamKey);
    }


}
