package com.example.airsoft.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.airsoft.Classes.GamesClass;
import com.example.airsoft.R;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<GamesClass> gamesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_time, map;
        public Button winner;

        public MyViewHolder(View view) {
            super(view);
            date_time = (TextView) view.findViewById(R.id.recycler_date_time_game_row);
            map = (TextView) view.findViewById(R.id.recycler_map_game_row);
            winner = (Button) view.findViewById(R.id.recycler_winner_game_row);

        }
    }


    public GamesAdapter(List<GamesClass> gamesList) {
        this.gamesList = gamesList;
    }

    @Override
    public GamesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_row, parent, false);

        return new GamesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GamesAdapter.MyViewHolder holder, int position) {
        GamesClass game = gamesList.get(position);
        holder.date_time.setText(game.getDate_time());
        holder.map.setText(game.getMap());
        holder.winner.setText(game.getWinner());

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
