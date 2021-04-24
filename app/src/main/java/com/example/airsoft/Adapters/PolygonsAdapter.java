package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.airsoft.Classes.PolygonClass;
import com.example.airsoft.R;

import java.util.List;

public class PolygonsAdapter extends RecyclerView.Adapter<PolygonsAdapter.MyViewHolder> {
    private List<PolygonClass> polygonsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.recycler_polygon_name);
            address = view.findViewById(R.id.recycler_polygon_address);
        }
    }

    public PolygonsAdapter(List<PolygonClass> polygonsList) {
        this.polygonsList = polygonsList;
    }

    @NonNull
    @Override
    public PolygonsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.polygon_list_row, parent, false);

        return new PolygonsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PolygonsAdapter.MyViewHolder holder, int position) {
        PolygonClass polygon = polygonsList.get(position);
        holder.name.setText(polygon.getPolygonName());
        holder.address.setText(polygon.getPolygonAddress());

    }

    @Override
    public int getItemCount() {
        return polygonsList.size();
    }
}
