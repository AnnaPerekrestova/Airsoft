package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        final List<String> userIdList = new ArrayList();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Team");
        databaseRef.child("members_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    userIdList.add(postSnapShot.getKey());
                    Log.d("test", postSnapShot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
            }
        });
    }



}
