package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerInfo extends AppCompatActivity {

    TextView txt_id_member;
    TextView txt_fio;
    TextView txt_arsenal;
    TextView txt_position;
    TextView txt_nickname;
    String personUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
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
            public void onPlayerInfoChanged(String fio, String nickname, String birthday, String position, String arsenal) {
                fillPlayerInfo(arsenal, fio, position);
            }

            @Override
            public void onOrgInfoChanged(String fio, String birthday) {

            }
        }, personUID );

    }
//--------заполняем элементы активности-----------------------------------
    private void fillPlayerInfo(String ars, String fio, String pos) {
        txt_fio = (TextView) findViewById(R.id.member_fio);
        txt_arsenal = (TextView) findViewById(R.id.member_arsenal);
        txt_position = (TextView) findViewById(R.id.member_position);

        txt_arsenal.setText("Снаряжение: "+ars);
        txt_fio.setText("ФИО: " + fio);
        txt_position.setText("Должность: " + pos);

    }


}
