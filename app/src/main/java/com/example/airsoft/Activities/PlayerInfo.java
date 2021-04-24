package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class PlayerInfo extends AppCompatActivity {

    FirebaseData fbData = new FirebaseData().getInstance();
    Button leave_team;
    Button members;
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
        personUID = intent.getStringExtra("playerID");

//------Заполняем  информацию----------------------------------------------------------------------
        getMemberInfo();
        personInfo();

        leave_team  = findViewById(R.id.leave_the_team);
        members  = findViewById(R.id.team_members_button);

        addListenerOnButton();
    }

    public void personInfo(){
        if (personUID.equals(fbData.getUserUID()))  {
            findViewById(R.id.leave_the_team).setVisibility(View.VISIBLE);
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
                        fbData.LeaveFromTeam();
                        finish();
                        Intent i = new Intent(".SearchTeamActivity");
                        startActivity(i);
                    }
                }

        );
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".MembersRecyclerActivity");
                startActivity(i);
            }
        });
    }

//-----Мполучаем информацию об игроке из БД и заполняем ею элементы----------------------------------------------------
    private void getMemberInfo() {
        //заполняем элементы активности

        fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
            @Override
            public void onPlayerInfoChanged(final String fio, final String nickname, final String birthday, final String contacts, final String arsenal, String teamKey) {
                if (teamKey==null){
                    //--делаем невидимыми кнопку и строку с названием команды---------------------------
                    fillPlayerInfo(fio, birthday,nickname, contacts, arsenal,"");
                }
                else{
                    fbData.getTeamInfo(new FirebaseData.teamInfoCallback() {
                        @Override
                        public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {

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
