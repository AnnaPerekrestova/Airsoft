package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.airsoft.R;

public class GamesViewSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_view_select);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        Button prev_games = findViewById(R.id.prev_games);
        Button plan_games = findViewById(R.id.planing_games);
        Button new_games = findViewById(R.id.game_search);

        prev_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".PreviousGamesActivity");
                        startActivity(i);
                    }
                }

        );
        plan_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".PlannedGamesActivity");
                        startActivity(i);
                    }
                }

        );
        new_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".SearchNewGameActivity");
                        startActivity(i);
                    }
                }
        );
    }
}
