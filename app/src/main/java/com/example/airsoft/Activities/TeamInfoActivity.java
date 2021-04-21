package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

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
        addListenerOnButton(teamKey);
    }

    private void memberOfTeamChecker() {
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

    private void getData(String teamKey){
        fbData.getTeamInfo(new FirebaseData.teamInfoCallback()  {
            @Override
            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                ((TextView) findViewById(R.id.text_team_name)).setText(teamName);
                ((TextView) findViewById(R.id.team_info_age)).setText("Год основания команды: "+teamYear);
                ((TextView) findViewById(R.id.team_info_city)).setText("Город команды: "+teamCity);
            }
        },teamKey);
    }
    private void addListenerOnButton(final String teamKey){
        Button requestToConnect =  findViewById(R.id.button_request_to_connect);
        Button requestsToMyTeam =  findViewById(R.id.requests_to_my_team_button);
        requestToConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.checkPersonRequests(new FirebaseData.checkPersonsRequestsCallback() {
                    @Override
                    public void onPersonRequestsResultChanged(boolean res) {
                        if (res){
                            Toast.makeText(TeamInfoActivity.this,  "Ваша заявка в эту команду уже рассматривается",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            fbData.requestToConnect(teamKey);
                            Toast.makeText(TeamInfoActivity.this, "Ваша заявка на вступление в команду отправлена!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, teamKey);


            }
        });
        requestsToMyTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".RequestsToMyTeam");
                startActivity(i);
            }
        });
    }


}
