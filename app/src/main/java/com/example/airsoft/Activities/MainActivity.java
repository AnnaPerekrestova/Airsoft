package com.example.airsoft.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.airsoft.NotificationService;
import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private String team_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----Получаем значения переданные через intent------------------------------------------------------------


        startService(
                new Intent(MainActivity.this, NotificationService.class));

        addListenerOnButton();

        FirebaseData fbData = new FirebaseData().getInstance();
        fbData.getTeamName(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
            }

            @Override
            public void onTeamNameChanged(String teamName) {
                ((TextView) findViewById(R.id.text_team_name)).setText(teamName);

            }


        });

//

////        //--------Получаем пользователя и на основе этого выводим название команды----------------
//        String userId =FirebaseAuth.getInstance().getUid();
////
////---------получаем по uid ключ команды, к которой присоеденен юзер, записываем в team_key, получам название команды---------------
//
//        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
//        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot == null) return;
//
//                team_key = snapshot.child("TeamKey").getValue().toString();
//
//                final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("TeamInfo");
//                databaseRef.child(team_key).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot == null) return;
//                        //----выводим название команды:--------
//                        else {
//                            ((TextView) findViewById(R.id.text_team_name)).setText(snapshot.child("TeamName").getValue().toString());
//                        }
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        }
//        );
////---------получаем по uid ключ команды, к которой присоеденен юзер, записываем в team_key, получам название команды---------------
//         final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("TeamInfo");
//         databaseRef.child(team_key).addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot snapshot) {
//                 if (snapshot == null) return;
//                     //----выводим название команды:--------
//                 else {
//                     ((TextView) findViewById(R.id.text_team_name)).setText(snapshot.child("TeamName").getValue().toString());
//                 }
//
//             }
//             @Override
//             public void onCancelled(@NonNull DatabaseError error) {
//
//             }
//         });


    }

//--------------------------------------------------------------------------------------------------
    public void addListenerOnButton() {
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
