package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.airsoft.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPersonInfo extends AppCompatActivity {

    private boolean f = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_person_info);

        final EditText nickname_reg = findViewById(R.id.nickname_reg);
        final EditText person_position_reg = findViewById(R.id.person_position_reg);
        final EditText arsenal_reg = findViewById(R.id.arsenal_reg);

        final Switch switch_to_org = findViewById(R.id.switch_to_org);

        switch_to_org.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    nickname_reg.setVisibility(View.INVISIBLE);
                    person_position_reg.setVisibility(View.INVISIBLE);
                    arsenal_reg.setVisibility(View.INVISIBLE);
                    nickname_reg.setText("");
                    person_position_reg.setText("");
                    arsenal_reg.setText("");
                    f = true;
                }else{
                    nickname_reg.setVisibility(View.VISIBLE);
                    person_position_reg.setVisibility(View.VISIBLE);
                    arsenal_reg.setVisibility(View.VISIBLE);
                    f = false;
                }
            }
        });
        addListenerOnButton();
    }



    public void addListenerOnButton() {
        Button next = findViewById(R.id.person_info_done);
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (f==false) {

                            //-------------записываем информацию об игроке в бд----------------------
                            to_db_player();

                            //-------переходим в подключение к команде-------------
                            Intent i = new Intent(".ConnectingToTeam");
                            startActivity(i);
                        }
                        else{
                            to_db_org();

                            Intent i = new Intent(".MainActivity");
                            startActivity(i);

                        }
                    }
                }

        );
    }



    public void to_db_player(){
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

        db_personFIO = database.getReference("PlayerInfo/"+personUID+"/FIO");
        db_personNickname = database.getReference("PlayerInfo/"+personUID+"/Nickname");
        db_personBirthday = database.getReference("PlayerInfo/"+personUID+"/Birthday");
        db_personPosition = database.getReference("PlayerInfo/"+personUID+"/Position");
        db_personArsenal = database.getReference("PlayerInfo/"+personUID+"/Arsenal");
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

    public void to_db_org(){
        final String personFIO = ((EditText)findViewById(R.id.person_fio_reg)).getText().toString();
        final String personBirthday = ((EditText)findViewById(R.id.birthday_reg)).getText().toString();
        final String personUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_personFIO;
        DatabaseReference db_personBirthday;

        db_personFIO = database.getReference("OrgInfo/"+personUID+"/FIO");
        db_personBirthday = database.getReference("OrgInfo/"+personUID+"/Birthday");

        db_personFIO.setValue(personFIO);
        db_personBirthday.setValue(personBirthday);


    }

}
