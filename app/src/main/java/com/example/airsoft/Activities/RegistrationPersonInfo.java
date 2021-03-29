package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.airsoft.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPersonInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_person_info);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button next = findViewById(R.id.person_info_done);
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //-------------записываем информацию об игроке в бд----------------------
                        to_db();

                        //-------переходим в подключение к команде-------------
                        Intent i = new Intent(".ConnectingToTeam");
                        startActivity(i);
                    }
                }

        );
    }
    public void to_db(){
        final String personFIO = ((EditText)findViewById(R.id.person_fio_reg)).getText().toString();
        final String personNickname = ((EditText)findViewById(R.id.nickname_reg)).getText().toString();
        final String personBirthday = ((EditText)findViewById(R.id.birthday_reg)).getText().toString();
        final String personPosition = ((EditText)findViewById(R.id.person_position_reg)).getText().toString();
        final String personArsenal = ((EditText)findViewById(R.id.arsenal_reg)).getText().toString();
        final String personUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_personFIO;
        DatabaseReference db_personPosition;
        DatabaseReference db_personArsenal;
        DatabaseReference db_personBirthday;
        DatabaseReference db_personNickname;
//        DatabaseReference db_personPlayed;
//        DatabaseReference db_personWon;
//        DatabaseReference db_actually;

        db_personFIO = database.getReference("PersonInfo/"+personUID+"/FIO");
        db_personNickname = database.getReference("PersonInfo/"+personUID+"/Nickname");
        db_personBirthday = database.getReference("PersonInfo/"+personUID+"/Birthday");
        db_personPosition = database.getReference("PersonInfo/"+personUID+"/Position");
        db_personArsenal = database.getReference("PersonInfo/"+personUID+"/Arsenal");
//        db_personPlayed = database.getReference("PersonInfo/"+personUID+"/Played");
//        db_personWon = database.getReference("Members/members_nicknames/"+personNickname+"/Won");
//        db_actually = database.getReference("Members/members_nicknames/"+personNickname+"/Actually");

        db_personFIO.setValue(personFIO);
        db_personNickname.setValue(personNickname);
        db_personBirthday.setValue(personBirthday);
        db_personPosition.setValue(personPosition);
        db_personArsenal.setValue(personArsenal);
//        db_personPlayed.setValue(0);
//        db_personWon.setValue(0);
//        db_actually.setValue(1);
    }

}
