package com.example.airsoft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberTeamAdapter extends RecyclerView.Adapter<MemberTeamAdapter.MemberTeamHolder> {

    private List<MemberTeamClass> member_team_List;



    //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
    public class MemberTeamHolder extends RecyclerView.ViewHolder {
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
    public MemberTeamAdapter.MemberTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_team_list_row, parent, false);

        return new MemberTeamAdapter.MemberTeamHolder(itemView); //получает макет строки
    }

    @Override
    public void onBindViewHolder(MemberTeamAdapter.MemberTeamHolder holder, int position) { //заполняет макеты данными
        MemberTeamClass member_team = member_team_List.get(position);
        holder.member.setText(member_team.getMember());

        String[] teams_list= {"map1", "map2", "Вологда", "Волгоград", "Саратов", "Воронеж"};

        ArrayAdapter<String> adapterTeams = new ArrayAdapter<String>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, teams_list);
        // Определяем разметку для использования при выборе элемента
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        holder.team.setAdapter(adapterTeams);

    }

    @Override
    public int getItemCount() {
        return member_team_List.size();
    }
}

