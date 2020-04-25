package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        addListenerOnButton();
        //add_to_members_table();
        addRow();



    }
    public void add_to_members_table(){
        final List<String> userIdList = new ArrayList<>();
        final List<List<String>> fio_pos=new ArrayList<>();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Team");
        databaseRef.child("members_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    userIdList.add(postSnapShot.getKey());
                    Log.d("work_test_a", postSnapShot.getKey());
                    //Log.d("work_test_b",  userIdList.get(0));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
            }
        });
        try {
            TimeUnit.SECONDS.sleep(5);
            Log.d("test_do", "wait_time");
        } catch (InterruptedException e) {
            Log.d("test_do", "test_dodotime");
        }
        //Log.d("any_id", userIdList.get(1));
        Log.d("test_do", "test_dododo");
        //Log.d("work_test_do",  userIdList.get(0));
        for (String i: userIdList){
            int id = Integer.parseInt (i);
            Log.d("work_test_c", "new_messenge");
            databaseRef.child("members_id").child(i).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {
                    if(dataSnapshot2==null)return;
                    String fio =  (String)dataSnapshot2.child("fio").getValue();
                    String pos = (String)dataSnapshot2.child("position").getValue();
                    List<String> newList = Arrays.asList(fio, pos);
                    fio_pos.add(newList);

                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                }

            });
            Log.d("test_do", "test_what");

        }
        for (List<String> i: fio_pos){
            Log.d("test_do", "test_dodono");
            //addRow(i.get(0),i.get(1));
        }
    }

//   String fio, String position
    public void addRow() {
        //Сначала найдем в разметке активити саму таблицу по идентификатору
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableMembers);
        //Создаём экземпляр инфлейтера, который понадобится для создания строки таблицы из шаблона. В качестве контекста у нас используется сама активити
        LayoutInflater inflater = LayoutInflater.from(this);
        //Создаем строку таблицы, используя шаблон из файла /res/layout/table_row.xml
        TableRow tr = (TableRow) inflater.inflate(R.layout.members_row, null);
        //Находим ячейку по идентификатору
        TextView tv = (TextView) tr.findViewById(R.id.textView_fio_string);
        //Обязательно приводим число к строке, иначе оно будет воспринято как идентификатор ресурса
        tv.setText("fio");
        //Ищем следующую ячейку и устанавливаем её значение
        tv = (TextView) tr.findViewById(R.id.textView_position_string);
        tv.setText("position");

        tableLayout.addView(tr); //добавляем созданную строку в таблицу

        Log.d("test_do", "test_dododowhat");

    }

    public void addListenerOnButton() {
        Button buttonAddPerson = findViewById(R.id.add_new_member);
        Button buttonBack = findViewById(R.id.button_back_members);
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
                        finish();
                    }
                }

        );

    }


}
