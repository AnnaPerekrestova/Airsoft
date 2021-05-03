package com.example.airsoft.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class PlayerInfo extends AppCompatActivity {

    FirebaseData fbData = new FirebaseData().getInstance();
    Button leave_team;
    Button delog;
    Button save;
    TextView txt_birthday;
    TextView txt_fio;
    TextView txt_arsenal;
    TextView txt_position;
    TextView txt_nickname;
    TextView txt_team;
    String personUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
//-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        if (intent.getStringExtra("playerID")==null){
            personUID=fbData.getUserUID();
        }
        else {
            personUID = intent.getStringExtra("playerID");
        }
//------Заполняем  информацию----------------------------------------------------------------------
        getMemberInfo();
        personInfo();

        leave_team  = findViewById(R.id.leave_the_team);
        delog = findViewById(R.id.delog);
        save = findViewById(R.id.save_my_info);

        addListenerOnButton();

    }

    public void personInfo(){
        if (personUID.equals(fbData.getUserUID()))  {
            findViewById(R.id.leave_the_team).setVisibility(View.VISIBLE);
            findViewById(R.id.delog).setVisibility(View.VISIBLE);
            findViewById(R.id.save_my_info).setVisibility(View.VISIBLE);
            findViewById(R.id.member_nickname).setEnabled(true);
            findViewById(R.id.member_contacts).setEnabled(true);
            findViewById(R.id.member_arsenal).setEnabled(true);
        }
        else {
            return;
        }

    }

    public void addListenerOnButton(){
        leave_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerInfo.this);
                        builder.setTitle("Выход из команды");
                        builder.setMessage("Вы действительно хотите выйти из команды?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbData.LeaveFromTeam();
                                finish();
                                Intent i = new Intent(".SearchTeamActivity");
                                startActivity(i);
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
                }

        );
        delog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerInfo.this);
                        builder.setTitle("Выход из системы");
                        builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbData.deLogin();
                                finish();
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
                }
        );
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayerInfo.this);
                builder.setTitle("Сохранить изменения");
                builder.setMessage("Перезаписать данные аккаунта?");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewPersonInfo();
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

    public void saveNewPersonInfo(){
        final String personFIO = ((TextView)findViewById(R.id.member_fio)).getText().toString();
        final String personNickname = ((EditText)findViewById(R.id.member_nickname)).getText().toString();
        final String personBirthday = ((TextView)findViewById(R.id.member_birthday)).getText().toString();
        final String personContacts = ((EditText)findViewById(R.id.member_contacts)).getText().toString();
        final String personArsenal = ((EditText)findViewById(R.id.member_arsenal)).getText().toString();
        final boolean personOrgFlag = false;

        fbData.creatingPlayer(personFIO,personNickname,personBirthday,personContacts,personArsenal,personOrgFlag);
        finish();
    }

//-----Мполучаем информацию об игроке из БД и заполняем ею элементы----------------------------------------------------
    private void getMemberInfo() {
        //заполняем элементы активности

        fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
            @Override
            public void onPlayerInfoChanged(final String fio, final String nickname, final String birthday, final String contacts, final String arsenal, String teamKey) {
                if (teamKey==null){
                    //--делаем пустой строку с названием команды---------------------------
                    fillPlayerInfo(fio, birthday,nickname, contacts, arsenal,"");
                }
                else{
                    fbData.getTeamInfo(new FirebaseData.teamInfoCallback() {
                        @Override
                        public void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription) {

                            fillPlayerInfo(fio, birthday,nickname, contacts, arsenal, teamName);
                        }
                    }, teamKey);
                }


            }

            @Override
            public void onOrgInfoChanged(String fio, String birthday) {

            }
        }, personUID );


    }

//--------заполняем элементы активности-----------------------------------
    @SuppressLint("SetTextI18n")
    private void fillPlayerInfo(String fio, String birthday, String nickname, String contacts, String ars, String teamname) {
        txt_fio = (TextView) findViewById(R.id.member_fio);
        txt_birthday = (TextView) findViewById(R.id.member_birthday);
        txt_nickname = (TextView) findViewById(R.id.member_nickname);
        txt_position = (TextView) findViewById(R.id.member_contacts);
        txt_arsenal = (TextView) findViewById(R.id.member_arsenal);
        txt_team = (TextView) findViewById(R.id.member_team_name);

        txt_fio.setText(fio);
        txt_birthday.setText(birthday);
        txt_nickname.setText(nickname);
        txt_position.setText(contacts);
        txt_arsenal.setText(ars);
        txt_team.setText(teamname);

    }


}
