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

public class CreatingOrgcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_orgcom);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        Button create_team = findViewById(R.id.create_orgcom_button);
        Button next = findViewById(R.id.create_orgcom_next);
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String orgcomName = ((EditText)findViewById(R.id.orgcom_name)).getText().toString();
                        String orgcomCity = ((EditText)findViewById(R.id.orgcom_city)).getText().toString();
                        String orgcomDescription = ((EditText)findViewById(R.id.orgcom_description)).getText().toString();
                        //------проверяем заполненность полей-----------------------------------------
                        if ((!orgcomName.equals("")) & (!orgcomCity.equals(""))){
                            //------если заполнено, то добавляем в бд ------------------------
                            FirebaseData fbData = new FirebaseData().getInstance();
                            String orgcomKey =  fbData.creatingOrgcom(orgcomName,orgcomCity, orgcomDescription);
                            EditText key = findViewById(R.id.new_orgcom_key);
                            key.setText(orgcomKey);

                            //----высвечиваем ключ ----------------------------------------------------
                            findViewById(R.id.create_orgcom_button).setVisibility(View.INVISIBLE);
                            findViewById(R.id.connecting_members_to_orgcom).setVisibility(View.VISIBLE);
                            findViewById(R.id.new_orgcom_key).setVisibility(View.VISIBLE);
                            findViewById(R.id.create_orgcom_next).setVisibility(View.VISIBLE);


                            //key.selectAll();
                        }
                        else {
                            //------если не заполнено, то просим заполнить-----------------------------------
                            Toast.makeText(CreatingOrgcomActivity.this, "Заполните поля!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       переходим к главной активности для организаторов
                        Intent i = new Intent(".MainOrgcomActivity");
                        finish();
                        startActivity(i);

                    }
                }
        );
    }
}