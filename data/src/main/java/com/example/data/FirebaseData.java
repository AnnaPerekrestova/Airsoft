package com.example.data;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//-----Singleton----------------------------------------------------------------------------------
public class FirebaseData {
    private static volatile FirebaseData instance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public FirebaseData(){}

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





//----------------------выход из аккаунта------------------------------
    public void deLogin(){
        FirebaseAuth.getInstance().signOut();
    }

//-----------------------получаем UID текущего пользователя---------------------------------------------------------
    public String getUserUID(){
        return (FirebaseAuth.getInstance().getUid());
    }


//--------------получаем teamKey и TeamName команды, к которой приклеплен активный пользователь-------------------------------------------------------------
    public interface teamCallback {
        void onTeamIdChanged(String teamKey);
        void onTeamNameChanged(String teamName);
    }
    public void getTeamKey(final teamCallback callback, String userUID){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("TeamKey").getValue() == null) {
                    callback.onTeamIdChanged("no info");
                }
                else {callback.onTeamIdChanged(Objects.requireNonNull(snapshot.child("TeamKey").getValue()).toString());}
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {}
           }
        );
    }

    public void getTeamName(final teamCallback callback){
        final DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addListenerForSingleValueEvent(new ValueEventListener() {

          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              String teamID = Objects.requireNonNull(snapshot.child("TeamKey").getValue()).toString();
              final DatabaseReference databaseRef = database.getReference("TeamInfo");
              databaseRef.child(teamID).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      callback.onTeamNameChanged(Objects.requireNonNull(snapshot.child("TeamName").getValue()).toString());

                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {
                  }
              });

          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
          }
      }
    );
    }

//-------------получаем orgcomKey и orgcomName оргкомитета, к которому прикреплен активный пользователь----------------------------------------------------------
    public interface orgcomCallback {
        void onOrgcomIdChanged(String orgcomKey);
        void onOrgcomNameChanged(String orgcomName);
    }

    public void getOrgcomKey(final orgcomCallback callback){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.child("OrgcomKey").getValue() == null) {
                     callback.onOrgcomIdChanged("no info");
                 }
                 else {callback.onOrgcomIdChanged(Objects.requireNonNull(snapshot.child("OrgcomKey").getValue()).toString());}
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
        databaseRef.child(getUserUID()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  String orgcomID = Objects.requireNonNull(snapshot.child("OrgcomKey").getValue()).toString();

                  final DatabaseReference databaseRef = database.getReference("OrgcomInfo");
                  databaseRef.child(orgcomID).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          callback.onOrgcomNameChanged(Objects.requireNonNull(snapshot.child("OrgcomName").getValue()).toString());
                      }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                      }
                  });

              }
              @Override
              public void onCancelled(@NonNull DatabaseError error) {
              }
          }
        );
    }
//---------------------------получаем информацию о оргкомитете по OrgcomKey----------------------------------------------------
    public interface orgcomInfoCallback{
        void onOrgcomInfoChanged(String orgcomName, String orgcomCity, String orgcomDescription);
    }
    public void getOrgcomInfo(final orgcomInfoCallback callback, String orgcomKey){
        DatabaseReference databaseRef = database.getReference("OrgcomInfo");
        databaseRef.child(orgcomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orgcomName = (String) snapshot.child("OrgcomName").getValue();
                String orgcomCity = (String) snapshot.child("OrgcomCity").getValue();
                String orgcomDescription = (String) snapshot.child("OrgcomDescription").getValue();
                callback.onOrgcomInfoChanged(orgcomName, orgcomCity, orgcomDescription);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//----------------------получаем OrgFlag активного пользователя----------------------------------------------------------------------------
    public interface orgFlagCallback {
        void onOrgFlagChanged(boolean orgFlag);
        void onOrgFlagNull(String no_info);
    }

    public void getOrgFlag(final orgFlagCallback callback){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.getValue() == null) {
                     callback.onOrgFlagNull("no info");
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

//---------получаем список UID пользователей, присоединенных к команде teamKey-----------------------------
    public interface teamMembersUIDListCallback {
        void onTeamMembersUIDListChanged(List<String> teamMembersUIDList);
    }

    private void getTeamMembersUIDList(final teamMembersUIDListCallback callback, String teamKey){
        final List<String> membersUIDList = new ArrayList<>();
        final DatabaseReference databaseReference = database.getReference("PersonInfo");
        final Query databaseQuery = databaseReference.orderByChild("TeamKey").equalTo(teamKey);
        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    membersUIDList.add(postSnapShot.getKey());
                }
                callback.onTeamMembersUIDListChanged(membersUIDList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });

    }
//-------по каждому участнику команды, к которой прикреплен текущий пользователь, получаем фио и ник-----------------
    public interface teamMembersDataCallback{
        void onTeamMemberDataChanged(String playerUID ,String nickname, String fio);
    }
    public void getTeamMembersData(final teamMembersDataCallback callback, String teamKey) {
        getTeamMembersUIDList(new teamMembersUIDListCallback() {
            @Override
            public void onTeamMembersUIDListChanged(List<String> teamMembersUIDList) {
                for (final String id: teamMembersUIDList){
                    final DatabaseReference databaseReference = database.getReference("PersonInfo");

                    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            String fio =  (String)dataSnapshot2.child("FIO").getValue();
                            String nickname =  (String)dataSnapshot2.child("Nickname").getValue();
                            callback.onTeamMemberDataChanged(id,nickname,fio);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Error
                            Log.d("Error", "databaseError");
                        }
                    });
                }
            }
        }, teamKey);
    }

//-------------------------------получаем список всех команд в БД---------------------------------------------------------
    public interface teamsListCallback{
        void onTeamsListChanged(String teamKey ,String teamName, String teamCity, String teamYear);
        void onTeamsListChanged();
    }
    public void getTeamsList(final teamsListCallback callback){
        final DatabaseReference databaseReference = database.getReference("TeamInfo");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String teamKey = snapshot.getKey();
                if (teamKey != null) {
                    String teamName =  (String)snapshot.child("TeamName").getValue();
                    String teamCity =  (String)snapshot.child("TeamCity").getValue();
                    String teamYear =  (String)snapshot.child("TeamYear").getValue();
                    if (teamName!=null & teamCity!=null & teamYear!=null) {
                        callback.onTeamsListChanged(teamKey, teamName, teamCity, teamYear);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String teamKey = snapshot.getKey();
                if (teamKey != null) {
                    String teamName =  (String)snapshot.child("TeamName").getValue();
                    String teamCity =  (String)snapshot.child("TeamCity").getValue();
                    String teamYear =  (String)snapshot.child("TeamYear").getValue();
                    if (teamName!=null & teamCity!=null & teamYear!=null) {
                        callback.onTeamsListChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback.onTeamsListChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//-------------------------устанавливаем игроку ключ команды (при присоединении по ключу)---------------------------------------------------
    public interface checkTeamExistCallback {
        void onTeamExistChanged(boolean f, String teamName);
    }
    public void setTeamKeyIfExist(final checkTeamExistCallback callback , final String teamKey){
        if (!teamKey.equals("")) {
            final DatabaseReference databaseReference = database.getReference("TeamInfo");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(teamKey).getValue() != null) {

                        String teamName = Objects.requireNonNull(snapshot.child(teamKey).child("TeamName").getValue()).toString();
                        String userUID = getUserUID();

                        //----------записываем введенный ключ в соответствующее поле в информацию о пользователе------------------------
                        final DatabaseReference databaseRef = database.getReference("PersonInfo");
                        DatabaseReference user_person_info = databaseRef.child(userUID);
                        user_person_info.child("TeamKey").setValue(teamKey);

                        //-------------------удаление всех заявок со статусом "рассматривается" пользователя, который вступил в команду---------------------------
                        DatabaseReference requests = database.getReference("RequestsToConnectTeam");
                        final Query databaseQuery = requests.orderByChild("UserUID").equalTo(userUID);
                        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                                    if (Objects.requireNonNull(postSnapShot.child("Status").getValue()).toString().equals("рассматривается")) {
                                        postSnapShot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        callback.onTeamExistChanged(true, teamName);
                    } else {
                        callback.onTeamExistChanged(false, "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
//-------------------------устанавливаем пользователю ключ оргкомитета (при присоединении по ключу)---------------------------------------------------
    public interface checkOrgcomExistCallback {
        void onOrgcomExistChanged(boolean f, String orgcomName);
    }

    public void setOrgcomKeyIfExist(final checkOrgcomExistCallback callback, final String orgcomKey){
        final DatabaseReference databaseReference = database.getReference("OrgcomInfo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(orgcomKey).getValue()!=null){
                    String orgcomName = Objects.requireNonNull(snapshot.child(orgcomKey).child("OrgcomName").getValue()).toString();
                    String userId= getUserUID();

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
//---------------создание в БД записи о новом пользователе (игроке)-------------------------------------------------------------
    public void creatingPlayer(String fio, String nickname, String birthday, String contacts, String arsenal, boolean orgFlag){
        String personUID = getUserUID();

        DatabaseReference db_personFIO = database.getReference("PersonInfo/"+personUID+"/FIO");
        DatabaseReference db_personNickname = database.getReference("PersonInfo/"+personUID+"/Nickname");
        DatabaseReference db_personBirthday = database.getReference("PersonInfo/"+personUID+"/Birthday");
        DatabaseReference db_personContacts = database.getReference("PersonInfo/"+personUID+"/Contacts");
        DatabaseReference db_personArsenal = database.getReference("PersonInfo/"+personUID+"/Arsenal");
        DatabaseReference db_personOrg = database.getReference("PersonInfo/"+personUID+"/OrgFlag");

        db_personFIO.setValue(fio);
        db_personNickname.setValue(nickname);
        db_personBirthday.setValue(birthday);
        db_personContacts.setValue(contacts);
        db_personArsenal.setValue(arsenal);
        db_personOrg.setValue(orgFlag);
    }

//---------------создание в БД записи о новом пользователе (организаторе)-------------------------------------------------------------
    public void creatingOrg(String fio, String birthday, boolean orgFlag){
        final String personUID = getUserUID();

        DatabaseReference db_personFIO = database.getReference("PersonInfo/"+personUID+"/FIO");
        DatabaseReference db_personBirthday = database.getReference("PersonInfo/"+personUID+"/Birthday");
        DatabaseReference db_personOrg = database.getReference("PersonInfo/"+personUID+"/OrgFlag");

        db_personFIO.setValue(fio);
        db_personBirthday.setValue(birthday);
        db_personOrg.setValue(orgFlag);
    }

//---------------создание в БД записи о новом оргкомитете-------------------------------------------------------------
    public String creatingOrgcom(String orgcomName,String orgcomCity, String orgcomDescription){

        //--------generate random key-------------------------------------------------------------------------
        String newOrgcomKey = database.getReference("quiz").push().getKey();

        DatabaseReference db_orgcomName = database.getReference("OrgcomInfo/"+newOrgcomKey+"/OrgcomName");
        DatabaseReference db_orgcomCity = database.getReference("OrgcomInfo/"+newOrgcomKey+"/OrgcomCity");
        DatabaseReference db_orgcomDescription = database.getReference("OrgcomInfo/"+newOrgcomKey+"/OrgcomDescription");

        db_orgcomName.setValue(orgcomName);
        db_orgcomCity.setValue(orgcomCity);
        db_orgcomDescription.setValue(orgcomDescription);

        String userId= getUserUID();

        //----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = database.getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("OrgcomKey").setValue(newOrgcomKey);
        return (newOrgcomKey);
    }

//---------------создание в БД записи о новой команде--------------------------------------------------------------------
    public String creatingTeam(String teamName,String teamCity, String teamYear, String teamDescription){
        //--------generate random key-------------------------------------------------------------------------
        String newTeamKey = database.getReference("quiz").push().getKey();

        DatabaseReference db_teamName = database.getReference("TeamInfo/"+newTeamKey+"/TeamName");
        DatabaseReference db_teamCity = database.getReference("TeamInfo/"+newTeamKey+"/TeamCity");
        DatabaseReference db_teamYear = database.getReference("TeamInfo/"+newTeamKey+"/TeamYear");
        DatabaseReference db_teamDescription = database.getReference("TeamInfo/"+newTeamKey+"/TeamDescription");

        db_teamName.setValue(teamName);
        db_teamCity.setValue(teamCity);
        db_teamYear.setValue(teamYear);
        db_teamDescription.setValue(teamDescription);

        String userId=getUserUID();

        //----------записываем сгенерированный ключ в соответствующее поле в информацию о пользователе------------------------
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        DatabaseReference user_person_info = databaseRef.child(userId);
        user_person_info.child("TeamKey").setValue(newTeamKey);

        //-------------------удаление всех заявок со статусом "рассматривается" пользователя, который вступил в команду---------------------------
        DatabaseReference requests = database.getReference("RequestsToConnectTeam");
        final Query databaseQuery = requests.orderByChild("UserUID").equalTo(userId);
        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot: snapshot.getChildren()){
                    if (Objects.requireNonNull(postSnapShot.child("Status").getValue()).toString().equals("рассматривается")){
                        postSnapShot.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return(newTeamKey);
    }
// ----------Обновление информации о команде-------------------
    public void changeTeamInfo(String teamDescription, String teamKey) {
        DatabaseReference db_teamDescription = database.getReference("TeamInfo/" + teamKey + "/TeamDescription");
        db_teamDescription.setValue(teamDescription);
    }

//-------------------------получаем информацию об игроке по UID----------------------------------------------------
    public interface personInfoCallback{
        void onPlayerInfoChanged(String fio, String nickname, String birthday, String contacts, String arsenal, String teamKey);
        void onOrgInfoChanged(String fio, String birthday, String contacts, String orgcomKey);
    }
    public void getPersonInfo(final personInfoCallback callback, String personUID){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(personUID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotInfo) {
                if ((boolean) dataSnapshotInfo.child("OrgFlag").getValue()){
                    String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                    String birthday = (String) dataSnapshotInfo.child("Birthday").getValue();
                    String contacts = (String) dataSnapshotInfo.child("Contacts").getValue();
                    String orgcomKey = (String) dataSnapshotInfo.child("OrgcomKey").getValue();
                    callback.onOrgInfoChanged(fio,birthday, contacts, orgcomKey);
                }
                if (!((boolean) dataSnapshotInfo.child("OrgFlag").getValue())){
                    String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                    String arsenal = (String) dataSnapshotInfo.child("Arsenal").getValue();
                    String contacts = (String) dataSnapshotInfo.child("Contacts").getValue();
                    String birthday = (String) dataSnapshotInfo.child("Birthday").getValue();
                    String nickname = (String) dataSnapshotInfo.child("Nickname").getValue();
                    String teamKey = (String) dataSnapshotInfo.child("TeamKey").getValue();
                    callback.onPlayerInfoChanged(fio,nickname,birthday,contacts,arsenal,teamKey);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }
//---------------------------получаем информацию о команде по teamKey----------------------------------------------------
    public interface teamInfoCallback{
        void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription);
    }
    public void getTeamInfo(final teamInfoCallback callback, String teamKey){
        DatabaseReference databaseRef = database.getReference("TeamInfo");
        databaseRef.child(teamKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String teamName = (String) snapshot.child("TeamName").getValue();
                String teamCity = (String) snapshot.child("TeamCity").getValue();
                String teamYear = (String) snapshot.child("TeamYear").getValue();
                String teamDescription = (String) snapshot.child("TeamDescription").getValue();
                callback.onTeamInfoChanged(teamName, teamCity, teamYear, teamDescription);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//------------------проверяем, нет ли уже в эту команду, соответствующую переданному teamKey -----------------------------
//-----------------------заявки со статусом "рассматривается"-----------------------------------------------------------------
    public interface checkPersonsRequestsCallback{
        void onPersonRequestsResultChanged(boolean res);
    }
    public void checkPersonRequests(final checkPersonsRequestsCallback callback, final String teamKey) {
        final boolean[] f = {false};
        //---проверяем, нет ли уже заявки в эту команду со статусом "рассматривается"---------------------
        DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(getUserUID());
        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(postSnapShot.child("TeamKey").getValue()).toString().equals(teamKey)){
                        if (Objects.requireNonNull(postSnapShot.child("Status").getValue()).toString().equals("рассматривается")) {
                            f[0] = true;
                        }
                    }
                }
                callback.onPersonRequestsResultChanged(f[0]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
//----------создание в БД новой записи заявки от текущего пользователя в команду teamKey-----------------------------
    public void requestToConnect(String teamKey){
        //--------generate random key-------------------------------------------------------------------------
        String newKey = database.getReference("quiz").push().getKey();
        String userUID=getUserUID();

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

//------------------получение списка заявок текущего пользователя-------------------------------------------------------
//-----------------проверяем наличие изменений в списке заявок от текущего пользователя-------------------------------------
    public interface myRequestsListIfChangedCallback{
        void onMyRequestsListChanged();
        void onMyRequestsListChanged(String requestKey, String userUID,String teamName ,String status);
    }
    public void getMyRequestsIfChanged(final myRequestsListIfChangedCallback callback) {
        final DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        getTeamKey(new teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(getUserUID());
                databaseQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        final String requestKey = snapshot.getKey();
                        String teamKey = (String) snapshot.child("TeamKey").getValue();
                        final String status = (String) snapshot.child("Status").getValue();

                        if (teamKey!=null & status!=null){
                            getTeamInfo(new teamInfoCallback() {
                                @Override
                                public void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription) {
                                    callback.onMyRequestsListChanged(requestKey, getUserUID(), teamName, status);
                                }
                            }, teamKey);
                        }


                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.child("Status").getValue()!=null){
                            callback.onMyRequestsListChanged();
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        callback.onMyRequestsListChanged();
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
        }, getUserUID());
    }
//--------------получаем список заявок в команду, к которой прикреплен текущий пользователь-------------------------------
//------------два callback'a - один для всех заявок, другой только для тех, которые рассматриваются----------------
//-------------проверяем изменения в списке заявок в команду, к которой прикреплен текущий пользователь--------------------
    public interface requestsToMyTeamListIfChangedCallback{
        void onRequestsToMyTeamListChanged();
        void onAllRequestsToMyTeamListChanged(String requestKey,String playerUID,String teamName ,String status);
        void onFilteredRequestsToMyTeamListChanged(String requestKey,String playerUID,String teamName ,String status);
    }
    public void getRequestRequestsToMyTeamIfChanged(final requestsToMyTeamListIfChangedCallback callback) {
        final DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        getTeamKey(new teamCallback() {
            @Override
            public void onTeamIdChanged(String teamKey) {
                final Query databaseQuery = databaseRef.orderByChild("TeamKey").equalTo(teamKey);
                databaseQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        final String requestKey = snapshot.getKey();
                        String teamKey = (String) snapshot.child("TeamKey").getValue();
                        final String playerUID = (String) snapshot.child("UserUID").getValue();
                        final String status = (String) snapshot.child("Status").getValue();
                        if (status!=null & playerUID!=null & teamKey!=null) {

                            getTeamInfo(new teamInfoCallback() {
                                @Override
                                public void onTeamInfoChanged(String teamName, String teamCity, String teamYear, String teamDescription) {
                                    callback.onAllRequestsToMyTeamListChanged(requestKey, playerUID, teamName, status);
                                    if (status.equals("рассматривается")) {
                                        callback.onFilteredRequestsToMyTeamListChanged(requestKey, playerUID, teamName, status);
                                    }
                                }
                            }, teamKey);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.child("Status").getValue()!=null){
                            callback.onRequestsToMyTeamListChanged();}
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        callback.onRequestsToMyTeamListChanged();
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
        },getUserUID());
    }
//------------проверяем смену статуса заявки, обрабатываем одобрение и отклонение заявки------------------------------
    public interface changeRequestStatusCallback{
        void onChangeRequestStatus(boolean f);
    }
    public void approveRequest(final changeRequestStatusCallback callback, final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToConnectTeam");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToConnectTeam/" + requestKey + "/Status");
                    db_status.setValue("одобрена");
                //----записываем игроку команду, в которой приняли заявку, остальные заявки этого пользователя удаляем из бд------------
                    String teamKey = Objects.requireNonNull(snapshot.child(requestKey).child("TeamKey").getValue()).toString();
                    String playerUID = Objects.requireNonNull(snapshot.child(requestKey).child("UserUID").getValue()).toString();

                    //----------записываем ключ в информацию о пользователе------------------------
                    DatabaseReference db_teamKey;
                    db_teamKey = database.getReference("PersonInfo/" + playerUID + "/TeamKey");
                    db_teamKey.setValue(teamKey);
                    callback.onChangeRequestStatus(true);

//-------------------удаление всех заявок со статусом "рассматривается" пользователя, который вступил в команду---------------------------
                    DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
                    final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(playerUID);
                    databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapShot: snapshot.getChildren()){
                                if (Objects.requireNonNull(postSnapShot.child("Status").getValue()).toString().equals("рассматривается")){
                                    postSnapShot.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }else {
                    callback.onChangeRequestStatus(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    public void dismissRequest(final changeRequestStatusCallback callback,final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToConnectTeam");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToConnectTeam/" + requestKey + "/Status");
                    db_status.setValue("отклонена");
                    callback.onChangeRequestStatus(true);
                }else{
                    callback.onChangeRequestStatus(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//------------------------проверяем одобрения заявки для текущего пользователя------------------------------------
    public interface onRequestApproveCallback{
        void onRequestApprove();
    }
    public void onRequestApprove(final onRequestApproveCallback callback){
        final DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(getUserUID());
        databaseQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("Status").getValue()!=null) {
                    if (Objects.requireNonNull(snapshot.child("Status").getValue()).toString().equals("одобрена")){
                    callback.onRequestApprove();}
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
    public void cancelRequest(String requestKey){
        DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
        databaseRef.child(requestKey).getRef().removeValue();
    }

//----------------------покинуть команду (удаляем ключ-значение TeamKey)---------------------------------------------
    public void LeaveFromTeam(){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).child("TeamKey").getRef().removeValue();
    }
//----------------------покинуть оргкомитет (удаляем ключ-значение OrgcomKey)---------------------------------------------
    public void LeaveFromOrgcom(){
        DatabaseReference databaseRef = database.getReference("PersonInfo");
        databaseRef.child(getUserUID()).child("OrgcomKey").getRef().removeValue();
    }

//---------------------создание в БД новой записи о полигоне-------------------------------------------------------------
    public void creatingNewPolygon(final String polygonName, final String polygonAddress, final String polygonDescription, final Double polygonLatitude, final Double polygonLongitude){
        getOrgcomKey(new orgcomCallback() {
            @Override
            public void onOrgcomIdChanged(String orgcomKey) {
                //--------generate random key-------------------------------------------------------------------------
                String newPolygonKey = database.getReference("quiz").push().getKey();

                DatabaseReference db_polygonOrgcomID = database.getReference("Polygons/"+newPolygonKey+"/PolygonOrgcomID");
                DatabaseReference db_polygonName = database.getReference("Polygons/"+newPolygonKey+"/PolygonName");
                DatabaseReference db_polygonAddress = database.getReference("Polygons/"+newPolygonKey+"/PolygonAddress");
//                DatabaseReference db_polygonActuality = database.getReference("Polygons/"+newPolygonKey+"/PolygonActuality");
                DatabaseReference db_polygonDescription = database.getReference("Polygons/"+newPolygonKey+"/PolygonDescription");
                DatabaseReference db_polygonLatitude = database.getReference("Polygons/"+newPolygonKey+"/PolygonLatitude");
                DatabaseReference db_polygonLongitude = database.getReference("Polygons/"+newPolygonKey+"/PolygonLongitude");

                db_polygonOrgcomID.setValue(orgcomKey);
                db_polygonName.setValue(polygonName);
                db_polygonAddress.setValue(polygonAddress);
//                db_polygonActuality.setValue(true);
                db_polygonDescription.setValue(polygonDescription);
                db_polygonLatitude.setValue(polygonLatitude);
                db_polygonLongitude.setValue(polygonLongitude);
            }

            @Override
            public void onOrgcomNameChanged(String orgcomName) {}
        });
    }
    //-------------------------------получаем список всех команд в БД---------------------------------------------------------
    public interface orgcomPolygonListCallback{
        void onOrgcomPolygonListChanged(String polygonKey,String polygonName, String polygonAddress,
                                  String polygonOrgcomID, String polygonDescription, Double polygonLatitude, Double polygonLongitude);
        void onOrgcomPolygonListChanged();
        void onPolygonNamesListChanged(List<String> polygonNamesList);
    }
    public void getOrgcomPolygonsList(final orgcomPolygonListCallback callback){
        getOrgcomKey(new orgcomCallback() {
            @Override
            public void onOrgcomIdChanged(final String orgcomKey) {
                final List<String> polygonNamesList = new ArrayList<>();
                final DatabaseReference databaseReference = database.getReference("Polygons");
                final Query databaseQuery = databaseReference.orderByChild("PolygonOrgcomID").equalTo(orgcomKey);
                databaseQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        final String polygonKey = snapshot.getKey();
                        if (polygonKey != null) {
                            String polygonName =  (String)snapshot.child("PolygonName").getValue();
                            String polygonAddress =  (String)snapshot.child("PolygonAddress").getValue();
//                            boolean polygonActuality =  (boolean) snapshot.child("PolygonActuality").getValue();
                            String polygonDescription =  (String)snapshot.child("PolygonDescription").getValue();
                            Double polygonLatitude = (Double)snapshot.child("PolygonLatitude").getValue();
                            Double polygonLongitude = (Double)snapshot.child("PolygonLongitude").getValue();
                            if (polygonName!=null & polygonAddress!=null & polygonDescription!=null) {
                                callback.onOrgcomPolygonListChanged(polygonKey, polygonName, polygonAddress, orgcomKey,  polygonDescription, polygonLatitude, polygonLongitude);
                                polygonNamesList.add(polygonName);
                            }
                        }
                        callback.onPolygonNamesListChanged(polygonNamesList);
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String polygonName =  (String)snapshot.child("PolygonName").getValue();
                        String polygonAddress =  (String)snapshot.child("PolygonAddress").getValue();
                        String polygonDescription =  (String)snapshot.child("PolygonDescription").getValue();
                        if (polygonName!=null & polygonAddress!=null & polygonDescription!=null) {
                            callback.onOrgcomPolygonListChanged();
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        callback.onOrgcomPolygonListChanged();
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
            public void onOrgcomNameChanged(String orgcomName) {}
        });
    }
    public interface polygonInfoByNameCallback{
        void onPolygonIDByNameChanged(String polygonID);

        void onPolygonAddressByNameChanged(String polygonAddress);
    }
    public void getPolygonInfoByName(final polygonInfoByNameCallback callback, String polygonName){
        final DatabaseReference databaseReference = database.getReference("Polygons");
        final Query databaseQuery = databaseReference.orderByChild("PolygonName").equalTo(polygonName);
        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot: snapshot.getChildren()){
                    callback.onPolygonIDByNameChanged(postSnapShot.getKey());

                    callback.onPolygonAddressByNameChanged(Objects.requireNonNull(postSnapShot.child("PolygonAddress").getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public interface polygonInfoCallback{
        void onPolygonInfoChanged(String polygonName, String polygonAddress, String polygonOrgcomID,  String polygonDescription, Double polygonLatitude, Double polygonLongitude);
    }
    public void getPolygonInfo(final polygonInfoCallback callback, String polygonID){
        final DatabaseReference databaseReference = database.getReference("Polygons");
        databaseReference.child(polygonID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String polygonName = (String) snapshot.child("PolygonName").getValue();
                    String polygonAddress = (String)snapshot.child("PolygonAddress").getValue();
                    String polygonOrgcomID = (String)snapshot.child("PolygonOrgcomID").getValue();
//                    boolean polygonActuality = (boolean)postSnapShot.child("PolygonActuality").getValue();
                    String polygonDescription = (String) snapshot.child("PolygonDescription").getValue();
                    Double polygonLatitude = (Double)snapshot.child("PolygonLatitude").getValue();
                    Double polygonLongitude = (Double)snapshot.child("PolygonLongitude").getValue();
                    callback.onPolygonInfoChanged(polygonName,polygonAddress,polygonOrgcomID,polygonDescription,polygonLatitude,polygonLongitude);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void creatingNewGame(final String gameName,final String gameDate, final String gamePolygonID, final String gameDescription, final String gameSides){
        getOrgcomKey(new orgcomCallback() {
            @Override
            public void onOrgcomIdChanged(String orgcomKey) {
                //--------generate random key-------------------------------------------------------------------------
                String newGameKey = database.getReference("quiz").push().getKey();

                DatabaseReference db_gameOrgcomID = database.getReference("Games/"+newGameKey+"/GameOrgcomID");
                DatabaseReference db_gameName = database.getReference("Games/"+newGameKey+"/GameName");
                DatabaseReference db_gameDate = database.getReference("Games/"+newGameKey+"/GameDate");
                DatabaseReference db_gamePolygonID = database.getReference("Games/"+newGameKey+"/GamePolygonID");
                DatabaseReference db_gameStatus = database.getReference("Games/"+newGameKey+"/GameStatus");
                DatabaseReference db_gameDescription = database.getReference("Games/"+newGameKey+"/GameDescription");
                DatabaseReference db_gameSides = database.getReference("Games/"+newGameKey+"/GameSides");

                db_gameOrgcomID.setValue(orgcomKey);
                db_gameName.setValue(gameName);
                db_gameDate.setValue(gameDate);
                db_gamePolygonID.setValue(gamePolygonID);
                db_gameStatus.setValue("открыт набор на игру");
                db_gameDescription.setValue(gameDescription);
                db_gameSides.setValue(gameSides);
            }

            @Override
            public void onOrgcomNameChanged(String orgcomName) {}
        });
    }
//------------получаем список всех игр оргкомитета: на которые идет набор, на которые набор закрыт,-------------------------
// ----------------------отмененные и прошедшие (у прошедших есть победитель)------------------------------------------------------
    public interface gamesOfOrgcomCallback{
        void onOpeningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription);
        void onClosedGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                          String polygonID, String gameStatus, String gameDescription);
        void onRunningGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                      String polygonID, String gameStatus, String gameDescription);
        void onHappensGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription, String gameWinner);
        void onCancelledGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription);
        void onOpeningGamesOfOrgcomChanged();
        void onClosedGamesOfOrgcomChanged();
        void onRunningGamesOfOrgcomChanged();
        void onHappensGamesOfOrgcomChanged();
        void onCancelledGamesOfOrgcomChanged();
    }
    public void gamesOfOrgcom(final gamesOfOrgcomCallback callback){
        getOrgcomKey(new orgcomCallback() {
            @Override
            public void onOrgcomIdChanged(final String orgcomKey) {
                DatabaseReference games = database.getReference("Games");
                final Query databaseQuery = games.orderByChild("GameOrgcomID").equalTo(orgcomKey);
                databaseQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        final String gameKey = snapshot.getKey();
                        final String gameName= (String) snapshot.child("GameName").getValue();
                        final String gameDate = (String) snapshot.child("GameDate").getValue();
                        final String gamePolygonID = (String) snapshot.child("GamePolygonID").getValue();
                        final String gameStatus = (String) snapshot.child("GameStatus").getValue();
                        final String gameDescription = (String) snapshot.child("GameDescription").getValue();


                        if (gameKey!=null & gameName!=null & gameDate!=null & gamePolygonID!=null & gameStatus!=null & gameDescription!=null){
                            if (gameStatus.equals("открыт набор на игру")){
                                callback.onOpeningGamesOfOrgcomChanged(gameKey,orgcomKey,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                            }
                            if (gameStatus.equals("набор на игру закрыт")){
                                callback.onClosedGamesOfOrgcomChanged(gameKey,orgcomKey,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                            }
                            if (gameStatus.equals("игра отменена")){
                                callback.onCancelledGamesOfOrgcomChanged(gameKey,orgcomKey,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                            }
                            if (gameStatus.equals("игра идет")){
                                callback.onRunningGamesOfOrgcomChanged(gameKey,orgcomKey,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                            }
                            if (gameStatus.equals("игра прошла")){
                                final String gameWinner = (String) snapshot.child("GameWinner").getValue();
                                callback.onHappensGamesOfOrgcomChanged(gameKey,orgcomKey,gameName,gameDate,gamePolygonID,gameStatus,gameDescription,gameWinner);
                            }
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        final String gameKey = snapshot.getKey();
                        final String gameName= (String) snapshot.child("GameName").getValue();
                        final String gameDate = (String) snapshot.child("GameDate").getValue();
                        final String gamePolygonID = (String) snapshot.child("GamePolygonID").getValue();
                        final String gameStatus = (String) snapshot.child("GameStatus").getValue();
                        final String gameDescription = (String) snapshot.child("GameDescription").getValue();


                        if (gameKey!=null & gameName!=null & gameDate!=null & gamePolygonID!=null & gameStatus!=null & gameDescription!=null){
                            if (gameStatus.equals("открыт набор на игру")){
                                callback.onOpeningGamesOfOrgcomChanged();
                            }
                            if (gameStatus.equals("набор на игру закрыт")){
                                callback.onClosedGamesOfOrgcomChanged();
                            }
                            if (gameStatus.equals("игра отменена")){
                                callback.onCancelledGamesOfOrgcomChanged();
                            }
                            if (gameStatus.equals("игра идет")) {
                                callback.onRunningGamesOfOrgcomChanged();
                            }
                            if (gameStatus.equals("игра прошла")) {
                                callback.onHappensGamesOfOrgcomChanged();
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        callback.onOpeningGamesOfOrgcomChanged();
                        callback.onClosedGamesOfOrgcomChanged();
                        callback.onCancelledGamesOfOrgcomChanged();
                        callback.onRunningGamesOfOrgcomChanged();
                        callback.onHappensGamesOfOrgcomChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            @Override
            public void onOrgcomNameChanged(String orgcomName) {}
        });
    }
    //------------получаем список всех игр команды: на которые идет набор, на которые набор закрыт,-------------------------
// ----------------------отмененные и прошедшие (у прошедших есть победитель)------------------------------------------------------
    public interface gamesOfTeamCallback{
        void onOpeningGamesOfTeamChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription);
        void onClosedGamesOfTeamChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                          String polygonID, String gameStatus, String gameDescription);
        void onRunningGamesOfTeamChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription);
        void onHappensGamesOfTeamChanged(String gameKey, String orgcomID, String gameName, String gameDate,
                                           String polygonID, String gameStatus, String gameDescription, String gameWinner);
//        void onCancelledGamesOfOrgcomChanged(String gameKey, String orgcomID, String gameName, String gameDate,
//                                             String polygonID, String gameStatus, String gameDescription);
        void onOpeningGamesOfTeamChanged();
        void onClosedGamesOfTeamChanged();
        void onRunningGamesOfTeamChanged();
        void onHappensGamesOfTeamChanged();
//        void onCancelledGamesOfTeamChanged();
    }
    public void gamesOfTeam(final gamesOfTeamCallback callback){
        DatabaseReference games = database.getReference("Games");
        games.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                final String gameKey = snapshot.getKey();
                final String gameName= (String) snapshot.child("GameName").getValue();
                final String gameOrgcomID = (String) snapshot.child("GameOrgcomID").getValue();
                final String gameDate = (String) snapshot.child("GameDate").getValue();
                final String gamePolygonID = (String) snapshot.child("GamePolygonID").getValue();
                final String gameStatus = (String) snapshot.child("GameStatus").getValue();
                final String gameDescription = (String) snapshot.child("GameDescription").getValue();


                if (gameKey!=null & gameName!=null & gameDate!=null & gamePolygonID!=null & gameStatus!=null & gameDescription!=null){
                    if (gameStatus.equals("открыт набор на игру")){
                        callback.onOpeningGamesOfTeamChanged(gameKey,gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                    }
                    if (gameStatus.equals("набор на игру закрыт")){
                        callback.onClosedGamesOfTeamChanged(gameKey,gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                    }
//                    if (gameStatus.equals("игра отменена")){
//                        callback.(gameKey,gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
//                    }
                    if (gameStatus.equals("игра идет")){
                        callback.onRunningGamesOfTeamChanged(gameKey,gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription);
                    }
                    if (gameStatus.equals("игра прошла")){
                        final String gameWinner = (String) snapshot.child("GameWinner").getValue();
                        callback.onHappensGamesOfTeamChanged(gameKey,gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription,gameWinner);
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String gameKey = snapshot.getKey();
                final String gameName= (String) snapshot.child("GameName").getValue();
                final String gameDate = (String) snapshot.child("GameDate").getValue();
                final String gamePolygonID = (String) snapshot.child("GamePolygonID").getValue();
                final String gameStatus = (String) snapshot.child("GameStatus").getValue();
                final String gameDescription = (String) snapshot.child("GameDescription").getValue();


                if (gameKey!=null & gameName!=null & gameDate!=null & gamePolygonID!=null & gameStatus!=null & gameDescription!=null){
                    if (gameStatus.equals("открыт набор на игру")){
                        callback.onOpeningGamesOfTeamChanged();
                    }
                    if (gameStatus.equals("набор на игру закрыт")){
                        callback.onClosedGamesOfTeamChanged();
                    }
//                    if (gameStatus.equals("игра отменена")){
//                        callback.onCancelledGamesOfOrgcomChanged();
//                    }
                    if (gameStatus.equals("игра идет")) {
                        callback.onRunningGamesOfTeamChanged();
                    }
                    if (gameStatus.equals("игра прошла")) {
                        callback.onHappensGamesOfTeamChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback.onOpeningGamesOfTeamChanged();
                callback.onClosedGamesOfTeamChanged();
//                callback.onCancelledGamesOfOrgcomChanged();
                callback.onRunningGamesOfTeamChanged();
                callback.onHappensGamesOfTeamChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    public interface gameInfoCallback{
        void onGameInfoChanged(String orgcomID, String gameName, String gameDate,
                                  String polygonID, String gameStatus, String gameDescription, String gameWinner, String gameSides);
    }
    public void getGameInfo(final gameInfoCallback callback, final String gameKey){
        final DatabaseReference databaseReference = database.getReference("Games");
        databaseReference.child(gameKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String gameOrgcomID= (String) snapshot.child("GameOrgcomID").getValue();
                final String gameName= (String) snapshot.child("GameName").getValue();
                final String gameDate = (String) snapshot.child("GameDate").getValue();
                final String gamePolygonID = (String) snapshot.child("GamePolygonID").getValue();
                final String gameStatus = (String) snapshot.child("GameStatus").getValue();
                final String gameDescription = (String) snapshot.child("GameDescription").getValue();
                final String gameSides = (String) snapshot.child("GameSides").getValue();
                if (gameStatus.equals("игра прошла")) {
                    final String gameWinner = (String) snapshot.child("GameWinner").getValue();
                    callback.onGameInfoChanged(gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription,gameWinner,gameSides);
                }else{
                    callback.onGameInfoChanged(gameOrgcomID,gameName,gameDate,gamePolygonID,gameStatus,gameDescription,null,gameSides);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //----------создание в БД новой записи заявки на участие команды в игре-----------------------------
    public void requestToGame(String orgcomID, String gameID, String teamID, String reqGameDescr, String playersCount, String reqGameStatus,  boolean payment){
        //--------generate random key-------------------------------------------------------------------------
        String newKey = database.getReference("quiz").push().getKey();

        DatabaseReference db_orgcomID;
        DatabaseReference db_gameID;
        DatabaseReference db_teamID;
        DatabaseReference db_reqGameDescr;
        DatabaseReference db_playersCount;
        DatabaseReference db_reqGameStatus;
        DatabaseReference db_payment;

        db_orgcomID = database.getReference("RequestsToGame/"+newKey+"/OrgcomID");
        db_gameID = database.getReference("RequestsToGame/"+newKey+"/GameID");
        db_teamID = database.getReference("RequestsToGame/"+newKey+"/TeamID");
        db_reqGameDescr = database.getReference("RequestsToGame/"+newKey+"/RequestGameDescription");
        db_playersCount = database.getReference("RequestsToGame/"+newKey+"/PlayersCount");
        db_reqGameStatus = database.getReference("RequestsToGame/"+newKey+"/RequestGameStatus");
        db_payment = database.getReference("RequestsToGame/"+newKey+"/Payment");

        db_orgcomID.setValue(orgcomID);
        db_gameID.setValue(gameID);
        db_teamID.setValue(teamID);
        db_reqGameDescr.setValue(reqGameDescr);
        db_playersCount.setValue(playersCount);
        db_reqGameStatus.setValue(reqGameStatus);
        db_payment.setValue(payment);
    }
    //-------------------получаем информацию о заявке---------------------
    public interface requestToGameInfoCallback{
        void onRequestToGameInfoChanged(String gameID, String orgcomID ,boolean payment, String playersCount, String description, String status, String teamID);
    }
    public void getRequestToGameInfo(final requestToGameInfoCallback callback, final String requestKey){
        final DatabaseReference databaseReference = database.getReference("RequestsToGame");
        databaseReference.child(requestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String orgcomID= (String) snapshot.child("OrgcomID").getValue();
                final String gameID= (String) snapshot.child("GameID").getValue();
                final boolean payment= (boolean) snapshot.child("Payment").getValue();
                final String playersCount= (String) snapshot.child("PlayersCount").getValue();
                final String description= (String) snapshot.child("RequestGameDescription").getValue();
                final String status= (String) snapshot.child("RequestGameStatus").getValue();
                final String teamID= (String) snapshot.child("TeamID").getValue();
                callback.onRequestToGameInfoChanged(gameID, orgcomID, payment, playersCount, description, status, teamID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //--------------получаем список заявок участия в игре-------------------------------
//------------два callback'a - один для принятых, другой только для тех, которые рассматриваются и отклонены----------------
//-------------проверяем изменения в списке заявок в команду, к которой прикреплен текущий пользователь--------------------
    public interface requestsToGameListIfChangedCallback{
        void onRequestsToGameListChanged();
        void onApproveRequestsToGameListChanged(String requestKey, String teamName, boolean payment, String status);
        void onNewRequestsToMyTeamListChanged(String requestKey, String teamName, boolean payment, String status);
    }
    public void getRequestsToGameListIfChanged(final requestsToGameListIfChangedCallback callback, final String gameID) {
        final DatabaseReference databaseRef = database.getReference("RequestsToGame");
        final Query databaseQuery = databaseRef.orderByChild("GameID").equalTo(gameID);
        databaseQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot snapshot, @Nullable String previousChildName) {
                final String requestKey = snapshot.getKey();
                String teamID = (String) snapshot.child("TeamID").getValue();
                final String status = (String) snapshot.child("RequestGameStatus").getValue();
                if (status!=null & teamID!=null) {
                    getTeamInfo(new teamInfoCallback() {
                        @Override
                        public void onTeamInfoChanged(final String teamName, String teamCity, String teamYear, String teamDescription) {
                            getGameInfo(new gameInfoCallback() {
                                @Override
                                public void onGameInfoChanged(String orgcomID, String gameName, String gameDate, String polygonID, String gameStatus, String gameDescription, String gameWinner, String gameSides) {
                                    boolean payment = (boolean) snapshot.child("Payment").getValue();
                                    if (status.equals("рассматривается") | status.equals("отклонена")) {
                                        callback.onNewRequestsToMyTeamListChanged(requestKey, teamName, payment, status);
                                    }
                                    if (status.equals("одобрена")) {
                                        callback.onApproveRequestsToGameListChanged(requestKey, teamName, payment, status);
                                    }
                                }
                            },gameID);
                        }
                    }, teamID);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("Status").getValue()!=null){
                    callback.onRequestsToGameListChanged();}
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback.onRequestsToGameListChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //------------проверяем смену статуса заявки на игру, обрабатываем одобрение и отклонение заявки------------------------------
    public interface changeRequestToGameStatusCallback{
        void onChangeRequestToGameStatus(boolean f);
    }
    public void approveRequestToGame(final changeRequestToGameStatusCallback callback, final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToGame");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToGame/" + requestKey + "/RequestGameStatus");
                    db_status.setValue("одобрена");

                    callback.onChangeRequestToGameStatus(true);

                }else {
                    callback.onChangeRequestToGameStatus(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    public void dismissRequestToGame(final changeRequestToGameStatusCallback callback,final String requestKey){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToGame");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToGame/" + requestKey + "/RequestGameStatus");
                    db_status.setValue("отклонена");
                    callback.onChangeRequestToGameStatus(true);
                }else{
                    callback.onChangeRequestToGameStatus(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void paymentRequestToGame(final changeRequestToGameStatusCallback callback, final String requestKey, final boolean payment){
        //----проверяем, актуальна ли все еще эта заявка (не отменена/игрок не вступил в команду)----------
        final DatabaseReference databaseReference = database.getReference("RequestsToGame");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(requestKey).getValue() != null) {
                    DatabaseReference db_status;
                    db_status = database.getReference("RequestsToGame/" + requestKey + "/Payment");
                    db_status.setValue(payment);
                    callback.onChangeRequestToGameStatus(true);
                }else{
                    callback.onChangeRequestToGameStatus(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void takePartInTheGame(String gameKey, String teamID, String userUID, boolean f){
        DatabaseReference db_userUID = database.getReference("TakePartInTheGame/"+gameKey+"/"+userUID);

        db_userUID.setValue(f);
    }
    public void changeGameStatus(String gameKey, String gameStatus){
        DatabaseReference db_gameStatus = database.getReference("Games/"+gameKey+"/GameStatus");
        db_gameStatus.setValue(gameStatus);
    }
    public void changeGameWinner(String gameKey, String gameWinner){
        DatabaseReference db_gameStatus = database.getReference("Games/"+gameKey+"/GameWinner");
        db_gameStatus.setValue(gameWinner);
    }


}


//public interface myRequestsListCallback{
//    void onMyRequestsListChanged(String requestKey, String userUID,String teamName ,String status);
//}
//
//    public void getMyRequest(final myRequestsListCallback callback){
//        DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
//        final Query databaseQuery = databaseRef.orderByChild("UserUID").equalTo(getUserUID());
//        databaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot == null) return;
//                else{
//                    for (DataSnapshot postSnapShot: snapshot.getChildren()) {
//                        final String requestKey = postSnapShot.getKey().toString();
//                        String teamKey = (String) postSnapShot.child("TeamKey").getValue();
//                        final String status = (String) postSnapShot.child("Status").getValue();
//
//                        getTeamInfo(new teamInfoCallback() {
//                            @Override
//                            public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
//                                callback.onMyRequestsListChanged(requestKey, getUserUID(), teamName, status);
//                            }
//                        }, teamKey);
//
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

//    public interface requestsToMyTeamListCallback{
//        void onAllRequestsToMyTeamListChanged(String requestKey,String playerUID,String teamName ,String status);
//        void onFilteredRequestsToMyTeamListChanged(String requestKey,String playerUID,String teamName ,String status);
//    }
//
//    public void getRequestRequestsToMyTeam(final requestsToMyTeamListCallback callback) {
//        final DatabaseReference databaseRef = database.getReference("RequestsToConnectTeam");
//        getTeamKey(new teamCallback() {
//            @Override
//            public void onTeamIdChanged(String teamKey) {
//                final Query databaseQuery = databaseRef.orderByChild("TeamKey").equalTo(teamKey);
//                databaseQuery.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot == null) return;
//                        else {
//                            for (DataSnapshot postSnapShot : snapshot.getChildren()) {
//                                final String requestKey = postSnapShot.getKey();
//                                String teamKey = (String) postSnapShot.child("TeamKey").getValue();
//                                final String playerUID = (String) postSnapShot.child("UserUID").getValue();
//                                final String status = (String) postSnapShot.child("Status").getValue();
//                                if (status!=null) {
//
//                                    getTeamInfo(new teamInfoCallback() {
//                                        @Override
//                                        public void onTeamInfoChanged(String teamName, String teamCity, String teamYear) {
//                                            callback.onAllRequestsToMyTeamListChanged(requestKey, playerUID, teamName, status);
//                                            if (status.equals("рассматривается")) {
//                                                callback.onFilteredRequestsToMyTeamListChanged(requestKey, playerUID, teamName, status);
//                                            }
//                                        }
//                                    }, teamKey);
//                                }
//
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//            @Override
//            public void onTeamNameChanged(String teamName) {
//            }
//        });
//    }
