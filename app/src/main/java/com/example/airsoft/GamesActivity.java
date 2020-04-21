package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button buttonBackGames = findViewById(R.id.button_back_games);
        Button buttonNewGame = findViewById(R.id.add_game);
        buttonBackGames.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }

        );
        buttonNewGame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".NewGameActivity");
                        startActivity(i);
                    }
                }

        );
    }




}
