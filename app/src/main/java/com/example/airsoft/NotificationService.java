package com.example.airsoft;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    final String LOG_TAG = "myLogs";
    NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";

//    // Идентификатор уведомления
//    private static final int NOTIFY_ID = 101;
//
//    // Идентификатор канала
//    private static String CHANNEL_ID = "Cat channel";

    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "onCreate");
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");




        someTask();
        return super.onStartCommand(intent, flags, startId);
    }



    public void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }
    private void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    private void someTask() {
        Log.d(LOG_TAG, "some_task");
//---------смотрим на изменения в бд событий в календаре. при появлении нового - вкидываем пуш------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Calendar");
        databaseRef.child("events_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(LOG_TAG, "changed");
                if (dataSnapshot == null) return;

                Log.d(LOG_TAG, "changed "+ dataSnapshot.child("Date").getValue().toString());

//------------------создаем объект, который откроет нам активность календаря при нажатии на уведомление--------------------
                Intent notificationIntent = new Intent(".CalendarActivity");
                PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
//--------------получаем значение даты изменненного дочернего эл-та-----------------------------------------------
                String date = dataSnapshot.child("Date").getValue().toString();
//---------------создаем менеджер уведомлений-------------------------------------------------------------
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//---------------создаем конструктор уведомления--------------------------------------------------------------
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setAutoCancel(false)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(contentIntent)
                                .setContentTitle("Новое")
                                .setContentText("Добавлено новое мероприятие на "+date)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                createChannelIfNeeded(notificationManager);
                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }
}
