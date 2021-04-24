package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.airsoft.Adapters.RequestsToMyTeamAdapter;
import com.example.airsoft.Classes.RequestClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class RequestsToMyTeam extends AppCompatActivity {
    RequestsToMyTeamAdapter requestsToMyTeamAdapter;
    List<RequestClass> requestsToMyTeamList= new ArrayList<>();
    FirebaseData fbData = FirebaseData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_to_my_team);
        RecyclerView recyclerView = findViewById(R.id.requests_to_my_team_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this, LinearLayoutManager.VERTICAL, 16));

        requestsToMyTeamAdapter = new RequestsToMyTeamAdapter(requestsToMyTeamList);
        RecyclerView.LayoutManager teamsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(teamsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestsToMyTeamAdapter);


        addAllToRecycler();
        updateRecycler();
        addSwitchListener();
    }
    public void updateRecycler(){
        fbData.getRequestRequestsToMyTeamIfChanged(new FirebaseData.requestsToMyTeamListIfChangedCallback() {
            @Override
            public void onRequestsToMyTeamListChanged() {
                requestsToMyTeamList.clear();
                addAllToRecycler();
                requestsToMyTeamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) { }

            @Override
            public void onFilteredRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) { }
        });
    }
    private void addSwitchListener() {
        Switch mySwitch = findViewById(R.id.onlyRassm);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    requestsToMyTeamList.clear();
                    addFilteredToRecycler();
                    requestsToMyTeamAdapter.notifyDataSetChanged();
                }
                else{
                    requestsToMyTeamList.clear();
                    addAllToRecycler();
                    requestsToMyTeamAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void addFilteredToRecycler(){
        fbData.getRequestRequestsToMyTeamIfChanged(new FirebaseData.requestsToMyTeamListIfChangedCallback() {
            @Override
            public void onRequestsToMyTeamListChanged() {}

            @Override
            public void onAllRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) { }

            @Override
            public void onFilteredRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) {
                addRow(requestKey, playerUID, teamName, status);
            }
        });
    }

    public void addAllToRecycler(){
        fbData.getRequestRequestsToMyTeamIfChanged(new FirebaseData.requestsToMyTeamListIfChangedCallback() {
            @Override
            public void onRequestsToMyTeamListChanged() {}

            @Override
            public void onAllRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) {
                addRow(requestKey, playerUID, teamName, status);
            }

            @Override
            public void onFilteredRequestsToMyTeamListChanged(String requestKey, String playerUID, String teamName, String status) { }
        });
    }

    private void addRow(final String requestKey, final String playerUID, final String teamName, final String status) {
        fbData.getPersonInfo(new FirebaseData.personInfoCallback() {
            @Override
            public void onPlayerInfoChanged(String fio, String nickname, String birthday, String position, String arsenal, String teamKey) {
                RequestClass request = new RequestClass(requestKey);
                request.setRequestKey(requestKey);
                request.setStatus(status);
                request.setTeamName(teamName);
                request.setUserUID(playerUID);
                request.setPlayerFIO(fio);
                requestsToMyTeamList.add(request);

                requestsToMyTeamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onOrgInfoChanged(String fio, String birthday) {}
        },playerUID);
    }
}
