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

public class ConnectingToOrgTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_orgcom);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button connect_to_team = findViewById(R.id.button_connect_to_orgcom);
        Button create_team = findViewById(R.id.create_orgcom);
        connect_to_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//----------------------считываем введенный ключ--------------------------------------------------------------
                        String key = ((EditText)findViewById(R.id.orgcom_key)).getText().toString();
                        //--вносим изменения в бд (присоединяем персон к команде)-----------------------------
                        to_bd(key);
                        //--высвечиваем уведомление о причоединении к команде---------------------------------

                        Toast.makeText(ConnectingToOrgTeam.this, "Вы успешно присоединены к команде!",
                                Toast.LENGTH_SHORT).show();
                        //--уходим на главную активность------------------------------------------------------
                        Intent i = new Intent(".MainOrgcomActivity");
                        startActivity(i);
                        finish();
                    }
                }

        );
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CreatingOrgcomActivity");
                        startActivity(i);
                        finish();
                    }
                }

        );

    }
    private void to_bd(String key){
        String userId= FirebaseAuth.getInstance().getUid();

//----------записываем введенный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("OrgcomKey").setValue(key);
    }
}

