package com.example.airsoft;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberTeamAdapter extends RecyclerView.Adapter<MemberTeamAdapter.MemberTeamHolder> {
    private List<MemberTeamClass> member_team_List;
    ArrayAdapter<String> adapterTeams;

    //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
    public class MemberTeamHolder extends RecyclerView.ViewHolder { //получает макет строки
        public TextView member;
        public Spinner team;
        public MemberTeamHolder(View view) {
            super(view);
            member = (TextView) view.findViewById(R.id.recycler_member_team_row);
            team = (Spinner) view.findViewById(R.id.member_team);
        }
    }

    public MemberTeamAdapter(List<MemberTeamClass> member_team_List) { //адаптер получает значения
        this.member_team_List = member_team_List;
    }

    @Override
    public MemberTeamAdapter.MemberTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {// создает новвый объект
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_team_list_row, parent, false);

        return new MemberTeamAdapter.MemberTeamHolder(itemView); //новый объект будет использоваться для отображения элементов при помощи адапрера
    }

    @Override
    public void onBindViewHolder(final MemberTeamAdapter.MemberTeamHolder holder, int position) {
        final MemberTeamClass member_team = member_team_List.get(position);
        holder.member.setText(member_team.getMember());

//------Создаем адаптер для заполнения спиннера (контекст, в котором выводится спиннер - получаем родительский элемент холдера;
        //разметка по умолчанию для выпадающего списка; список, поторым будет  заполняться спиннер)
        adapterTeams = new ArrayAdapter<String>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, this.TeamsList());
        // Определяем разметку для использования при выборе элемента
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        holder.team.setAdapter(adapterTeams);

//------Добавляем слушатель выбора элемента в спиннере -----------------------------------------------------------------
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                member_team.setTeam(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        holder.team.setOnItemSelectedListener(itemSelectedListener);
    }
 //   ----- Заполняем список для спиннера (Данные из БД + "Не учавстоввал") ----------------------------------------------

    public List<String> TeamsList(){
        final List<String> teamsList= new ArrayList<String>();

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Teams");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    teamsList.add(postSnapShot.getValue().toString());
                }
                adapterTeams.notifyDataSetChanged();//обновление адаптера
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
        teamsList.add("Не учавствовал");
        return teamsList;
    }

    @Override
    public int getItemCount() {
        return member_team_List.size();
    }
}

