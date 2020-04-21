package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button buttonPersonSave = findViewById(R.id.person_save);
        Button buttonPersonCancel = findViewById(R.id.person_cancel);
        final String personFIO = ((EditText)findViewById(R.id.person_fio)).getText().toString();
        final String personNickname = ((EditText)findViewById(R.id.nickname)).getText().toString();
        final String personPosition = ((EditText)findViewById(R.id.person_position)).getText().toString();
        final String personArsenal = ((EditText)findViewById(R.id.arsenal)).getText().toString();

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference db_personFIO;
//        DatabaseReference db_personNickname;
//        DatabaseReference db_personPosition;
//        DatabaseReference db_personArsenal;
//        db_personFIO = database.getReference("Team/members_id/1/FIO");
//        db_personNickname= database.getReference("Team/members_id/1/Nickname");
//        db_personPosition = database.getReference("Team/members_id/1/Position");
//        db_personArsenal = database.getReference("Team/members_id/1/Arsenal");

//        db_personFIO.setValue(personFIO);
//        db_personNickname.setValue(personNickname);
//        db_personPosition.setValue(personPosition);
//        db_personArsenal.setValue(personArsenal);


        buttonPersonSave.setOnClickListener(
                new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference db_personFIO;
                        DatabaseReference db_personNickname;
                        DatabaseReference db_personPosition;
                        DatabaseReference db_personArsenal;
                        db_personFIO = database.getReference("Team/members_id/1/FIO");
                        db_personNickname= database.getReference("Team/members_id/1/Nickname");
                        db_personPosition = database.getReference("Team/members_id/1/Position");
                        db_personArsenal = database.getReference("Team/members_id/1/Arsenal");
                        db_personFIO = database.getReference("Team/members_id/1/FIO");
                        db_personNickname= database.getReference("Team/members_id/1/Nickname");
                        db_personPosition = database.getReference("Team/members_id/1/Position");
                        db_personArsenal = database.getReference("Team/members_id/1/Arsenal");

                        db_personFIO.setValue(personFIO);
                        db_personNickname.setValue(personNickname);
                        db_personPosition.setValue(personPosition);
                        db_personArsenal.setValue(personArsenal);
                        Intent i = new Intent(".MembersActivity");
                        startActivity(i);
                    }
                }

        );
        buttonPersonCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MembersActivity");
                        startActivity(i);
                    }
                }

        );

    }

}
