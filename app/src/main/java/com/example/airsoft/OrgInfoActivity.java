package com.example.airsoft;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.airsoft.Activities.PlayerInfo;
import com.example.data.FirebaseData;

public class OrgInfoActivity extends AppCompatActivity {

    FirebaseData fbData = new FirebaseData().getInstance();
    Button leave_orgcom;
    Button delog;
    Button save;
    TextView org_birthday;
    TextView org_fio;
    TextView org_contacts;
    TextView org_orgcom;
    String personUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_info);

        //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        if (intent.getStringExtra("orgID")==null){
            personUID=fbData.getUserUID();
        }
        else {
            personUID = intent.getStringExtra("orgID");
        }
//------Заполняем  информацию----------------------------------------------------------------------
        getOrgInfo();
        personInfo();

        addListenerOnButton();
    }

    public void personInfo(){
        if (personUID.equals(fbData.getUserUID()))  {
            findViewById(R.id.leave_the_orgcom).setVisibility(View.VISIBLE);
            findViewById(R.id.delog).setVisibility(View.VISIBLE);
            findViewById(R.id.save_my_info).setVisibility(View.VISIBLE);
            findViewById(R.id.org_contacts).setEnabled(true);
        }
        else {
            return;
        }

    }

    public void addListenerOnButton(){
        leave_orgcom  = findViewById(R.id.leave_the_orgcom);
        delog = findViewById(R.id.delog);
        save = findViewById(R.id.save_my_info);
        leave_orgcom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrgInfoActivity.this);
                        builder.setTitle("Выход из оргкомитета");
                        builder.setMessage("Вы действительно хотите покинуть оргкомитет?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbData.LeaveFromOrgcom();
                                finish();
                                Intent i = new Intent(".ConnectingToOrgcom");
                                startActivity(i);
                                // Отпускает диалоговое окно
                            }

                        });
                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Оставить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Отпускает диалоговое окно
                            }

                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

        );
        delog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrgInfoActivity.this);
                        builder.setTitle("Выход из системы");
                        builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbData.deLogin();
                                finish();
                                // Отпускает диалоговое окно
                            }

                        });
                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Оставить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Отпускает диалоговое окно
                            }

                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrgInfoActivity.this);
                builder.setTitle("Сохранить изменения");
                builder.setMessage("Перезаписать данные аккаунта?");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewOrgInfo();
                        // Отпускает диалоговое окно
                    }

                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Оставить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void saveNewOrgInfo(){

    }

    //-----получаем информацию об игроке из БД и заполняем ею элементы----------------------------------------------------
    private void getOrgInfo() {
        //заполняем элементы активности

        fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
            @Override
            public void onPlayerInfoChanged(final String fio, final String nickname, final String birthday, final String contacts, final String arsenal, String teamKey) {

            }

            @Override
            public void onOrgInfoChanged(final String fio, final String birthday, final String contacts, String orgcomKey) {
                if (orgcomKey==null){
                    //--делаем пустой строку с названием команды---------------------------
                    fillOrgInfo(fio, birthday, contacts, "");
                }
                else{
                    fbData.getOrgcomInfo(new FirebaseData.orgcomInfoCallback() {
                        @Override
                        public void onOrgcomInfoChanged(String orgcomName, String orgcomCity, String orgcomDescription) {
                            fillOrgInfo(fio, birthday, contacts, orgcomName);
                        }
                    }, orgcomKey);
                }

            }
        }, personUID );


    }

    //--------заполняем элементы активности-----------------------------------
    @SuppressLint("SetTextI18n")
    private void fillOrgInfo(String fio, String birthday,  String contacts,  String orgcomName) {
        org_fio = (TextView) findViewById(R.id.org_fio);
        org_birthday = (TextView) findViewById(R.id.org_birthday);
        org_contacts = (TextView) findViewById(R.id.org_contacts);
        org_orgcom = (TextView) findViewById(R.id.org_orgcom_name);

        org_fio.setText(fio);
        org_birthday.setText(birthday);
        org_contacts.setText(contacts);
        org_orgcom.setText(orgcomName);

    }

}