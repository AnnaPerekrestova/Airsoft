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

public class ConnectingToTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_team);
        addListenerOnButton();

    }
    public void addListenerOnButton() {
        Button connect_to_team = findViewById(R.id.connect_to_team);
        Button create_team = findViewById(R.id.create_team);
        Button search_team = findViewById(R.id.search_team);
        connect_to_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//----------------------считываем введенный ключ--------------------------------------------------------------
                        String key = ((EditText)findViewById(R.id.team_key)).getText().toString();
                        //--вносим изменения в бд (присоединяем персон к команде)-----------------------------
                        FirebaseData fbData = new FirebaseData().getInstance();
                        fbData.setTeamKeyIfExist(new FirebaseData.checkTeamExistCallback() {
                            @Override
                            public void onTeamExistChanged(boolean f, String teamName) {
                                if (f){
                                    //--высвечиваем уведомление о причоединении к команде---------------------------------

                                    Toast.makeText(ConnectingToTeam.this, "Вы успешно присоединены к команде "+ teamName+"!",
                                            Toast.LENGTH_SHORT).show();
                                    //--уходим на главную активность------------------------------------------------------
                                    Intent i = new Intent(".MainActivity");
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Toast.makeText(ConnectingToTeam.this, "Команда, соответствующая введенному ключу, отсутствует",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, key);


                    }
                }

        );
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CreatingTeam");
                        finish();
                        startActivity(i);
                    }
                }

        );
        search_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".SearchTeamActivity");
                        finish();
                        startActivity(i);
                    }
                }

        );

    }
}
