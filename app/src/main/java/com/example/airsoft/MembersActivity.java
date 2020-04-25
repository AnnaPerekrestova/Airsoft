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

public class MembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        addListenerOnButton();




    }
    public void all_members(){
        setContentView(R.layout.activity_new_game);
        final List<String> userIdList = new ArrayList<>();
        final List<List<String>> fio_pos=new ArrayList<>();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Team");
        databaseRef.child("members_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    userIdList.add(postSnapShot.getKey());
                    Log.d("test", postSnapShot.getKey());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
            }
        });
        for (String i: userIdList){
            int id = Integer.parseInt (i);
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

        }
        for (List<String> i: fio_pos){
            addRow(Integer.parseInt(i.get(0)),Integer.parseInt(i.get(1)));
        }
    }


    public void addRow(int fio, int position) {
        //Сначала найдем в разметке активити саму таблицу по идентификатору
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableMembers);
        //Создаём экземпляр инфлейтера, который понадобится для создания строки таблицы из шаблона. В качестве контекста у нас используется сама активити
        LayoutInflater inflater = LayoutInflater.from(this);
        //Создаем строку таблицы, используя шаблон из файла /res/layout/table_row.xml
        TableRow tr = (TableRow) inflater.inflate(R.layout.members_row, null);
        //Находим ячейку для номера дня по идентификатору
        TextView tv = (TextView) tr.findViewById(R.id.textView_fio_string);
        //Обязательно приводим число к строке, иначе оно будет воспринято как идентификатор ресурса
        tv.setText(fio);
        //Ищем следующую ячейку и устанавливаем её значение
        tv = (TextView) tr.findViewById(R.id.textView_position_string);
        tv.setText(position);

        tableLayout.addView(tr); //добавляем созданную строку в таблицу
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
