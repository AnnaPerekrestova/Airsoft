package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class PlayerInfo extends AppCompatActivity {

    TextView txt_birthday;
    TextView txt_fio;
    TextView txt_arsenal;
    TextView txt_position;
    TextView txt_nickname;
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
    }
//-----Мполучаем информацию об игроке из БД и заполняем ею элементы----------------------------------------------------
    private void getMemberInfo() {
        //заполняем элементы активности
        FirebaseData fbData = new FirebaseData().getInstance();
        fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
            @Override
            public void onPlayerInfoChanged(String fio, String nickname, String birthday, String contacts, String arsenal) {
                fillPlayerInfo(fio, birthday,nickname, contacts, arsenal);
            }

            @Override
            public void onOrgInfoChanged(String fio, String birthday) {

            }
        }, personUID );

    }
//--------заполняем элементы активности-----------------------------------
    @SuppressLint("SetTextI18n")
    private void fillPlayerInfo(String fio, String birthday, String nickname, String contacts, String ars) {
        txt_fio = (TextView) findViewById(R.id.member_fio);
        txt_birthday = (TextView) findViewById(R.id.member_birthday);
        txt_nickname = (TextView) findViewById(R.id.member_nickname);
        txt_position = (TextView) findViewById(R.id.member_contacts);
        txt_arsenal = (TextView) findViewById(R.id.member_arsenal);

        txt_fio.setText("ФИО: " + fio);
        txt_birthday.setText("День рождения: "+birthday);
        txt_nickname.setText("Позывной: "+nickname);
        txt_position.setText("Контакты: " + contacts);
        txt_arsenal.setText("Снаряжение: "+ars);

    }


}
