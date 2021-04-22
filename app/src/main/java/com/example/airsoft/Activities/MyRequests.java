package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.airsoft.Adapters.MyRequestsAdapter;
import com.example.airsoft.Adapters.TeamAdapter;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.Classes.TeamClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class MyRequests extends AppCompatActivity {
    MyRequestsAdapter myRequestAdapter;
    List<RequestClass> myRequestsList= new ArrayList<>();
    FirebaseData fbData = new FirebaseData().getInstance();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        recyclerView = (RecyclerView) findViewById(R.id.my_requests_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        myRequestAdapter = new MyRequestsAdapter(myRequestsList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myRequestAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                RequestClass selectedRequest = myRequestsList.get(position);
////                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();
//
//                String teamKey = (String) selectedTeam.getTeamKey();
////                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(".TeamInfoActivity");
//                intent.putExtra("teamKey", teamKey);
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        addToRecycler();
        updateRecycler();
        onRequestApprove();
    }
    public void updateRecycler(){

        fbData.getMyRequestsIfChanged(new FirebaseData.myRequestsListIfChangedCallback() {

            @Override
            public void onMyRequestsListChanged() {
                finish();
                startActivity(getIntent());
                finish();
            }

        });
    }
    public void addToRecycler(){
        fbData.getMyRequest(new FirebaseData.myRequestsListCallback() {
            @Override
            public void onMyRequestsListChanged(String requestKey, String userUID, String teamName, String status) {
                addRow(requestKey, userUID, teamName, status);
            }
        });
    }
    private void onRequestApprove(){
        //---если появилась одобренная заявка - открываем mainAct------------------
        fbData.onRequestApprove(new FirebaseData.onRequestApproveCallback() {

            @Override
            public void onRequestApprove() {
                Intent i = new Intent(".MainActivity");
                startActivity(i);
                finish();
            }
        });
    }


    private void addRow(String requestKey,String userUID,String teamName,String status) {
        RequestClass request = new RequestClass(requestKey);
        request.setRequestKey(requestKey);
        request.setStatus(status);
        request.setTeamName(teamName);
        request.setUserUID(userUID);
        myRequestsList.add(request);

        myRequestAdapter.notifyDataSetChanged();
    }
}
