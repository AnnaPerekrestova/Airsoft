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
    private String[] used_teams;
    private String id;
    List<List<String>> f = new ArrayList<>();


    //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
    public class MemberTeamHolder extends RecyclerView.ViewHolder { //получает макет строки
        public ListView listView;
        public MemberTeamHolder(View view) {
            super(view);
            listView = (ListView) view.findViewById(R.id.member_team_game_info);
        }
    }

    public MemberTeamGameAdapter(String[] used_teams, String id) { //адаптер получает значения
        this.used_teams= used_teams;
        this.id= id;

    }

    @Override
    public MemberTeamGameAdapter.MemberTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {// создает новвый объект
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_team_list_row_game_info, parent, false);
        f= GetMembersOfUsedTeams(id);
        return new MemberTeamGameAdapter.MemberTeamHolder(itemView); //новый объект будет использоваться для отображения элементов при помощи адапрера
    }

    @Override
    public void onBindViewHolder(final MemberTeamGameAdapter.MemberTeamHolder holder, int position) {
//        GetLists();
        ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
                android.R.layout.simple_list_item_1, f.get(position)) ;
        holder.listView.setAdapter(list_adapter);

//        for (List<String> team_list:member_list){
//            String s = team_list.toString();
//            Log.i("list",s);
//            ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
//                    android.R.layout.simple_list_item_1, team_list) ;
//            holder.listView.setAdapter(list_adapter);
        }

//        List<String> team_list = (List<String>) member_list.get(position);
//        String s = team_list.toString();
//        Log.i("list",s);
//        ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
//                android.R.layout.simple_list_item_1, team_list) ;
//        holder.listView.setAdapter(list_adapter);


//    }
    private List<List<String>> GetMembersOfUsedTeams(String member_team_id) {
        final List<MemberTeamClass> member_team_list = new ArrayList<>();
        final List<List<String>> list_of_teams = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MembersTeams");
        databaseRef.child("id").child(member_team_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)return;
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                    String member = postSnapShot.getKey();
                    String team = postSnapShot.getValue().toString();

                    MemberTeamClass mt = new MemberTeamClass();
                    mt.setMember(member);
                    mt.setTeam(team);
                    member_team_list.add(mt);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
        return GetLists(member_team_list);

    }

//    public List<List<String>> GetLists(){
//        List<List<String>> member_list = new ArrayList<>();
//        List<MemberTeamClass> member_team_List=GetMembersOfUsedTeams(game_id);
//        for (String t:used_teams ){
//            if (!t.equals("")) {
//                List<String> selected_team_list= new ArrayList<String>();
//                selected_team_list.add(t);
//
//                for (MemberTeamClass el: member_team_List) {
//                    String member = el.getMember();
//                    String team = el.getTeam();
//                    if (team.equals(t)) {
//                        selected_team_list.add(member);
//                    }
//                }
//                member_list.add(selected_team_list);
//            }
//        }
//      return member_list;
//    }
    public List<List<String>> GetLists(List<MemberTeamClass> member_team_List){
        List<List<String>> member_list = new ArrayList<>();
        for (String t:used_teams ){
            if (!t.equals("")) {
                List<String> selected_team_list= new ArrayList<String>();
                selected_team_list.add(t);

                for (MemberTeamClass el: member_team_List) {
                    String member = el.getMember();
                    String team = el.getTeam();
                    if (team.equals(t)) {
                        selected_team_list.add(member);
                    }
                }
                member_list.add(selected_team_list);
            }
        }
        return member_list;
    }




    @Override
    public int getItemCount() {
        int len = 0;
        for (String i: used_teams){
            if (!i.equals("")){
                len++;
            }
        }
        return len;
    }
}

