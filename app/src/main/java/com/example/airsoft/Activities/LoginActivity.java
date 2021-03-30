package com.example.airsoft.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airsoft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
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
        addListenerOnButton();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d("state", "onStart    " + FirebaseAuth.getInstance().getUid());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUILogIn(currentUser);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void addListenerOnButton() {
        Button logIn = findViewById(R.id.logIn);
        Button registration = findViewById(R.id.registration);

//------------------войти - попадаем на главную страницу------------------------------------------------------------
        logIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String password = ((EditText)findViewById(R.id.password)).getText().toString();

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task){
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(LoginActivity.this, "Вы успешно вошли в систему!",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUILogIn(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(LoginActivity.this, "Ошибка аутентификации",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUILogIn(null);
                                        }
                                        // ...
                                    }
                                });
                    }
                });


//----------------зарегестрироваться - попадаем на внесение данных об игроке-------------------------------------------------
        registration.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String password = ((EditText)findViewById(R.id.password)).getText().toString();;

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(LoginActivity.this, "Вы успешно зарегестрированы в системе!",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUIRegistration(user);
                                        } else {
                                            // If sign in fails, display a message to the user.

                                            Toast.makeText(LoginActivity.this, "Ошибка регистрации",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUIRegistration(null);
                                        }

                                    }
                                });
                    }
                });
    }

    private void updateUILogIn(FirebaseUser user) {
        if (user != null) {
            Log.d("state", "updateUILogIn    " + FirebaseAuth.getInstance().getUid());
            Intent i = new Intent(".MainActivity");
            startActivity(i);

            // User is signed in
        } else {
            // No user is signed in
            return;
        }
    }

    private void updateUIRegistration(FirebaseUser user) {
        if (user != null) {
            Log.d("state", "updateUIRegistration   " + FirebaseAuth.getInstance().getUid());
            Intent i = new Intent(".RegistrationPersonInfo");
            startActivity(i);

            // User is signed in
        } else {
            // No user is signed in
            return;
        }
    }

}

