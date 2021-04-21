package com.example.airsoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.data.FirebaseData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("state", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("state", "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUILogIn(user);
                // [END_EXCLUDE]
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d("state", "onStart    " + FirebaseAuth.getInstance().getUid());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUILogIn(currentUser);
    }
    private void updateUILogIn(FirebaseUser user) {
        if (user != null) {
            final FirebaseData fbData = new FirebaseData().getInstance();
            fbData.getOrgFlag(new FirebaseData.orgFlagCallback() {
                @Override
                public void onOrgFlagChanged(boolean orgFlag) {
                    if (orgFlag){
                        fbData.getOrgcomKey(new FirebaseData.orgcomCallback() {
                            @Override
                            public void onOrgcomIdChanged(String orgcomKey) {
                                if (orgcomKey.equals("no info")){
                                    Log.d("no info", "no info   " + FirebaseAuth.getInstance().getUid());

                                    Intent i = new Intent(".ConnectingToOrgcom");
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Intent i = new Intent(".MainOrgcomActivity");
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onOrgcomNameChanged(String orgcomName) {

                            }
                        });


                    }
                    if (!orgFlag){
                        fbData.getTeamKey(new FirebaseData.teamCallback() {
                            @Override
                            public void onTeamIdChanged(String teamKey) {
                                if (teamKey.equals("no info")){
                                    Log.d("no info", "no info   " + FirebaseAuth.getInstance().getUid());

                                    Intent i = new Intent(".ConnectingToTeam");
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Intent i = new Intent(".MainActivity");
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onTeamNameChanged(String teamName) {

                            }
                        });
                    }
                }

                @Override
                public void onOrgFlagNull(String no_info) {
                    if (no_info.equals("no info")){
                        Log.d("no info", "no info   " + FirebaseAuth.getInstance().getUid());

                        Intent i = new Intent(".RegistrationPersonInfo");
                        startActivity(i);
                        finish();
                    }
                }
            });
            // User is signed in
        } else {
            Intent i = new Intent(".LoginActivity");
            startActivity(i);
            finish();
        }
    }
}
