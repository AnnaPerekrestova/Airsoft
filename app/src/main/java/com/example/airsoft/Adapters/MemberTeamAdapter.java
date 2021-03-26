package com.example.airsoft.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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

public class MemberTeamAdapter extends RecyclerView.Adapter<MemberTeamAdapter.MemberTeamHolder> {
    private List<MemberTeamClass> member_team_List;

    //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
    public class MemberTeamHolder extends RecyclerView.ViewHolder { //получает макет строки
        public TextView member;
        public Spinner team;
        public Switch playF;                                                                           // что я написал?
        public MemberTeamHolder(View view) {
            super(view);
            member = (TextView) view.findViewById(R.id.recycler_member_team_row);

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
    }

//    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // do something when check is selected
        } else {
            //do something when unchecked
        }
    }


    @Override
    public int getItemCount() {
        return member_team_List.size();
    }
}

