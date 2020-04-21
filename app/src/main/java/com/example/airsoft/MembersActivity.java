package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        Button buttonAddPerson = findViewById(R.id.add_new_member);
        Button buttonBack = findViewById(R.id.button_back);
        buttonAddPerson.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".PersonActivity");
                        startActivity(i);
                    }
                }

        );
        buttonBack.setOnClickListener(
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
