package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.data.FirebaseData;

public class CreatingRequestToGame extends AppCompatActivity {

    FirebaseData fbData = FirebaseData.getInstance();
    String gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_request_to_game);

        Intent intent = getIntent();
        gameID=intent.getStringExtra("gameID");
    }
}