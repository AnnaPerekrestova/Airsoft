package com.example.airsoft.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    private List<PlayerClass> membersList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, fio;

        MyViewHolder(View view) {
            super(view);
            nickname = view.findViewById(R.id.recycler_nickname);
            fio = view.findViewById(R.id.recycler_fio);
        }
    }

    public MembersAdapter(List<PlayerClass> membersList) {
        this.membersList = membersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlayerClass member = membersList.get(position);
        holder.nickname.setText(member.getNickname());
        holder.fio.setText(member.getFio());

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
