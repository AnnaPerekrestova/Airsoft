package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.TeamClass;
import com.example.airsoft.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> implements Filterable {
    private List<TeamClass> teamsList;
    private List<TeamClass> teamsListAll;

    public Filter getTeamCityFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
//            teamsListAll= new ArrayList<>();
                if (teamsListAll.size()==0){ teamsListAll.addAll(teamsList);}


                List<TeamClass> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(teamsListAll);
                } else {
                    for (TeamClass team: teamsListAll) {
                        if (team.getTeamCity().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(team);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                teamsList.clear();
                teamsList.addAll((Collection<? extends TeamClass>) results.values);
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
//            teamsListAll= new ArrayList<>();
                if (teamsListAll.size() == 0) {
                    teamsListAll.addAll(teamsList);
                }
                List<TeamClass> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(teamsListAll);
                } else {
                    for (TeamClass team : teamsListAll) {
                        if (team.getTeamName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(team);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                teamsList.clear();
                teamsList.addAll((Collection<? extends TeamClass>) results.values);
                notifyDataSetChanged();
            }
        };}


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
        teamsListAll= new ArrayList<>();
//        teamsListAll.addAll(teamsList);
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
        holder.teamYears.setText(team.getTeamAge());

    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }
}

