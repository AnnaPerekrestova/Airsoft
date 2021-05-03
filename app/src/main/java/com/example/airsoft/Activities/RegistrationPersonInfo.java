package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

import java.util.Calendar;

public class RegistrationPersonInfo extends AppCompatActivity {

    private boolean f = false;
    FirebaseData fbData = new FirebaseData().getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_person_info);

        final EditText nickname_reg = findViewById(R.id.nickname_reg);
        final EditText person_position_reg = findViewById(R.id.person_contacts_reg);
        final EditText arsenal_reg = findViewById(R.id.arsenal_reg);

        Switch switch_to_org = findViewById(R.id.switch_to_org);

        switch_to_org.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    nickname_reg.setVisibility(View.INVISIBLE);
                    person_position_reg.setVisibility(View.INVISIBLE);
                    arsenal_reg.setVisibility(View.INVISIBLE);
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

//        findViewById(R.id.birthday_reg).setEnabled(false);

        findViewById(R.id.birthday_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });
    }


    public void callDatePicker() {
        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day = "";
                        String month = "";
                        if (dayOfMonth<10) {
                            day="0"+dayOfMonth;
                        }
                        else {
                            day=""+dayOfMonth;
                        }
                        if (monthOfYear<9) {
                            monthOfYear += 1;
                            month="0"+monthOfYear;
                        }
                        else {
                            monthOfYear += 1;
                            month=""+monthOfYear;
                        }
                        String editTextDateParam = day + "." + month + "." + year;
                        ((TextView) findViewById(R.id.birthday_reg)).setText(editTextDateParam);
                    }
                }, 2000, 0, 1);
        datePickerDialog.show();
    }



    public void addListenerOnButton() {
        Button next = findViewById(R.id.person_info_done);
        next.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //проверка ввода ФИО
                    if (((EditText)findViewById(R.id.person_fio_reg)).getText().toString().equals("")) {
                        Toast.makeText(RegistrationPersonInfo.this, "Введите ФИО",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //проверка ввода даты рождения
                        if (((TextView) findViewById(R.id.birthday_reg)).getText().toString().equals("")) {
                            Toast.makeText(RegistrationPersonInfo.this, "Введите дату рождения",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Проверка флага отвечающего за выбор организатора\игрока

                            if (!f) {
                                if (((EditText) findViewById(R.id.nickname_reg)).getText().toString().length() > 0) {

                                    //-------------записываем информацию об игроке в бд----------------------
                                    playerToDB();

                                    //-------переходим в подключение к команде-------------
                                    Intent i = new Intent(".ConnectingToTeam");
                                    finish();
                                    startActivity(i);
                                } else {
                                    Toast.makeText(RegistrationPersonInfo.this, "Введите позывной",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (f) {
                                orgToDB();
//                                      переходим в подключение к оргкомитету
                                Intent i = new Intent(".ConnectingToOrgcom");
                                finish();
                                startActivity(i);

                            }
                            else {
                                Toast.makeText(RegistrationPersonInfo.this, "Какая-то ошибка",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        );
    }



    public void playerToDB(){
        final String personFIO = ((EditText)findViewById(R.id.person_fio_reg)).getText().toString();
        final String personNickname = ((EditText)findViewById(R.id.nickname_reg)).getText().toString();
        final String personBirthday = ((TextView)findViewById(R.id.birthday_reg)).getText().toString();
        final String personContacts = ((EditText)findViewById(R.id.person_contacts_reg)).getText().toString();
        final String personArsenal = ((EditText)findViewById(R.id.arsenal_reg)).getText().toString();
        final boolean personOrgFlag = false;

        fbData.creatingPlayer(personFIO,personNickname,personBirthday,personContacts,personArsenal,personOrgFlag);

    }

    public void orgToDB(){
        final String personFIO = ((EditText)findViewById(R.id.person_fio_reg)).getText().toString();
        final String personBirthday = ((TextView)findViewById(R.id.birthday_reg)).getText().toString();
        final boolean personOrgFlag = true;

        fbData.creatingOrg(personFIO, personBirthday,personOrgFlag);
    }

}
