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
                        String orgcomCity = ((EditText)findViewById(R.id.orgcom_city)).getText().toString();;
                        //------проверяем заполненность полей-----------------------------------------
                        if ((!orgcomName.equals("")) & (!orgcomCity.equals(""))){
                            //------если заполнено, то добавляем в бд ------------------------
                            to_bd(orgcomName,orgcomCity);

                            //----высвечиваем ключ ----------------------------------------------------
                            findViewById(R.id.create_orgcom_button).setVisibility(View.INVISIBLE);
                            findViewById(R.id.connecting_members_to_orgcom).setVisibility(View.VISIBLE);
                            findViewById(R.id.new_orgcom_key).setVisibility(View.VISIBLE);
                            findViewById(R.id.create_orgcom_next).setVisibility(View.VISIBLE);

                            EditText key = (EditText)findViewById(R.id.new_orgcom_key);
                            key.selectAll();
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
    private void to_bd(String orgcomName,String orgcomCity){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

//--------generate random key-------------------------------------------------------------------------
        String new_orgcom_key = database.getReference("quiz").push().getKey();

        DatabaseReference db_orgcomName;
        DatabaseReference db_orgcomCity;


        db_orgcomName = database.getReference("OrgcomInfo/"+new_orgcom_key+"/OrgcomName");
        db_orgcomCity = database.getReference("OrgcomInfo/"+new_orgcom_key+"/OrgcomCity");

        db_orgcomName.setValue(orgcomName);
        db_orgcomCity.setValue(orgcomCity);

        String userId= FirebaseAuth.getInstance().getUid();

//----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("OrgcomKey").setValue(new_orgcom_key);
//----------высвечиваем ключ------------------------------------------------------------------------------------
        EditText key = findViewById(R.id.new_orgcom_key);
        key.setText(new_orgcom_key);

    }
}