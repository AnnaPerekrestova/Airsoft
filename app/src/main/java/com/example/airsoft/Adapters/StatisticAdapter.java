package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.PlayerClass;
import com.example.airsoft.R;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.MyViewHolder> {

    private List<PlayerClass> membersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fio,games, percent;

        public MyViewHolder(View view) {
            super(view);
            fio = view.findViewById(R.id.recycler_stats_fio);
            games = view.findViewById(R.id.recycler_stats_games);
            percent = view.findViewById(R.id.recycler_stats_percent);

        }
    }


    public StatisticAdapter(List<PlayerClass> membersList) {
        this.membersList = membersList;
    }

    @Override
    public StatisticAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_row, parent, false);

        return new StatisticAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatisticAdapter.MyViewHolder holder, int position) {
        PlayerClass member = membersList.get(position);
        holder.fio.setText(member.getNickname());
        holder.games.setText(member.getStatisticGames());
        holder.percent.setText(member.getStatisticPercent());

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
