package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class MainOrgcomActivity extends AppCompatActivity {

    String personUID;
    public FirebaseData fbData = FirebaseData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_orgcom);

        addListenerOnButton();
        getData();
        }
    private void getData(){
        fbData.getOrgcomName(new FirebaseData.orgcomCallback() {
            @Override
            public void onOrgcomIdChanged(String orgcomKey) {}

            @Override
            public void onOrgcomNameChanged(String orgcomName) {
                ((TextView) findViewById(R.id.text_orgcom_name)).setText(orgcomName);
                ((TextView) findViewById(R.id.text_orgcom_name)).setVisibility(View.VISIBLE);
            }
        });
    }
    private void addListenerOnButton() {
        Button buttonPolygons = findViewById(R.id.orgcom_polygon);
        Button orgInfo = findViewById(R.id.org_info);
        Button ogrcomInfo = findViewById(R.id.orgcom_info);
        Button orgcomGames = findViewById(R.id.orgcom_games);

        buttonPolygons.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(".Polygons");
                    startActivity(i);
                }
            }
        );
        orgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".OrgInfoActivity");
                startActivity(i);
            }
        });
        ogrcomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".OrgcomInfoActivity");
                startActivity(i);
            }
        });
        orgcomGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".GamesViewSelectOrgcom");
                startActivity(i);
            }
        });
    }
}

