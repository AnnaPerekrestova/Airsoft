package com.example.airsoft.Activities;


import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.MembersAdapter;
import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MembersRecyclerActivity extends AppCompatActivity {
    private List<PlayerClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    FirebaseData fbData = new FirebaseData().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_members_recycler);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_members);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        mAdapter = new MembersAdapter(membersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlayerClass selectedMember = membersList.get(position);
//                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();

                String personUID = (String) selectedMember.getPlayerUID();
//                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(".PlayerInfo");
                intent.putExtra("playerID", personUID);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

//            @Override
//            public void onLongClick(View view, int position) {
//                MembersClass selected_member = membersList.get(position);
//                final String id_member_to_del = (String) selected_member.getNickname();
//                AlertDialog.Builder builder = new AlertDialog.Builder(MembersRecyclerActivity.this);
//                builder.setTitle("Удаление игрока");
//                builder.setMessage("Вы действительно хотите удалить выбранного игрока?");
//                builder.setCancelable(false);
//                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { // Кнопка Удалить
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), id_member_to_del + " Удален из команды", Toast.LENGTH_SHORT).show();
//                        DatabaseReference db_actually;
//                        db_actually = database.getReference("Members/members_nicknames/" + id_member_to_del + "/Actually");
//                        db_actually.setValue(0);
//                        dialog.dismiss();
//                        Intent i = new Intent(".MembersRecyclerActivity");
//                        startActivity(i);
//                        finish();
//                        // Отпускает диалоговое окно
//                    }
//
//                });
//                builder.setNegativeButton("Оставить", new DialogInterface.OnClickListener() { // Кнопка Оставить
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss(); // Отпускает диалоговое окно
//                    }
//
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
        }));
        addToMembersRecycler();
    }
    public void addToMembersRecycler(){
        fbData.getTeamMembersData(new FirebaseData.teamMembersDataCallback(){

            @Override
            public void onTeamMemberDataChanged(String playerUID, String nickname, String fio) {
                addRow(playerUID, nickname, fio);
            }

        });
    }

    private void addRow(String playerUID, String nick, String fio ) {
        PlayerClass player = new PlayerClass( playerUID );
        player.setPlayerUID(playerUID);
        player.setNickname(nick);
        player.setFio(fio);
        membersList.add(player);
        mAdapter.notifyDataSetChanged();
    }
    public void addNewMember(View view) {
        Intent i = new Intent(".NewMemberActivity");
        startActivity(i);
        finish();
    }

    public void requestsToMyTeam(View view){
        Intent i = new Intent(".RequestsToMyTeam");
        startActivity(i);
    }

}
