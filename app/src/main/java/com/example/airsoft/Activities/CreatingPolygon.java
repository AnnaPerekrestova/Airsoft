package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.airsoft.R;
import com.example.data.FirebaseData;

public class CreatingPolygon extends AppCompatActivity {

    FirebaseData fbData = new FirebaseData().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_polygon);

        addListenerOnButton();
    }

    public void addListenerOnButton(){
        findViewById(R.id.creating_polygon_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.creatingNewPolygon(((EditText)findViewById(R.id.creating_polygon_name)).getText().toString(), ((EditText)findViewById(R.id.creating_polygon_adress)).getText().toString(), ((EditText)findViewById(R.id.creating_polygon_description)).getText().toString());
                finish();
            }
        });
    }
}

