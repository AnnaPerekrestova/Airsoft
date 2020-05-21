package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MemberInfo extends AppCompatActivity {

    TextView txt_id_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        txt_id_member = (TextView) findViewById(R.id.textView_id_member);

        Intent intent = getIntent();
        String Member_id = intent.getStringExtra("id_m");

        txt_id_member.setText(Member_id);

    }
}
