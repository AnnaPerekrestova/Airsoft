package com.example.data;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.os.SystemClock.sleep;

//-----Singleton----------------------------------------------------------------------------------
public class FirebaseData {
    private static volatile FirebaseData instance;
    private String teamID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private void FirebaseData(){}

    public static FirebaseData getInstance() {
        // Техника, которую мы здесь применяем называется «блокировка с двойной
        // проверкой» (Double-Checked Locking). Она применяется, чтобы
        // предотвратить создание нескольких объектов-одиночек, если метод будет
        // вызван из нескольких потоков одновременно.
        //
        // Хотя переменная `result` вполне оправданно кажется здесь лишней, она
        // помогает избежать подводных камней реализации DCL в Java.
        //
        // Больше об этой проблеме можно почитать здесь:
        // https://refactoring.guru/ru/java-dcl-issue
        FirebaseData result = instance;
        if (result != null) {
            return result;
        }
        synchronized(FirebaseData.class) {
            if (instance == null) {
                instance = new FirebaseData();
            }
            return instance;
        }
    }

    //------------------------------------------------------------------------------------------------------
    public interface userCallback {
        void onUserUIDChanged(String userUID);
    }
    public interface teamCallback {
        void onTeamIdChanged(String teamKey);
        void onTeamNameChanged(String teamName);
    }

    public interface teamMembersUIDListCallback {
        void onTeamMembersUIDListChanged(List<String> teamMembersUIDList);

    }
    public interface teamMembersDataCallback{
        void onTeamMemberDataChanged(String nickname, String fio);
    }

    public void getUserUID(userCallback callback){
        callback.onUserUIDChanged(FirebaseAuth.getInstance().getUid());
    }

    public void getTeamKey(final teamCallback callback){
        getUserUID(new FirebaseData.userCallback() {
            @Override
            public void onUserUIDChanged(String userUID) {
                DatabaseReference databaseRef = database.getReference("PersonInfo");
                databaseRef.child(userUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot == null) return;
                        callback.onTeamIdChanged(snapshot.child("TeamKey").getValue().toString());
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                       System.out.println("Unable to attach listener");
                       }
                   }
                );
            }
        });

    }

    public void getTeamName(final teamCallback callback){


        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        databaseRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot == null) return;
              else{
                  String teamID = snapshot.child("TeamKey").getValue().toString();

                  final DatabaseReference databaseRef = database.getReference("TeamInfo");
                  databaseRef.child(teamID).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot == null) return;
                              //----выводим название команды:--------
                          else {
                              callback.onTeamNameChanged(snapshot.child("TeamName").getValue().toString());
                          }
                      }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                      }
                  });
              }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
          }
      }
    );

    }

    public void getTeamMembersUIDList(final teamMembersUIDListCallback callback){
        final List<String> membersUIDList = new ArrayList<>();
        getTeamKey(new teamCallback(){

            @Override
            public void onTeamIdChanged(String teamKey) {
                final DatabaseReference databaseReference = database.getReference("PersonInfo");
                final Query databaseQuery = databaseReference.orderByChild("TeamKey").equalTo(teamKey);
                databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot==null)return;
                        for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                            membersUIDList.add(postSnapShot.getKey());
                        }
                        callback.onTeamMembersUIDListChanged(membersUIDList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Error
                        Log.d("Error", "databaseError");
                    }
                });
            }

            @Override
            public void onTeamNameChanged(String teamName) {

            }
        });
    }
    public void getTeamMembersData(final teamMembersDataCallback callback) {
        getTeamMembersUIDList(new teamMembersUIDListCallback() {
            @Override
            public void onTeamMembersUIDListChanged(List<String> teamMembersUIDList) {
                for (final String id: teamMembersUIDList){
                    final DatabaseReference databaseReference = database.getReference("PersonInfo");

                    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2==null)return;
                            String fio =  (String)dataSnapshot2.child("FIO").getValue();
                            String nickname =  (String)dataSnapshot2.child("Nickname").getValue();
                            //addRow(nickname,fio);
                            callback.onTeamMemberDataChanged(nickname,fio);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Error
                            Log.d("Error", "databaseError");
                        }
                    });
                }
            }
        });

    }

}
