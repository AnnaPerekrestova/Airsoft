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

import com.example.data.FirebaseData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    final String LOG_TAG = "myLogs";
    NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    String team_key;
    final FirebaseData fbData = new FirebaseData().getInstance();
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

//        calendarPush();
        newRequestToConnectToMyTeam();
        myRequestStatusChanged();
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
////    private void calendarPush() {
////        Log.d(LOG_TAG, "some_task");
//////---------смотрим на изменения в бд событий в календаре. при появлении нового - вкидываем пуш------------------------
////        String userID = FirebaseAuth.getInstance().getUid();
////        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
////        databaseRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
////
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (snapshot == null) return;
////                else {
////                    team_key = snapshot.child("TeamKey").getValue().toString();
////
////
////                    final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Calendar");
////                    databaseRef.child(team_key).addChildEventListener(new ChildEventListener() {
////                        @Override
////                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                        }
////
////                        @Override
////                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                            Log.d(LOG_TAG, "changed");
////                            if (dataSnapshot == null) return;
////
////                            Log.d(LOG_TAG, "changed " + dataSnapshot.child("Date").getValue().toString());
////
//////------------------создаем объект, который откроет нам активность календаря при нажатии на уведомление--------------------
////                            Intent notificationIntent = new Intent(".CalendarActivity");
////                            PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,
////                                    0, notificationIntent,
////                                    PendingIntent.FLAG_CANCEL_CURRENT);
//////--------------получаем значение даты изменненного дочернего эл-та-----------------------------------------------
////                            String date = dataSnapshot.child("Date").getValue().toString();
//////---------------создаем менеджер уведомлений-------------------------------------------------------------
////                            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//////---------------создаем конструктор уведомления--------------------------------------------------------------
////                            NotificationCompat.Builder notificationBuilder =
////                                    new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
////                                            .setAutoCancel(false)
////                                            .setSmallIcon(R.mipmap.ic_launcher)
////                                            .setWhen(System.currentTimeMillis())
////                                            .setContentIntent(contentIntent)
////                                            .setContentTitle("Новое")
////                                            .setContentText("Добавлено новое мероприятие на " + date)
////                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
////
////                            createChannelIfNeeded(notificationManager);
////                            notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
////
////                        }
////
////                        @Override
////                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////
////                        }
////
////                        @Override
////                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                        }
////
////
////                        @Override
////                        public void onCancelled(DatabaseError databaseError) {
////                            // Error
////                            Log.d("Error", "databaseError");
////                        }
////                    });
////                }
////
////            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//
//    }
    private void newRequestToConnectToMyTeam(){

        fbData.getTeamKey(new FirebaseData.teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestsToConnectTeam");
                final Query databaseQuery = databaseReference.orderByChild("TeamKey").equalTo(teamKey);
                databaseQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.child("Status").getValue().toString().equals("рассматривается")){
                            fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
                                @Override
                                public void onPlayerInfoChanged(String fio, String nickname, String birthday, String position, String arsenal, String teamKey) {
                                    //------------------создаем объект, который откроет нам активность календаря при нажатии на уведомление--------------------
                                    Intent notificationIntent = new Intent(".RequestsToMyTeam");
                                    PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,
                                            0, notificationIntent,
                                            PendingIntent.FLAG_CANCEL_CURRENT);

                                    //---------------создаем менеджер уведомлений-------------------------------------------------------------
                                    notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    //---------------создаем конструктор уведомления--------------------------------------------------------------
                                    NotificationCompat.Builder notificationBuilder =
                                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setAutoCancel(false)
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setContentIntent(contentIntent)
                                                    .setContentTitle("Запрос на вступление в команду")
                                                    .setContentText(fio+" хочет вступить в вашу команду")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                    createChannelIfNeeded(notificationManager);
                                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                                }

                                @Override
                                public void onOrgInfoChanged(String fio, String birthday) {

                                }
                            },snapshot.child("UserUID").getValue().toString());
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        });
    }
    private void myRequestStatusChanged() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestsToConnectTeam");
        final Query databaseQuery = databaseReference.orderByChild("UserUID").equalTo(fbData.getUserUID());
        databaseQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot snapshot, @Nullable String previousChildName) {


                if (snapshot.child("Status").getValue()!=null) {
                    if ((snapshot.child("Status").getValue().toString().equals("отклонена"))) {
                        fbData.getTeamInfo(new FirebaseData.teamInfoCallback() {
                            @Override
                            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                                //------------------создаем объект, который откроет нам активность календаря при нажатии на уведомление--------------------
                                Intent notificationIntent = new Intent(".MyRequests");
                                PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,
                                        0, notificationIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                //---------------создаем менеджер уведомлений-------------------------------------------------------------
                                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                //---------------создаем конструктор уведомления--------------------------------------------------------------
                                NotificationCompat.Builder notificationBuilder =
                                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setAutoCancel(false)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setWhen(System.currentTimeMillis())
                                                .setContentIntent(contentIntent)
                                                .setContentTitle("Статус Вашего запроса изменен")
                                                .setContentText("Ваша заявка в команду "+teamName+" "+snapshot.child("Status").getValue().toString())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                createChannelIfNeeded(notificationManager);
                                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                            }
                        }, snapshot.child("TeamKey").getValue().toString());
                    }
                    if (snapshot.child("Status").getValue().toString().equals("одобрена")){
                        fbData.getTeamInfo(new FirebaseData.teamInfoCallback() {
                            @Override
                            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                                //------------------создаем объект, который откроет нам активность календаря при нажатии на уведомление--------------------
                                Intent notificationIntent = new Intent(".MainActivity");
                                PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,
                                        0, notificationIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                //---------------создаем менеджер уведомлений-------------------------------------------------------------
                                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                //---------------создаем конструктор уведомления--------------------------------------------------------------
                                NotificationCompat.Builder notificationBuilder =
                                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setAutoCancel(false)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setWhen(System.currentTimeMillis())
                                                .setContentIntent(contentIntent)
                                                .setContentTitle("Статус Вашего запроса изменен")
                                                .setContentText("Ваша заявка в команду "+teamName+" "+snapshot.child("Status").getValue().toString())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                createChannelIfNeeded(notificationManager);
                                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                            }
                        }, snapshot.child("TeamKey").getValue().toString());
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
