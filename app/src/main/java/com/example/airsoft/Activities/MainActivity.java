package com.example.airsoft.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        getData();

    }

    //---------------Вывод названия команды----------------------------------------------------------
    public void getData(){
        FirebaseData fbData = new FirebaseData().getInstance();
        fbData.getTeamName(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(final String teamKey) {

            }

            @Override
            public void onTeamNameChanged(String teamName) {
                ((TextView) findViewById(R.id.text_team_name)).setText(teamName);
//---------------Делаем название команды видимым и добавляем информацию о команде при клике---------------------------------------
                findViewById(R.id.text_team_name).setVisibility(View.VISIBLE);



            }
        });
        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(final String teamKey) {
                findViewById(R.id.text_team_name).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(".TeamInfoActivity");
                        i.putExtra("teamKey", teamKey);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onTeamNameChanged(String teamName) {


            }
        });
    }



    //--------------------------------------------------------------------------------------------------
    public void addListenerOnButton(){
        Button buttonMembers = findViewById(R.id.members);
        Button buttonGames = findViewById(R.id.games);
        Button buttonStatistic = findViewById(R.id.stats);
        Button buttonCalendar = findViewById(R.id.calendar);
        ImageButton buttonDelog = findViewById(R.id.delog);

        buttonMembers.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MembersRecyclerActivity");
                        startActivity(i);
                    }
                }

        );
        buttonGames.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".GamesViewSelectActivity");
                        startActivity(i);
                    }
                }

        );
        buttonStatistic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".StatisticActivity");
                        startActivity(i);
                    }
                }

        );
        buttonCalendar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CalendarActivity");
                        startActivity(i);
                    }
                }

        );
        buttonDelog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Выход из системы");
                        builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //---выход из аккаунта------------------------------
                                FirebaseAuth.getInstance().signOut();
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
    }
}