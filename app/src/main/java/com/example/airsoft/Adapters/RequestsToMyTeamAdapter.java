package com.example.airsoft.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Activities.TeamInfoActivity;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.R;
import com.example.airsoft.RequestsToMyTeam;
import com.example.data.FirebaseData;

import java.util.List;

public class RequestsToMyTeamAdapter extends RecyclerView.Adapter<RequestsToMyTeamAdapter.MyViewHolder> {
    private List<RequestClass> requestsList;
    FirebaseData fbData = new FirebaseData().getInstance();

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

        checkData(holder,request);
        addListenerOnButton(holder,request);
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    private void checkData(RequestsToMyTeamAdapter.MyViewHolder holder, RequestClass request){
        if (request.getStatus().equals("рассматривается")){
            holder.status.setVisibility(View.INVISIBLE);
            holder.approve.setVisibility(View.VISIBLE);
            holder.dismiss.setVisibility(View.VISIBLE);
        }
        else{
            holder.approve.setVisibility(View.INVISIBLE);
            holder.dismiss.setVisibility(View.INVISIBLE);
            holder.status.setVisibility(View.VISIBLE);
        }
    }
    private void addListenerOnButton(final RequestsToMyTeamAdapter.MyViewHolder holder, final RequestClass request){
       holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbData.approveRequest(new FirebaseData.changeRequestStatusCallback() {
                    @Override
                    public void onChangeRequestStatus(boolean f) {
                        if (f){
                            request.setStatus("одобрена");
                        }
                    }

                }, request.getRequestKey());



            }
        });
       holder.dismiss.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               fbData.dismissRequest(new FirebaseData.changeRequestStatusCallback() {
                   @Override
                   public void onChangeRequestStatus(boolean f) {
                       request.setStatus("отклонена");
                   }
               },request.getRequestKey());

           }
       });
    }


}
