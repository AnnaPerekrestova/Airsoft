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
import com.example.data.FirebaseData;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<GameClass> gamesList;
    private FirebaseData fbData = FirebaseData.getInstance();

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameName, gamePolygon, gameDateTime;

        MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.recycler_game_name);
            gameDateTime = view.findViewById(R.id.recycler_game_date);
            gamePolygon = view.findViewById(R.id.recycler_game_polygon);
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
    public void onBindViewHolder(final GamesAdapter.MyViewHolder holder, int position) {
        GameClass game = gamesList.get(position);
        holder.gameName.setText(game.getGameName());
        holder.gameDateTime.setText(game.getGameDateTime());
        fbData.getPolygonInfo(new FirebaseData.polygonInfoCallback() {
            @Override
            public void onPolygonInfoChanged(String polygonName, String polygonAddress, String polygonOrgcomID, String polygonDescription, Double polygonLatitude, Double polygonLongitude) {
                holder.gamePolygon.setText(polygonName);
            }
        },game.getGamePolygonID());
        holder.gamePolygon.setText(game.getGamePolygonID());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
