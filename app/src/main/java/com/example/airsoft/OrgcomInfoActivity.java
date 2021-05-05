package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.data.FirebaseData;

public class OrgcomInfoActivity extends AppCompatActivity {

    String thisOrgcomKey;
    FirebaseData fbData = new FirebaseData().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgcom_info);

    //-----Получаем значения переданные через intent------------------------------------------------------------
        Intent intent = getIntent();
        if (intent.getStringExtra("orgcomKey")==null){
            fbData.getOrgcomKey(new FirebaseData.orgcomCallback() {
                @Override
                public void onOrgcomIdChanged(String orgcomKey) {
                    thisOrgcomKey=orgcomKey;
                    findViewById(R.id.orgcom_info_description).setEnabled(true);
                    findViewById(R.id.button_save_changes).setVisibility(View.VISIBLE);
                }

                @Override
                public void onOrgcomNameChanged(String orgcomName) {

                }
            });
        }
        else {
            thisOrgcomKey = intent.getStringExtra("orgcomKey");

        }
        getData(thisOrgcomKey);
        addListenerOnButton(thisOrgcomKey);
    }

    public void getData(String orgcomKey){
        fbData.getOrgcomInfo(new FirebaseData.orgcomInfoCallback() {
            @Override
            public void onOrgcomInfoChanged(String orgcomName, String orgcomCity, String orgcomDescription) {
                ((TextView) findViewById(R.id.orgcom_name)).setText(orgcomName);
                ((TextView) findViewById(R.id.orgcom_info_city)).setText(orgcomCity);
                ((EditText) findViewById(R.id.orgcom_info_description)).setText(orgcomDescription);
            }
        },orgcomKey);
    }
    public void addListenerOnButton(final String orgcomKey){
        Button members = findViewById(R.id.orgcom_members_button);
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(".MembersRecyclerActivity");
//                i.putExtra("orgcomKey", orgcomKey);
//                startActivity(i);
            }
        });

    }
}