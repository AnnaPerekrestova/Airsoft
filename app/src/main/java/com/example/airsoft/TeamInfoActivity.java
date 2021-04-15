package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.data.FirebaseData;

import java.util.List;

public class TeamInfoActivity extends AppCompatActivity {

    String teamKey;
    FirebaseData fbData = new FirebaseData().getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        teamKey = intent.getStringExtra("teamKey");

        getData(teamKey);
        memberOfTeamChecker();
    }

    public void memberOfTeamChecker() {
        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
//-------------------проверяем, состоит ли участник в команде. Если нет, значит он ищет новую и подгоняем интерфейс для вступления------------------
                if (teamKey == "no info"){
                    findViewById(R.id.button_save_changes).setVisibility(View.INVISIBLE);
                    findViewById(R.id.button_request_to_connect).setVisibility(View.VISIBLE);
//                    findViewById(R.id.team_info_description)
                }
                else{
                    findViewById(R.id.button_request_to_connect).setVisibility(View.INVISIBLE);
                    findViewById(R.id.button_save_changes).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        });
    }

    public void getData(String teamKey){
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
