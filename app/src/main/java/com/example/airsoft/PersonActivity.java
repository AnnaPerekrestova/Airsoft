package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        addListenerOnButton();
    }
    @SuppressLint("WrongViewCast")
    public void addListenerOnButton() {
        Button buttonPersonSave = findViewById(R.id.person_save);
        Button buttonPersonCancel = findViewById(R.id.person_cancel);
        //Text personFIO = findViewById(R.id.person_fio)();
        //Text personFIO = findViewById(R.id.person_fio);
        
        
        
        //Button buttonMembers = findViewById(R.id.members);
        buttonPersonSave.setOnClickListener(
                new View.OnClickListener() {

                    


                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MembersActivity");
                        startActivity(i);
                    }
                }

        );
        buttonPersonCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(".MembersActivity");
                        startActivity(i);
                    }
                }

        );

    }

}
