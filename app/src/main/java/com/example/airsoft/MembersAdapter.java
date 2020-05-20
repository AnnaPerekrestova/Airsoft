package com.example.airsoft;


import android.provider.MediaStore;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    private List<MembersClass> membersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, fio;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.recycler_nickname);
            fio = (TextView) view.findViewById(R.id.recycler_fio);

        }
    }


    public MembersAdapter(List<MembersClass> moviesList) {
        this.membersList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MembersClass member = membersList.get(position);
        holder.nickname.setText(member.getNickname());
        holder.fio.setText(member.getFio());

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
