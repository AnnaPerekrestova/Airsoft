package com.example.airsoft.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class MainActivity extends AppCompatActivity {

    public FirebaseData fbData = FirebaseData.getInstance();
    String personUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        getData();
    }

    //---------------Вывод названия команды----------------------------------------------------------
    public void getData(){
        if (fbData.getUserUID()!=null) {
            fbData.getTeamName(new FirebaseData.teamCallback() {
                @Override
                public void onTeamIdChanged(final String teamKey) {}

                @Override
                public void onTeamNameChanged(String teamName) {
                    ((TextView) findViewById(R.id.text_team_name)).setText(teamName);
//---------------Делаем название команды видимым и добавляем информацию о команде при клике---------------------------------------
                    findViewById(R.id.text_team_name).setVisibility(View.VISIBLE);
                }
            });

        }
    }



    //--------------------------------------------------------------------------------------------------
    public void addListenerOnButton(){
        Button buttonMembers = findViewById(R.id.members);
        Button buttonGames = findViewById(R.id.games);
        Button buttonStatistic = findViewById(R.id.stats);
        Button buttonMyInfo = findViewById(R.id.my_info);

        buttonMembers.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fbData.getTeamKey(new FirebaseData.teamCallback() {
                            @Override
                            public void onTeamIdChanged(final String teamKey) {
                                Intent i = new Intent(".TeamInfoActivity");
                                startActivity(i);
                            }

                            @Override
                            public void onTeamNameChanged(String teamName) {}
                        });
                    }
                }

        );
        buttonGames.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".GamesViewSelectActivity");
                        startActivity(i);
                    }
                }

        );
        buttonStatistic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".StatisticActivity");
                        startActivity(i);
                    }
                }

        );
        buttonMyInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".PlayerInfo");
                        startActivity(i);
                    }
                }

        );
    }
}