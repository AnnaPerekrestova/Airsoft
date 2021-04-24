package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.airsoft.Adapters.PolygonsAdapter;
import com.example.airsoft.Classes.PolygonClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class Polygons extends AppCompatActivity {
    private List<PolygonClass> polygonsList = new ArrayList<>();
    private PolygonsAdapter polygonsAdapter;
    FirebaseData fbData = FirebaseData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygons);

        addListenerOnButton();

        RecyclerView recyclerView = findViewById(R.id.polygons_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this,LinearLayoutManager.VERTICAL, 16));

        polygonsAdapter = new PolygonsAdapter(polygonsList);
        RecyclerView.LayoutManager polygonsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(polygonsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(polygonsAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
        @Override
        public void onClick(View view, int position) {
//            PlayerClass selectedMember = membersList.get(position);
////                Toast.makeText(getApplicationContext(), selected_member.getFio() + " is selected!", Toast.LENGTH_SHORT).show();
//
//            String personUID = (String) selectedMember.getPlayerUID();
////                Toast.makeText(getApplicationContext(), nick + " nickname", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(".PlayerInfo");
//            intent.putExtra("playerID", personUID);
//            startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {

        }

    }));
        addToPolygonsRecycler();
}
    public void addToPolygonsRecycler(){
        fbData.getPolygonsList(new FirebaseData.polygonListCallback() {
            @Override
            public void onPolygonListChanged(String polygonKey, String polygonName, String polygonAddress, String polygonOrgcomID, boolean polygonActuality, String polygonDescription) {
                addRow(polygonKey, polygonName, polygonAddress, polygonOrgcomID, polygonActuality, polygonDescription);
            }
        });
    }

    private void addRow(String polygonKey, String polygonName, String polygonAddress,
                        String polygonOrgcomID, boolean polygonActuality, String polygonDescroption) {
        PolygonClass polygon = new PolygonClass(polygonKey);
        polygon.setPolygonName(polygonName);
        polygon.setPolygonAddress(polygonAddress);
        polygon.setPolygonOrgcomID(polygonOrgcomID);
        polygon.setPolygonActuality(polygonActuality);
        polygon.setPolygonDescription(polygonDescroption);
        polygonsList.add(polygon);

        polygonsAdapter.notifyDataSetChanged();
    }

    private void addListenerOnButton() {
        Button buttonNewPolygon = findViewById(R.id.new_polygon_button);

        buttonNewPolygon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(".CreatePolygon");
                        startActivity(i);
                    }
                }
        );
    }
}
