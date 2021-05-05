package com.example.airsoft.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class TeamInfoActivity extends AppCompatActivity {

    String thisTeamKey;
    FirebaseData fbData = new FirebaseData().getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        if (intent.getStringExtra("teamKey")==null){
            fbData.getTeamKey(new FirebaseData.teamCallback() {
                @Override
                public void onTeamIdChanged(String teamKey) {
                    thisTeamKey=teamKey;
                    findViewById(R.id.button_request_to_connect).setVisibility(View.INVISIBLE);
                    findViewById(R.id.button_save_changes).setVisibility(View.VISIBLE);
                    findViewById(R.id.team_info_description).setEnabled(true);
                }

                @Override
                public void onTeamNameChanged(String teamName) {}
            });
        }
        else {
            thisTeamKey = intent.getStringExtra("teamKey");
            findViewById(R.id.button_save_changes).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_request_to_connect).setVisibility(View.VISIBLE);


        }
        getData(thisTeamKey);
        addListenerOnButton(thisTeamKey);
        onRequestApprove();

    }


    private void getData(String teamKey){
        fbData.getTeamInfo(new FirebaseData.teamInfoCallback()  {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription) {
                ((TextView) findViewById(R.id.text_team_name)).setText(teamName);
                ((TextView) findViewById(R.id.team_info_age)).setText(teamYear);
                ((TextView) findViewById(R.id.team_info_city)).setText(teamCity);
                ((EditText) findViewById(R.id.team_info_description)).setText(teamDescription);
            }
        },teamKey);
    }
    private void addListenerOnButton(final String teamKey){
        Button requestToConnect =  findViewById(R.id.button_request_to_connect);
        Button members =  findViewById(R.id.team_members_button);
        Button saveTeamInfo = findViewById(R.id.button_save_changes);
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
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".MembersRecyclerActivity");
                i.putExtra("teamKey", thisTeamKey);
                startActivity(i);
            }
        });
        saveTeamInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeamInfoActivity.this);
                builder.setTitle("Сохранить изменения");
                builder.setMessage("Перезаписать данные команды?");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewTeamInfo();
                        // Отпускает диалоговое окно
                    }

                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Оставить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public void saveNewTeamInfo(){
        final String teamDescription = ((EditText) findViewById(R.id.team_info_description)).getText().toString();

        fbData.changeTeamInfo(teamDescription, thisTeamKey);
        finish();
    }

    private void onRequestApprove(){
        //---если появилась одобренная заявка - открываем mainAct------------------
        fbData.onRequestApprove(new FirebaseData.onRequestApproveCallback() {

            @Override
            public void onRequestApprove() {
                Intent i = new Intent(".MainActivity");
                startActivity(i);
                finish();
            }
        });
    }
}
