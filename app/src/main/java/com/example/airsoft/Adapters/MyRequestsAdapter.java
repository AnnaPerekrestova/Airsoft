package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.R;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyViewHolder> {
    private List<RequestClass> requestsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamName, status;

        public MyViewHolder(View view) {
            super(view);
            teamName = (TextView) view.findViewById(R.id.my_request_team_name);
            status = (TextView) view.findViewById(R.id.my_request_status);

        }
    }


    public MyRequestsAdapter(List<RequestClass> requestsList) {
        this.requestsList = requestsList;
    }

    @Override
    public MyRequestsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_requests_list_row, parent, false);

        return new MyRequestsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyRequestsAdapter.MyViewHolder holder, int position) {
        RequestClass request = requestsList.get(position);
        holder.teamName.setText(request.getTeamName());
        holder.status.setText(request.getStatus());

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}
