package com.example.data;

import android.content.Intent;
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
//    public interface userCallback {
//        void onUserUIDChanged(String userUID);
//    }
    public interface teamCallback {
        void onTeamIdChanged(String teamKey);
        void onTeamNameChanged(String teamName);
    }
    public interface orgcomCallback {
        void onOrgcomIdChanged(String orgcomKey);
        void onOrgcomNameChanged(String orgcomName);
    }

    public interface orgFlagCallback {
        void onOrgFlagChanged(boolean orgFlag);
        void onOrgFlagNull(String no_info);
    }

    public interface teamMembersUIDListCallback {
        void onTeamMembersUIDListChanged(List<String> teamMembersUIDList);

    }
    public interface teamMembersDataCallback{
        void onTeamMemberDataChanged(String playerUID ,String nickname, String fio);
    }

    public interface personInfoCallback{
        void onPlayerInfoChanged(String fio, String nickname, String birthday, String position, String arsenal);
        void onOrgInfoChanged(String fio, String birthday);
    }

    public interface teamInfoCallback{
        void onTeamInfoChanged(String teamName, String teamCity, String teamYear);
    }

    public interface teamsListCallback{
        void onTeamsListChanged(String teamKey ,String teamName, String teamCity, String teamYear);
    }




    public String getUserUID(){
        return (FirebaseAuth.getInstance().getUid());
    }

    public void getTeamKey(final teamCallback callback){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null)return;
                if (snapshot.child("TeamKey").getValue() == null) {
                    callback.onTeamIdChanged("no info");
                    return;
                }
                else {callback.onTeamIdChanged(snapshot.child("TeamKey").getValue().toString());}
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               System.out.println("Unable to attach listener");
               }
           }
        );
    }

    public void getTeamName(final teamCallback callback){


        final DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot == null)return;

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

    public void getOrgcomKey(final orgcomCallback callback){

        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot == null)return;
                 if (snapshot.child("OrgcomKey").getValue() == null) {
                     callback.onOrgcomIdChanged("no info");
                     return;
                 }
                 else {callback.onOrgcomIdChanged(snapshot.child("OrgcomKey").getValue().toString());}
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("Unable to attach listener");
             }
         }
        );

    }


    public void getOrgcomName(final orgcomCallback callback){


        final DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot == null) return;
                  else{
                      String orgcomID = snapshot.child("OrgcomKey").getValue().toString();

                      final DatabaseReference databaseRef = database.getReference("OrgcomInfo");
                      databaseRef.child(orgcomID).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if (snapshot == null) return;
                                  //----выводим название команды:--------
                              else {
                                  callback.onOrgcomNameChanged(snapshot.child("OrgcomName").getValue().toString());
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

    public void getOrgFlag(final orgFlagCallback callback){

        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot == null) return;
                 if (snapshot.getValue() == null) {
                     callback.onOrgFlagNull("no info");
                     return;
                 }
                 else {callback.onOrgFlagChanged((boolean) snapshot.child("OrgFlag").getValue());}
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("Unable to attach listener");
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
                            callback.onTeamMemberDataChanged(id,nickname,fio);
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

    public void getTeamsList(final teamsListCallback callback){
//        final List<String> membersUIDList = new ArrayList<>();
//        String teamName;
//        String teamCity;
//        String teamYear;
//        String teamMeanAge;

        final DatabaseReference databaseReference = database.getReference("TeamInfo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    final String teamKey = postSnapShot.getKey();
                    final DatabaseReference databaseReference = database.getReference("TeamInfo");

                    databaseReference.child(teamKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2==null)return;
                            String teamName =  (String)dataSnapshot2.child("TeamName").getValue();
                            String teamCity =  (String)dataSnapshot2.child("TeamCity").getValue();
                            String teamYear =  (String)dataSnapshot2.child("TeamYear").getValue();
                            //addRow(nickname,fio);
                            callback.onTeamsListChanged(teamKey,teamName,teamCity,teamYear);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Error
                            Log.d("Error", "databaseError");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });

    }

    public interface checkTeamExistCallback {
        void onTeamExistChanged(boolean f, String teamName);
    }

    public void setTeamKeyIfExist(final checkTeamExistCallback callback , final String teamKey){


        final DatabaseReference databaseReference = database.getReference("TeamInfo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot==null)return;
                 if (snapshot.child(teamKey).getValue()!=null){

                     String teamName = snapshot.child(teamKey).child("TeamName").getValue().toString();

                     String userUID = FirebaseAuth.getInstance().getUid();

                     //----------записываем введенный ключ в соответствующее поле в информацию о пользователе------------------------
                     final DatabaseReference databaseRef = database.getReference("PersonInfo");
                     DatabaseReference user_person_info = databaseRef.child(userUID);
                     user_person_info.child("TeamKey").setValue(teamKey);

                     callback.onTeamExistChanged(true,teamName);
                 }
                 else{
                     callback.onTeamExistChanged(false,"");
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
        });



    }

    public interface checkOrgcomExistCallback {
        void onOrgcomExistChanged(boolean f, String orgcomName);
    }

    public void setOrgcomKeyIfExist(final checkOrgcomExistCallback callback, final String orgcomKey){
        final DatabaseReference databaseReference = database.getReference("OrgcomInfo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot==null)return;
                if (snapshot.child(orgcomKey).getValue()!=null){

                    String orgcomName = snapshot.child(orgcomKey).child("OrgcomName").getValue().toString();

                    String userId= FirebaseAuth.getInstance().getUid();

                    //----------записываем введенный ключ в соответствующее поле в информацию о пользователе------------------------
                    final DatabaseReference databaseRef = database.getReference("PersonInfo");
                    DatabaseReference user_person_info = databaseRef.child(userId);
                    user_person_info.child("OrgcomKey").setValue(orgcomKey);

                    callback.onOrgcomExistChanged(true, orgcomName);
                }
                else{
                    callback.onOrgcomExistChanged(false,"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    public void creatingPlayer(String fio, String nickname, String birthday, String position, String arsenal, boolean orgFlag){
        String personUID = FirebaseAuth.getInstance().getUid();

        DatabaseReference db_personFIO;
        DatabaseReference db_personPosition;
        DatabaseReference db_personArsenal;
        DatabaseReference db_personBirthday;
        DatabaseReference db_personNickname;
        DatabaseReference db_personOrg;

        db_personFIO = database.getReference("PersonInfo/"+personUID+"/FIO");
        db_personNickname = database.getReference("PersonInfo/"+personUID+"/Nickname");
        db_personBirthday = database.getReference("PersonInfo/"+personUID+"/Birthday");
        db_personPosition = database.getReference("PersonInfo/"+personUID+"/Position");
        db_personArsenal = database.getReference("PersonInfo/"+personUID+"/Arsenal");
        db_personOrg = database.getReference("PersonInfo/"+personUID+"/OrgFlag");

        db_personFIO.setValue(fio);
        db_personNickname.setValue(nickname);
        db_personBirthday.setValue(birthday);
        db_personPosition.setValue(position);
        db_personArsenal.setValue(arsenal);
        db_personOrg.setValue(orgFlag);
    }

    public void creatingOrg(String fio, String birthday, boolean orgFlag){
        final String personUID = FirebaseAuth.getInstance().getUid();

        DatabaseReference db_personFIO;
        DatabaseReference db_personBirthday;
        DatabaseReference db_personOrg;

        db_personFIO = database.getReference("PersonInfo/"+personUID+"/FIO");
        db_personBirthday = database.getReference("PersonInfo/"+personUID+"/Birthday");
        db_personOrg = database.getReference("PersonInfo/"+personUID+"/OrgFlag");

        db_personFIO.setValue(fio);
        db_personBirthday.setValue(birthday);
        db_personOrg.setValue(orgFlag);
    }

    public String creatingOrgcom(String orgcomName,String orgcomCity){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //--------generate random key-------------------------------------------------------------------------
        String newOrgcomKey = database.getReference("quiz").push().getKey();

        DatabaseReference db_orgcomName;
        DatabaseReference db_orgcomCity;


        db_orgcomName = database.getReference("OrgcomInfo/"+newOrgcomKey+"/OrgcomName");
        db_orgcomCity = database.getReference("OrgcomInfo/"+newOrgcomKey+"/OrgcomCity");

        db_orgcomName.setValue(orgcomName);
        db_orgcomCity.setValue(orgcomCity);

        String userId= FirebaseAuth.getInstance().getUid();

        //----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = database.getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("OrgcomKey").setValue(newOrgcomKey);

        return (newOrgcomKey);
    }

    public String creatingTeam(String teamName,String teamCity, String teamYear){

        //--------generate random key-------------------------------------------------------------------------
        String newTeamKey = database.getReference("quiz").push().getKey();

        DatabaseReference db_teamName;
        DatabaseReference db_teamCity;
        DatabaseReference db_teamYear;


        db_teamName = database.getReference("TeamInfo/"+newTeamKey+"/TeamName");
        db_teamCity = database.getReference("TeamInfo/"+newTeamKey+"/TeamCity");
        db_teamYear = database.getReference("TeamInfo/"+newTeamKey+"/TeamYear");

        db_teamName.setValue(teamName);
        db_teamCity.setValue(teamCity);
        db_teamYear.setValue(teamYear);

        String userId=FirebaseAuth.getInstance().getUid();

        //----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("TeamKey").setValue(newTeamKey);
        return(newTeamKey);
    }

    public void getPersonInfo(final personInfoCallback callback, String personUID){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(personUID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshotInfo) {

                if (dataSnapshotInfo == null) return;
                if ((boolean)dataSnapshotInfo.child("OrgFlag").getValue()== true){
                    String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                    String birthday = (String) dataSnapshotInfo.child("Birthday").getValue();
                    callback.onOrgInfoChanged(fio,birthday);
                }
                if ((boolean)dataSnapshotInfo.child("OrgFlag").getValue()== false){
                    String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                    String arsenal = (String) dataSnapshotInfo.child("Arsenal").getValue();
                    String position = (String) dataSnapshotInfo.child("Position").getValue();
                    String birthday = (String) dataSnapshotInfo.child("Birthday").getValue();
                    String nickname = (String) dataSnapshotInfo.child("Nickname").getValue();
                    callback.onPlayerInfoChanged(fio,nickname,birthday,position,arsenal);

                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    public void getTeamInfo(final teamInfoCallback callback, String teamKey){
        DatabaseReference databaseRef = database.getReference("TeamInfo");
        databaseRef.child(teamKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                else{
                    String teamName = (String) snapshot.child("TeamName").getValue();
                    String teamCity = (String) snapshot.child("TeamCity").getValue();
                    String teamYear = (String) snapshot.child("TeamYear").getValue();
                    callback.onTeamInfoChanged(teamName, teamCity, teamYear);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void requestToConnect(String teamKey){
        //---проверяем, нет ли уже заявки в эту команду со статусом "рассматривается"---------------------


        //--------generate random key-------------------------------------------------------------------------
        String newKey = database.getReference("quiz").push().getKey();
        String userUID=FirebaseAuth.getInstance().getUid();

        DatabaseReference db_userUID;
        DatabaseReference db_teamKey;
        DatabaseReference db_status;
        db_userUID = database.getReference("RequestsToConnectTeam/"+newKey+"/UserUID");
        db_teamKey = database.getReference("RequestsToConnectTeam/"+newKey+"/TeamKey");
        db_status = database.getReference("RequestsToConnectTeam/"+newKey+"/Status");

        db_userUID.setValue(userUID);
        db_teamKey.setValue(teamKey);
        db_status.setValue("рассматривается");

    }

    public interface myRequestsListCallback{
        void onMyRequestsListChanged(String requestKey, String userUID,String teamName ,String status);
    }

    public void getMyRequest(final myRequestsListCallback callback){
        DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(getUserUID());
        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                else
                    {
                        for (DataSnapshot postSnapShot: snapshot.getChildren()) {
                            final String requestKey = snapshot.getValue().toString();
                            String teamKey = (String) postSnapShot.child("TeamKey").getValue();
                            final String status = (String) postSnapShot.child("Status").getValue();

                            getTeamInfo(new teamInfoCallback() {
                                @Override
                                public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                                    callback.onMyRequestsListChanged(requestKey, getUserUID(), teamName, status);
                                }
                            }, teamKey);

                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface requestsToMyTeamListCallback{
        void onRequestsToMyTeamListChanged(String requestKey,String playerUID,String teamName ,String status);
    }

    public void getRequestRequestsToMyTeam(final requestsToMyTeamListCallback callback) {
        final DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        getTeamKey(new teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                final Query databaseQuery = databaseRef.orderByChild("TeamKey").equalTo(teamKey);
                databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot == null) return;
                        else
                        {
                            for (DataSnapshot postSnapShot: snapshot.getChildren()) {
                                final String requestKey = postSnapShot.getKey().toString();
                                String teamKey = (String) postSnapShot.child("TeamKey").getValue();
                                final String playerUID = (String) postSnapShot.child("UserUID").getValue();
                                final String status = (String) postSnapShot.child("Status").getValue();

                                getTeamInfo(new teamInfoCallback() {
                                    @Override
                                    public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
                                        callback.onRequestsToMyTeamListChanged(requestKey, playerUID, teamName, status);
                                    }
                                }, teamKey);

                            }
                        }
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
    public interface changeRequestStatusCallback{
        void onChangeRequestStatus();
    }
    public void approveRequest(final changeRequestStatusCallback callback, final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToConnectTeam");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToConnectTeam/" + requestKey + "/Status");
                    db_status.setValue("одобрена");
                //----записываем игроку команду, в которой приняли заявку, остальные заявки этого пользователя удаляем из бд------------
                    String teamKey =snapshot.child(requestKey).child("TeamKey").getValue().toString();
                    String playerUID =snapshot.child(requestKey).child("UserUID").getValue().toString();

                    //----------записываем ключ в информацию о пользователе------------------------
                    DatabaseReference db_teamKey;
                    db_teamKey = database.getReference("PersonInfo/" + playerUID + "/TeamKey");
                    db_teamKey.setValue(teamKey);


                    DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
                    final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(playerUID);
                    databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapShot: snapshot.getChildren()){
                                postSnapShot.getRef().removeValue();
                                callback.onChangeRequestStatus();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    public void dismissRequest(final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToConnectTeam");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToConnectTeam/" + requestKey + "/Status");
                    db_status.setValue("отклонена");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}