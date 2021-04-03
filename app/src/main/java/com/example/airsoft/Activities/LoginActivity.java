package com.example.airsoft.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airsoft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String team_key;


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
        final Button logIn = findViewById(R.id.logIn);
        Button registration = findViewById(R.id.registration);

//------------------войти - попадаем на главную страницу------------------------------------------------------------
        logIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String password = ((EditText)findViewById(R.id.password)).getText().toString();

                        if (email.length() < 5) {
                            Toast.makeText(LoginActivity.this, "Введите почту",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else if (password.length() < 6) {
                            Toast.makeText(LoginActivity.this, "Введите пароль",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });


//----------------зарегестрироваться - попадаем на внесение данных об игроке-------------------------------------------------
        registration.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String password = ((EditText)findViewById(R.id.password)).getText().toString();

                        if (email.length() < 5) {
                            Toast.makeText(LoginActivity.this, "Для регистрации введите почту и пароль",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else if (password.length() < 6) {
                            Toast.makeText(LoginActivity.this, "Пароль должен содержать не менее 6 символов",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
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
                    }
                });
    }

    private void updateUILogIn(FirebaseUser user) {
        if (user != null) {
            get_team_key();
//            Log.d("state", "updateUILogIn    " + FirebaseAuth.getInstance().getUid());
            Intent i = new Intent(".MainActivity");
//            i.putExtra("team_key", team_key);
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
//            get_team_key();
            Intent i = new Intent(".RegistrationPersonInfo");
//            i.putExtra("team_key", team_key);
            startActivity(i);

            // User is signed in
        } else {
            // No user is signed in
            return;
        }
    }

    //---------получаем по uid ключ команды, к которой присоеденен юзер, записываем в team_key---------------
    private void get_team_key(){

        String userID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PersonInfo");
        databaseRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot == null) return;
                 else{
                     team_key = snapshot.child("TeamKey").getValue().toString();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         }
        );

    }


}

