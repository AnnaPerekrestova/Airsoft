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
        Button act_games = findViewById(R.id.actual_games);

        prev_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".GamesListActivity");
                        i.putExtra("listType","playerPrev");
                        startActivity(i);
                    }
                }

        );
        plan_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".GamesListActivity");
                        i.putExtra("listType","playerPlaning");
                        startActivity(i);
                    }
                }

        );
        new_games.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".GamesListActivity");
                        i.putExtra("listType","playerSearch");
                        startActivity(i);
                    }
                }
        );
        act_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".GamesListActivity");
                i.putExtra("listType","playerRunning");
                startActivity(i);
            }
        });
    }
}
