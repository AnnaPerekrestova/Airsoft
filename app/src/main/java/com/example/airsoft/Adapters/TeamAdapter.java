package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.TeamClass;
import com.example.airsoft.R;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {
    private List<TeamClass> teamsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamName, teamCity, teamYears;

        public MyViewHolder(View view) {
            super(view);
            teamName = (TextView) view.findViewById(R.id.recycler_team_name);
            teamCity = (TextView) view.findViewById(R.id.recycler_city);
            teamYears = (TextView) view.findViewById(R.id.recycler_years);

        }
    }


    public TeamAdapter(List<TeamClass> teamsList) {
        this.teamsList = teamsList;
    }

    @Override
    public TeamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teams_list_row, parent, false);

        return new TeamAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamAdapter.MyViewHolder holder, int position) {
        TeamClass team = teamsList.get(position);
        holder.teamName.setText(team.getTeamName());
        holder.teamCity.setText(team.getTeamCity());
        holder.teamYears.setText(team.getTeamYear());

    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }
}

