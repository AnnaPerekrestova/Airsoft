package com.example.airsoft.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.MemberTeamClass;
import com.example.airsoft.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberTeamGameAdapter extends RecyclerView.Adapter<MemberTeamGameAdapter.MemberTeamHolder> {
    //private List<MemberTeamClass> member_team_List;
    ArrayAdapter<String> adapterTeams;

    private List<String[]> used_teams = new ArrayList<>();


    //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
    public class MemberTeamHolder extends RecyclerView.ViewHolder { //получает макет строки
        public ListView listView;
        public MemberTeamHolder(View view) {
            super(view);
            listView = (ListView) view.findViewById(R.id.member_team_game_info);
        }
    }

    public MemberTeamGameAdapter(List<String[]> used_teams) { //адаптер получает значения
        this.used_teams= used_teams;


    }

    @Override
    public MemberTeamGameAdapter.MemberTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {// создает новвый объект
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_team_list_row_game_info, parent, false);
        //f= GetMembersOfUsedTeams(id);
        return new MemberTeamGameAdapter.MemberTeamHolder(itemView); //новый объект будет использоваться для отображения элементов при помощи адапрера
    }

    @Override
    public void onBindViewHolder(final MemberTeamGameAdapter.MemberTeamHolder holder, int position) {
//        GetLists();
        ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
                android.R.layout.simple_list_item_1, used_teams.get(position)) ;
        holder.listView.setAdapter(list_adapter);}


    @Override
    public int getItemCount() {
//        int len = 0;
//        for (String i: used_teams){
//            if (!i.equals("")){
//                len++;
//            }
//        }
        return used_teams.size();
    }
}

