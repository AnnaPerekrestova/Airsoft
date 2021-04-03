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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConnectingToTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_team);
        addListenerOnButton();

    }
    public void addListenerOnButton() {
        Button connect_to_team = findViewById(R.id.connect_to_team);
        Button create_team = findViewById(R.id.create_team);
        Button solo = findViewById(R.id.solo);
        connect_to_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//----------------------считываем введенный ключ--------------------------------------------------------------
                        String key = ((EditText)findViewById(R.id.team_key)).getText().toString();
                        //--вносим изменения в бд (присоединяем персон к команде)-----------------------------
                        to_bd(key);
                        //--высвечиваем уведомление о причоединении к команде---------------------------------

                        Toast.makeText(ConnectingToTeam.this, "Вы успешно присоединены к команде!",
                                Toast.LENGTH_SHORT).show();
                        //--уходим на главную активность------------------------------------------------------
                        Intent i = new Intent(".MainActivity");
                        startActivity(i);
                        finish();
                    }
                }

        );
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CreatingTeam");
                        finish();
                        startActivity(i);
                    }
                }

        );
        solo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

//--------generate random key-------------------------------------------------------------------------
                        String new_team_key = database.getReference("quiz").push().getKey();

                        DatabaseReference db_teamName;
                        DatabaseReference db_teamCity;


                        db_teamName = database.getReference("TeamInfo/"+new_team_key+"/TeamName");
                        db_teamCity = database.getReference("TeamInfo/"+new_team_key+"/TeamCity");

                        db_teamName.setValue("default_team_name");
                        db_teamCity.setValue("default_team_city");

                        String userId=FirebaseAuth.getInstance().getUid();

//----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
                        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
                        DatabaseReference user_person_info = databaseRef.child(userId);
                        user_person_info.child("TeamKey").setValue(new_team_key);



                        Intent i = new Intent(".MainActivity");
                        finish();
                        startActivity(i);
                    }
                }

        );
    }
    private void to_bd(String key){
        String userId= FirebaseAuth.getInstance().getUid();

//----------записываем введенный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("TeamKey").setValue(key);
    }
}
