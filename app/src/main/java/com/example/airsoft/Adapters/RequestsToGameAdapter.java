package com.example.airsoft.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airsoft.Activities.PlayerInfo;
import com.example.airsoft.Activities.RequestsToGame;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.Classes.RequestToGameClass;
import com.example.airsoft.R;
import com.example.data.FirebaseData;

import java.util.List;

public class RequestsToGameAdapter extends RecyclerView.Adapter<RequestsToGameAdapter.MyViewHolder> {
    private List<RequestToGameClass> requestsToGameList;
    private FirebaseData fbData = FirebaseData.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamName, status;
        Button approve,dismiss, payment;

        MyViewHolder(View view) {
            super(view);
            teamName = view.findViewById(R.id.request_to_game_teamname);
            status = view.findViewById(R.id.request_to_game_status);
            approve = view.findViewById(R.id.request_to_game_approve);
            dismiss = view.findViewById(R.id.request_to_game_dismiss);
            payment = view.findViewById(R.id.request_to_game_payment);
            fbData.getOrgFlag(new FirebaseData.orgFlagCallback() {
                @Override
                public void onOrgFlagChanged(boolean orgFlag) {
                    if (!orgFlag){
                        approve.setEnabled(false);
                        dismiss.setEnabled(false);
                        payment.setEnabled(false);
                    }
                }

                @Override
                public void onOrgFlagNull(String no_info) {

                }
            });
        }
    }
    public RequestsToGameAdapter(List<RequestToGameClass> requestsToGameList) {
        this.requestsToGameList = requestsToGameList;
    }

    @NonNull
    @Override
    public RequestsToGameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_to_game_list_row, parent, false);

        return new RequestsToGameAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestsToGameAdapter.MyViewHolder holder, int position) {
        RequestToGameClass request = requestsToGameList.get(position);
        holder.teamName.setText(request.getTeamName());
        holder.status.setText(request.getStatus());

        checkData(holder,request);
        addListenerOnButton(holder,request);
    }

    @Override
    public int getItemCount() {
        return requestsToGameList.size();
    }

    private void checkData(RequestsToGameAdapter.MyViewHolder holder, RequestToGameClass request){
        if (request.getStatus().equals("рассматривается")){
            holder.approve.setVisibility(View.VISIBLE);
            holder.dismiss.setVisibility(View.VISIBLE);
        }
        else if (request.getStatus().equals("одобрена")){
            holder.approve.setVisibility(View.INVISIBLE);
            holder.dismiss.setVisibility(View.INVISIBLE);
            holder.payment.setVisibility(View.VISIBLE);

//            holder.status.setVisibility(View.INVISIBLE);
            if (request.getPayment()){
//                holder.status.setText("оплачено");
                holder.payment.setEnabled(false);
                holder.payment.setBackgroundColor(Color.GRAY);
                holder.payment.setText("оплачено");

            }
            else {
//                holder.status.setText("не оплачено");
            }
        }
        else if (request.getStatus().equals("отклонена")){
            holder.approve.setVisibility(View.INVISIBLE);
            holder.dismiss.setVisibility(View.INVISIBLE);
        }
    }
    private void addListenerOnButton(final RequestsToGameAdapter.MyViewHolder holder, final RequestToGameClass request){
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.approveRequestToGame(new FirebaseData.changeRequestToGameStatusCallback() {
                    @Override
                    public void onChangeRequestToGameStatus(boolean f) {
                        request.setStatus("одобрена");
                    }
                }, request.getRequestKey());
            }
        });
        holder.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.dismissRequestToGame(new FirebaseData.changeRequestToGameStatusCallback() {
                    @Override
                    public void onChangeRequestToGameStatus(boolean f) {
                        request.setStatus("отклонена");
                    }
                },request.getRequestKey());

            }
        });
        holder.teamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestID = (String) request.getRequestKey();
                Intent intent = new Intent(".RequestToGameInfoActivity");
                intent.putExtra("requestID", requestID);
                v.getContext().startActivity(intent);
            }
        });
        holder.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbData.paymentRequestToGame(new FirebaseData.changeRequestToGameStatusCallback() {
                    @Override
                    public void onChangeRequestToGameStatus(boolean f) {
//                        request.setStatus("оплачено");
                        holder.payment.setEnabled(false);
                        holder.payment.setBackgroundColor(Color.GRAY);
                    }
                },request.getRequestKey(),true);
            }
        });
    }
}
