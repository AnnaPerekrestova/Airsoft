package com.example.airsoft;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.airsoft.Activities.MainOrgcomActivity;

public class Polygons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygons);

        addListenerOnButton();


    }


    private void addListenerOnButton() {
        Button buttonNewPolygon = findViewById(R.id.new_polygon_button);

        buttonNewPolygon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(".CreatePolygon");
                        startActivity(i);
                    }
                }
        );
    }
}
