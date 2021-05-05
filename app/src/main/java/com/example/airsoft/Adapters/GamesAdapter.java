package com.example.airsoft.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.airsoft.Classes.GameClass;
import com.example.airsoft.R;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<GameClass> gamesList;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameName, gamePolygon, gameDateTime, gameWinner;

        MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.recycler_game_name);
            gameDateTime = view.findViewById(R.id.recycler_game_date);
            gamePolygon = view.findViewById(R.id.recycler_game_polygon);
            gameWinner = view.findViewById(R.id.recycler_game_winner);
        }
    }

    public GamesAdapter(List<GameClass> gamesList) {
        this.gamesList = gamesList;
    }

    @NonNull
    @Override
    public GamesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(GamesAdapter.MyViewHolder holder, int position) {
        GameClass game = gamesList.get(position);
        holder.gameName.setText(game.getGameName());
        holder.gameDateTime.setText(game.getGameDateTime());
        holder.gamePolygon.setText(game.getGamePolygonID());
        if (game.getGameWinner()!=null){
            holder.gameWinner.setText("Победитель: "+game.getGameWinner());}

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
