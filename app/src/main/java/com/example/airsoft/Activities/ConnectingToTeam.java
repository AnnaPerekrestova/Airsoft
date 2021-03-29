package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.airsoft.R;

public class ConnectingToTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_team);
        addListenerOnButton();

    }
    public void addListenerOnButton() {
        Button connect_to_team = findViewById(R.id.connect_to_team);
        Button create_team = findViewById(R.id.create_team);
        Button solo = findViewById(R.id.solo);
        connect_to_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(".MainActivity");
                        startActivity(i);
                        finish();
                    }
                }

        );
        create_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".CreatingTeam");
                        startActivity(i);
                    }
                }

        );
        solo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MainActivity");
                        startActivity(i);
                    }
                }

        );
    }
}
