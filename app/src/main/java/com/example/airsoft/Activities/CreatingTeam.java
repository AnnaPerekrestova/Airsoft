package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airsoft.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class CreatingTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_team);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        Button create_team = findViewById(R.id.create_team_button);
        Button next = findViewById(R.id.create_team_next);
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String teamName = ((EditText)findViewById(R.id.team_name)).getText().toString();
                        String teamCity = ((EditText)findViewById(R.id.team_city)).getText().toString();;
                        //------проверяем заполненность полей-----------------------------------------
                        if ((!teamName.equals("")) & (!teamCity.equals(""))){
                            //------если заполнено, то добавляем в бд ------------------------
                            to_bd(teamName,teamCity);

                            //----высвечиваем ключ ----------------------------------------------------
                            findViewById(R.id.create_team_button).setVisibility(View.INVISIBLE);
                            findViewById(R.id.connecting_members).setVisibility(View.VISIBLE);
                            findViewById(R.id.new_team_key).setVisibility(View.VISIBLE);
                            findViewById(R.id.create_team_next).setVisibility(View.VISIBLE);

                            EditText key = (EditText)findViewById(R.id.new_team_key);
                            key.selectAll();




                        }
                        else {
                            //------если не заполнено, то просим заполнить-----------------------------------
                            Toast.makeText(CreatingTeam.this, "Заполните поля!",
                                    Toast.LENGTH_SHORT).show();
                        }





                    }
                }

        );
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MainActivity");
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
    private void to_bd(String teamName,String teamCity){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

//--------generate random key-------------------------------------------------------------------------
        String new_team_key = database.getReference("quiz").push().getKey();

        DatabaseReference db_teamName;
        DatabaseReference db_teamCity;


        db_teamName = database.getReference("TeamInfo/"+new_team_key+"/TeamName");
        db_teamCity = database.getReference("TeamInfo/"+new_team_key+"/TeamCity");

        db_teamName.setValue(teamName);
        db_teamCity.setValue(teamCity);

        String userId=FirebaseAuth.getInstance().getUid();

//----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("TeamKey").setValue(new_team_key);
//----------высвечиваем ключ------------------------------------------------------------------------------------
        EditText key = findViewById(R.id.new_team_key);
        key.setText(new_team_key);

    }
}
