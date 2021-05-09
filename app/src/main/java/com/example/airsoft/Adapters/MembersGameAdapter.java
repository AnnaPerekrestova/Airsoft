package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

import java.util.List;

public class MembersGameAdapter extends RecyclerView.Adapter<MembersGameAdapter.MyViewHolder> {

    private List<PlayerClass> membersList;
    private String gameKey;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, fio;
        public Switch takePartFlag;

        MyViewHolder(View view) {
            super(view);
            nickname = view.findViewById(R.id.recycler_nickname);
            fio = view.findViewById(R.id.recycler_fio);
            takePartFlag = view.findViewById(R.id.recycler_take_part_switch);
        }
    }

    public MembersGameAdapter(List<PlayerClass> membersList, String gameKey) {
        this.membersList = membersList;
        this.gameKey=gameKey;
    }

    @NonNull
    @Override
    public MembersGameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_list_row, parent, false);

        return new MembersGameAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MembersGameAdapter.MyViewHolder holder, int position) {
        PlayerClass member = membersList.get(position);
        holder.nickname.setText(member.getNickname());
        holder.fio.setText(member.getFio());

        switchChange(holder,member);
    }
    private void switchChange(MembersGameAdapter.MyViewHolder holder, final PlayerClass member){
        final boolean[] f = {false};
        holder.takePartFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
            {
                final String userUID =member.getPlayerUID();
                final FirebaseData fbData = new FirebaseData().getInstance();
                fbData.getTeamKey(new FirebaseData.teamCallback() {
                    @Override
                    public void onTeamIdChanged(String teamKey) {
                        if(isChecked){
                            f[0] = true;
                        }else{
                            f[0] = false;
                        }

                        fbData.takePartInTheGame(gameKey,teamKey,userUID,f[0]);
                    }

                    @Override
                    public void onTeamNameChanged(String teamName) {}
                },userUID);


            }
        });



    }
    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
