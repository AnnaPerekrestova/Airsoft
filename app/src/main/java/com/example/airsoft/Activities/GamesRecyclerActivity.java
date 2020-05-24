package com.example.airsoft.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.GamesAdapter;
import com.example.airsoft.Classes.GamesClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GamesRecyclerActivity extends AppCompatActivity {
    private List<GamesClass> gameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GamesAdapter gAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_games);

        gAdapter = new GamesAdapter(gameList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gAdapter);

        add_to_games_table();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                GamesClass selected_game = gameList.get(position);
                Toast.makeText(getApplicationContext(), selected_game.getGame_id() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(".GameInfoActivity");
                startActivity(i);
//                String id_member = (String) selected_member.getMember_id();
//                Intent intent = new Intent(".MemberInfo");
//                intent.putExtra("id_m", id_member);
//                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //addListenerOnButton();
    }
    public void add_to_games_table(){
        final List<String> gameIdList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Games");
        databaseRef.child("game_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    gameIdList.add(postSnapShot.getKey());
                }
                SetData(databaseRef, gameIdList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userIdList) {
        for (final String id: userIdList){
            databaseRef.child("game_id").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    if(dataSnapshot2==null)return;
                    String date_time =  (String)dataSnapshot2.child("DateTime").getValue();
                    String winner = (String)dataSnapshot2.child("WinnerTeam").getValue();
                    String map = (String)dataSnapshot2.child("Map").getValue();

                    addRow(id, date_time,map,winner);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }

    private void addRow(String id_from_base, String date_time_from_base, String map_from_base, String winner_from_base ) {
        GamesClass game = new GamesClass(id_from_base, date_time_from_base, map_from_base, winner_from_base);
        gameList.add(game);

        gAdapter.notifyDataSetChanged();
    }
//    public void addListenerOnButton() {
//        Button buttonAddPerson = findViewById(R.id.members_fab);
//        buttonAddPerson.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(".PersonActivity");
//                        startActivity(i);
//                        finish();
//                    }
//                }
//        );
//    }




    public void addNewGame(View view) {
        Intent i = new Intent(".NewGameRecyclerActivity");
        startActivity(i);
        finish();
    }
}
