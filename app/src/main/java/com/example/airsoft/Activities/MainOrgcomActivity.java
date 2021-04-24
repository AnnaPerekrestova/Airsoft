package com.example.airsoft.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.airsoft.StartActivity;
import com.example.data.FirebaseData;

public class MainOrgcomActivity extends AppCompatActivity {

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
            }
        });
    }
    private void addListenerOnButton() {
        ImageButton buttonDelog = findViewById(R.id.delog);
        Button buttonPolygons = findViewById(R.id.orgcom_polygon);

        buttonDelog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainOrgcomActivity.this);
                        builder.setTitle("Выход из системы");
                        builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка Удалить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //---выход из аккаунта------------------------------
                                fbData.deLogin();
                                finish();
                                // Отпускает диалоговое окно
                            }

                        });
                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Оставить
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Отпускает диалоговое окно
                            }

                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );
        buttonPolygons.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(".Polygons");
                        startActivity(i);
                    }
                }
        );
    }
}

