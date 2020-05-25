package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airsoft.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);
        addListenerOnButton();
    }
    //String fio,String nickname,String Position, String arsenal
    public void to_db(){
        final String personFIO = ((EditText)findViewById(R.id.person_fio)).getText().toString();
        final String personNickname = ((EditText)findViewById(R.id.nickname)).getText().toString();
        final String personPosition = ((EditText)findViewById(R.id.person_position)).getText().toString();
        final String personArsenal = ((EditText)findViewById(R.id.arsenal)).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_personFIO;
        DatabaseReference db_personPosition;
        DatabaseReference db_personArsenal;
        DatabaseReference db_personPlayed;
        DatabaseReference db_personWon;
        DatabaseReference db_actually;

        db_personFIO = database.getReference("Members/members_nicknames/"+personNickname+"/FIO");
        db_personPosition = database.getReference("Members/members_nicknames/"+personNickname+"/Position");
        db_personArsenal = database.getReference("Members/members_nicknames/"+personNickname+"/Arsenal");
        db_personPlayed = database.getReference("Members/members_nicknames/"+personNickname+"/Played");
        db_personWon = database.getReference("Members/members_nicknames/"+personNickname+"/Won");
        db_actually = database.getReference("Members/members_nicknames/"+personNickname+"/Actually");

        db_personFIO.setValue(personFIO);
        db_personPosition.setValue(personPosition);
        db_personArsenal.setValue(personArsenal);
        db_personPlayed.setValue(0);
        db_personWon.setValue(0);
        db_actually.setValue(1);
    }

    public void addListenerOnButton() {
        Button buttonPersonSave = findViewById(R.id.person_save);
        Button buttonPersonCancel = findViewById(R.id.person_cancel);
        buttonPersonSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((EditText)findViewById(R.id.person_fio)).getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Поле ФИО должно быть заполнено", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if (((EditText)findViewById(R.id.nickname)).getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(), "Поле Позывной должно быть заполнено", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                to_db();
                                Intent i = new Intent(".MembersRecyclerActivity");
                                startActivity(i);
                                finish();
                            }
                        }

                    }
                }

        );
        buttonPersonCancel.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MembersRecyclerActivity");
                        startActivity(i);
                        finish();
                    }
                }

        );

    }

}
