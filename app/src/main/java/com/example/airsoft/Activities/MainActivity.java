package com.example.airsoft.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.airsoft.NotificationService;
import com.example.airsoft.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(
                new Intent(MainActivity.this, NotificationService.class));

        addListenerOnButton();


//--------------получение токена для отправки пушей------------------------------------------

        FirebaseMessaging.getInstance().getToken().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("firebase_tag", "Fetching FCM registration token failed", e);

            }
        })

                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("firebase_tag", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d("firebase_tag", token);

                    }
                });


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
                        Intent i = new Intent(".GamesRecyclerActivity");
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
                        builder.setMessage("Вы действительно хотите выйти из системы?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
