package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.MyViewHolder> {

    private List<PlayerClass> membersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, percent;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.stats_nickname);
            percent = (TextView) view.findViewById(R.id.stats_percent);

        }
    }


    public StatsAdapter(List<PlayerClass> membersList) {
        this.membersList = membersList;
    }

    @Override
    public StatsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_row, parent, false);

        return new StatsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatsAdapter.MyViewHolder holder, int position) {
        PlayerClass member = membersList.get(position);
        holder.nickname.setText(member.getNickname());
        String per_str = (String) member.getStatistic();
        holder.percent.setText(per_str);

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
