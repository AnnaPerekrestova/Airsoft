package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GamesViewSelectOrgcom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_view_select_orgcom);

        addListenerOnButton();
    }
    private void addListenerOnButton(){
        Button planing = findViewById(R.id.planing_games_orgcom);
        Button prev = findViewById(R.id.prev_games_orgcom);

        planing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".GamesListActivity");
                i.putExtra("listType","orgcomPlaning");
                startActivity(i);
            }
        });
    }
}