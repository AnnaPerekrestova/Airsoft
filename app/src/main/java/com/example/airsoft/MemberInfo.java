package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberInfo extends AppCompatActivity {

    TextView txt_id_member;
    TextView txt_fio;
    TextView txt_arsenal;
    TextView txt_position;
    TextView txt_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);



        Intent intent = getIntent();
        String Member_id = intent.getStringExtra("id_m");

        txt_id_member = (TextView) findViewById(R.id.textView_id_member);
        txt_id_member.setText(Member_id);

        Get_member_info(Member_id);





    }

    private void Get_member_info(String memb_id) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_id").child(memb_id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshotInfo) {

                if (dataSnapshotInfo == null) return;
                String fio = (String) dataSnapshotInfo.child("FIO").getValue();
                String nick = (String) dataSnapshotInfo.child("Nickname").getValue();
                String arsenal = (String) dataSnapshotInfo.child("Arsenal").getValue();
                String position = (String) dataSnapshotInfo.child("Position").getValue();

                Fill_member_info(arsenal, fio, nick, position);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void Fill_member_info(String ars, String fio, String nick, String pos) {
        txt_fio = (TextView) findViewById(R.id.member_fio);
        txt_arsenal = (TextView) findViewById(R.id.member_arsenal);
        txt_position = (TextView) findViewById(R.id.member_position);
        txt_nickname = (TextView) findViewById(R.id.member_nickname);

        txt_arsenal.setText(ars);
        txt_fio.setText(fio);
        txt_nickname.setText(nick);
        txt_position.setText(pos);

    }


}
