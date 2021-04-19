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

public class ConnectingToOrgcom extends AppCompatActivity {

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
                        FirebaseData fbData = new FirebaseData().getInstance();
                        fbData.setOrgcomKeyIfExist(new FirebaseData.checkOrgcomExistCallback() {
                            @Override
                            public void onOrgcomExistChanged(boolean f, String orgcomName) {
                                if (f){
                                    //--высвечиваем уведомление о причоединении к команде---------------------------------

                                    Toast.makeText(ConnectingToOrgcom.this, "Вы успешно присоединены к оргкомитету "+ orgcomName+"!",
                                            Toast.LENGTH_SHORT).show();
                                    //--уходим на главную активность------------------------------------------------------
                                    Intent i = new Intent(".MainOrgcomActivity");
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Toast.makeText(ConnectingToOrgcom.this, "Оргкомитет, соответствующий введенному ключу, отсутствует",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, key);

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
}

