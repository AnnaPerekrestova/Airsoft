package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

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
                        String teamCity = ((EditText)findViewById(R.id.team_city)).getText().toString();
                        String teamYear = ((EditText)findViewById(R.id.team_year )).getText().toString();
                        //------проверяем заполненность полей-----------------------------------------
                        if ((!teamName.equals("")) & (!teamCity.equals(""))){
                            //------если заполнено, то добавляем в бд ------------------------
                            FirebaseData fbData = new FirebaseData().getInstance();
                            String teamKey = fbData.creatingTeam(teamName,teamCity,teamYear);

                            //----------высвечиваем ключ------------------------------------------------------------------------------------
                            EditText key = findViewById(R.id.new_team_key);
                            key.setText(teamKey);


                            //----высвечиваем ключ ----------------------------------------------------
                            findViewById(R.id.create_team_button).setVisibility(View.INVISIBLE);
                            findViewById(R.id.connecting_members).setVisibility(View.VISIBLE);
                            findViewById(R.id.new_team_key).setVisibility(View.VISIBLE);
                            findViewById(R.id.create_team_next).setVisibility(View.VISIBLE);

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
                        finish();
                        startActivity(i);

                    }
                }
        );
    }
}
