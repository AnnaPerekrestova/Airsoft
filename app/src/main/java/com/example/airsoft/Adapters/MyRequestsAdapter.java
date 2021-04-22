package com.example.airsoft.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyViewHolder> {
    private List<RequestClass> requestsList;
    FirebaseData fbData = new FirebaseData().getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamName, status;
        public Button cancelRequest;

        public MyViewHolder(View view) {
            super(view);
            teamName = (TextView) view.findViewById(R.id.my_request_team_name);
            status = (TextView) view.findViewById(R.id.my_request_status);
            cancelRequest = view.findViewById(R.id.my_request_cancel);
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

        checkData(holder,request);
        addListenerOnButton(holder,request);
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    private void checkData(MyRequestsAdapter.MyViewHolder holder, RequestClass request){
        if (request.getStatus().equals("рассматривается")){
            holder.status.setVisibility(View.INVISIBLE);
            holder.cancelRequest.setVisibility(View.VISIBLE);
        }
        else{
            holder.cancelRequest.setVisibility(View.INVISIBLE);
            holder.status.setVisibility(View.VISIBLE);
        }
    }

    private void addListenerOnButton(final MyRequestsAdapter.MyViewHolder holder, final RequestClass request){
        holder.cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.cancelRequest(request.getRequestKey());
            }
        });
    }
}
