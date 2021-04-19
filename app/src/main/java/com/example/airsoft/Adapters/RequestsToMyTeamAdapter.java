package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.R;

import java.util.List;

public class RequestsToMyTeamAdapter extends RecyclerView.Adapter<RequestsToMyTeamAdapter.MyViewHolder> {
    private List<RequestClass> requestsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView playerFIO, status;
        public Button approve,dismiss;

        public MyViewHolder(View view) {
            super(view);
            playerFIO = view.findViewById(R.id.request_to_my_team_FIO);
            status = view.findViewById(R.id.request_to_my_team_status);
            approve = view.findViewById(R.id.request_to_my_team_approve);
            dismiss = view.findViewById(R.id.request_to_my_team_dismiss);
        }
    }


    public RequestsToMyTeamAdapter(List<RequestClass> requestsList) {
        this.requestsList = requestsList;
    }

    @Override
    public RequestsToMyTeamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_to_my_team_list_row, parent, false);

        return new RequestsToMyTeamAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestsToMyTeamAdapter.MyViewHolder holder, int position) {
        RequestClass request = requestsList.get(position);
        holder.playerFIO.setText(request.getPlayerFIO());
        holder.status.setText(request.getStatus());

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}
