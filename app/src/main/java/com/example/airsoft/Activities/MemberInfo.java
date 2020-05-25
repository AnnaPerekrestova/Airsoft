package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.airsoft.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberInfo extends AppCompatActivity {

    TextView txt_id_member;
    TextView txt_fio;
    TextView txt_arsenal;
    TextView txt_position;
    TextView txt_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
//-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        String nick = intent.getStringExtra("id_m");
//------Заполняем информацию о никнейме---------------------------------------------------------------------
        txt_nickname = (TextView) findViewById(R.id.member_nickname);
        txt_nickname.setText("Позывной: " + nick);
//------Заполняем остальную информацию----------------------------------------------------------------------
        Get_member_info(nick);





    }
//-----Мполучаем информацию об игроке из БД и заполняем ею элементы----------------------------------------------------
    private void Get_member_info(String nick) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshotInfo) {

                if (dataSnapshotInfo == null) return;
                String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                String arsenal = (String) dataSnapshotInfo.child("Arsenal").getValue();
                String position = (String) dataSnapshotInfo.child("Position").getValue();
                //заполняем элементы активности
                Fill_member_info(arsenal, fio, position);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }
//--------заполняем элементы активности-----------------------------------
    private void Fill_member_info(String ars, String fio, String pos) {
        txt_fio = (TextView) findViewById(R.id.member_fio);
        txt_arsenal = (TextView) findViewById(R.id.member_arsenal);
        txt_position = (TextView) findViewById(R.id.member_position);

        txt_arsenal.setText("Снаряжение: "+ars);
        txt_fio.setText("ФИО: " + fio);
        txt_position.setText("Должность: " + pos);

    }


}
