//package com.example.airsoft.Activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.example.airsoft.Adapters.GamesAdapter;
//import com.example.airsoft.Classes.GameClass;
//import com.example.airsoft.R;
//import com.example.airsoft.RecyclerTouchListener;
//import com.example.airsoft.RecyclerViewDecorator;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GamesRecyclerActivity extends AppCompatActivity {
//    private List<GameClass> gameList = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private GamesAdapter gAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_games_recycler);
//
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_games);
////------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
//        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));
//
//
////--------Создаем адаптер для RecyclerView------------------------------------------------------------------------
//        gAdapter = new GamesAdapter(gameList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(gAdapter);
//
//        add_to_games_view();
//        //-------обрабатываем нажатие на строку-----------------------------------------------------------------------------------
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                GameClass selected_game = gameList.get(position);
//                Intent i = new Intent(".GameInfoActivity");
//                i.putExtra("game_id", selected_game.getGame_id());
//                i.putExtra("member_team_id", selected_game.getMemberTeamID());
//                i.putExtra("date_time", selected_game.getDate_time());
//                i.putExtra("map", selected_game.getMap());
//                i.putExtra("winner", selected_game.getWinner());
//
//                i.putExtra("used_teams", selected_game.getUsedTeams());
//                startActivity(i);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
//    }
////------Получаем данные для добавления в список-------------------------------------------------------------------------
//    public void add_to_games_view(){
//        final List<String> gameIdList = new ArrayList<>();
//        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Games");
//        databaseRef.child("game_id").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot==null)return;
//                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
//                    gameIdList.add(postSnapShot.getKey());
//                }
//                SetData(databaseRef, gameIdList);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Error
//                Log.d("Error", "databaseError");
//            }
//        });
//    }
//
//    private void SetData(DatabaseReference databaseRef, List<String> userIdList) {
//        for (final String id: userIdList){
//            databaseRef.child("game_id").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot2) {
//                    if(dataSnapshot2==null)return;
//                    String date_time =  (String)dataSnapshot2.child("DateTime").getValue();
//                    String winner = (String)dataSnapshot2.child("WinnerTeam").getValue();
//                    String map = (String)dataSnapshot2.child("Map").getValue();
//                    String usedTeams = (String)dataSnapshot2.child("UsedTeams").getValue();
//                    String memberTeamID = (String)dataSnapshot2.child("MemberTeamID").getValue();
//
//                    addRow(id, date_time,map,winner,usedTeams,memberTeamID);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Error
//                    Log.d("Error", "databaseError");
//                }
//            });
//        }
//    }
////---------------Добавляем строку ресайклер вью и обновляем адаптер ------------------------------------------------------------------
//    private void addRow(String id_from_base, String date_time_from_base, String map_from_base, String winner_from_base, String used_teams_from_base, String m_t_id_from_base ) {
//        GameClass game = new GameClass(id_from_base, date_time_from_base, map_from_base, winner_from_base,used_teams_from_base,m_t_id_from_base);
//        gameList.add(game);
//
//        gAdapter.notifyDataSetChanged();
//    }
////---------Функция при нажатии на кнопку Сохранить -------------------------------------------------------------------------------------
//    public void addNewGame(View view) {
//        Intent i = new Intent(".NewGameRecyclerActivity");
//        startActivity(i);
//        finish();
//    }
//}
