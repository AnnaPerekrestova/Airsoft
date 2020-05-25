package com.example.airsoft.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.airsoft.Adapters.MembersAdapter;
import com.example.airsoft.Classes.MembersClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MembersRecyclerActivity extends AppCompatActivity {
    private List<MembersClass> membersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                MembersClass selected_member = membersList.get(position);
//                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();

                String nick = (String) selected_member.getNickname();
//                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(".MemberInfo");
                intent.putExtra("id_m", nick);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                MembersClass selected_member = membersList.get(position);
                final String id_member_to_del = (String) selected_member.getNickname();
                AlertDialog.Builder builder = new AlertDialog.Builder(MembersRecyclerActivity.this);
                builder.setTitle("Удаление игрока");
                builder.setMessage("Вы действительно хотите удалить выбранного игрока?");
                builder.setCancelable(false);
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { // Кнопка Удалить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), id_member_to_del + " Удален из команды", Toast.LENGTH_SHORT).show();
                        DatabaseReference db_actually;
                        db_actually = database.getReference("Members/members_nicknames/" + id_member_to_del + "/Actually");
                        db_actually.setValue(0);
                        dialog.dismiss();
                        Intent i = new Intent(".MembersRecyclerActivity");
                        startActivity(i);
                        finish();
                        // Отпускает диалоговое окно
                    }

                });
                builder.setNegativeButton("Оставить", new DialogInterface.OnClickListener() { // Кнопка Оставить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }));
        add_to_members_table();
        //addListenerOnButton();
    }
    public void add_to_members_table(){
        final List<String> membersNicksList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_nicknames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    if (postSnapShot.child("Actually").getValue().toString().equals("1")) {
                        membersNicksList.add(postSnapShot.getKey());
                    }
                }
                SetData(databaseRef, membersNicksList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userNicksList) {
        for (final String nick: userNicksList){
            databaseRef.child("members_nicknames").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {

                    if(dataSnapshot2==null)return;
                    String fio =  (String)dataSnapshot2.child("FIO").getValue();
                    addRow(nick,fio);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }
    private void addRow(String nick_from_base, String fio_from_base ) {
        MembersClass member = new MembersClass( nick_from_base );
        member.setFio(fio_from_base);
        membersList.add(member);

        mAdapter.notifyDataSetChanged();
    }
    public void addNewMember(View view) {
        Intent i = new Intent(".NewMemberActivity");
        startActivity(i);
        finish();
    }


}
